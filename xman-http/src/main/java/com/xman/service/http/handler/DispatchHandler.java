package com.xman.service.http.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.xman.service.http.common.*;
import com.xman.service.http.exception.ExceptionCode;
import com.xman.service.http.exception.ServiceHttpException;
import com.xman.service.http.exception.UndefinedReturnCodeFieldException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.ContentType;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yx on 2015/9/17.
 */
public class DispatchHandler extends AbstractHandler {
    Map<String, Invocation> mapping = new HashMap<String, Invocation>();
    Map<String, ParamBasic> mappingBasic = new HashMap<String, ParamBasic>();

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if ("/system/thrift/url".equalsIgnoreCase(request.getRequestURI())) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(JSON.toJSONString(mappingBasic));
            baseRequest.setHandled(true);
            return;
        }

        Invocation invocation = mapping.get(request.getRequestURI());
        if (invocation == null) {
            throw new HttpResponseException(HttpStatus.NOT_FOUND_404, "No URI mapping to " + request.getRequestURI());
        }

        try {
            // 获取header中的签名
            String sign = request.getHeader(Constant.HEAD_SIGN_KEY);
            SignThreadLocal.setSign(sign);

            Object[] args = null;
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                String contentType = request.getContentType();
                System.out.println(contentType);
                if (!StringUtils.isEmpty(contentType)) {
                    // application_form_URLEncoded/multipart_form_data/text_plain 则按kv方式获取参数
                    if (contentType.startsWith(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                            || contentType.startsWith(ContentType.MULTIPART_FORM_DATA.getMimeType())) {
                        args = getKVParam(request, invocation);
                    } else {
                        // 读取body中数据
                        byte[] reqBodyBytes = StreamUtil.readBytes(request.getInputStream(), request.getContentLength());
                        String body = new String(reqBodyBytes);
                        Map<String, Object> bodyMap = JSON.parseObject(body, Map.class);
                        args = getJSONBody(bodyMap, invocation);
                    }
                } else {
                    throw new ServiceHttpException(ExceptionCode.UnsupportedContentType, request.getContentType() + "不支持." +
                            "json请使用application/json;form格式请使用application/x-www-form-urlencoded。");
                }
            } else if ("GET".equalsIgnoreCase(request.getMethod())) {
                args = getKVParam(request, invocation);
            } else {
                throw new HttpResponseException(HttpStatus.METHOD_NOT_ALLOWED_405, request.getMethod() + "方法不支持，目前只支持Get/Post。");
            }

            Method method = invocation.getMethod();
            Object result = method.invoke(invocation.getObj(), args);

            int responseCode = HttpServletResponse.SC_OK;
            // 是否需要校验错误码字段
            if (Constant.RETURN_OBJ_NEED_CODE_PROP) {
                Object codeObj = getFieldValueByName(Constant.FIELD_RETURN_CODE, result, Boolean.TRUE);
                Integer errorCode = Integer.parseInt("" + codeObj);
                if (errorCode.equals(Constant.SUCCESS_CODE)) {
                    responseCode = HttpServletResponse.SC_OK;
                } else if (errorCode >= 1 && errorCode < Constant.LOW_FRAME_ERROR_CODE) {
                    responseCode = errorCode;
                } else if (errorCode >= Constant.LOW_FRAME_ERROR_CODE && errorCode <= Constant.HIGH_FRAME_ERROR_CODE) {
                    responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                }
            }
            response.setStatus(responseCode);
            String responseData = "";
            if (result != null) {
                if (result instanceof TBase) {
                    // thrift struct to json
                    TSerializer serializer = new TSerializer(new TSimpleJSONProtocol.Factory());
                    responseData = serializer.toString((TBase) result);
                } else if (result instanceof String) {
                    responseData = result + "";
                } else {
                    responseData = JSON.toJSONString(result);
                }
            }
            // write to response
            response.getWriter().write(responseData);
            baseRequest.setHandled(true);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
            baseRequest.setHandled(true);
        } catch (UndefinedReturnCodeFieldException ue) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write("返回类型缺少必须属性。" + ue.getExceptionMessage());
            baseRequest.setHandled(true);
        } catch (JSONException je) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write("参数解析失败。" + je.getMessage());
            baseRequest.setHandled(true);
        } catch (InvocationTargetException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("方法调用异常：" + e.getTargetException().getMessage());
            baseRequest.setHandled(true);
        } catch (TException te) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("返回结果序列化失败：" + te.getMessage());
            baseRequest.setHandled(true);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            baseRequest.setHandled(true);
        }
    }

    public Object[] getKVParam(HttpServletRequest request, Invocation invocation) {
        Object[] args = new Object[invocation.getParameterNames().length];
        for (int i = 0; i < invocation.getParameterNames().length; i++) {
            String paramName = invocation.getParameterNames()[i];
            String paramVal = request.getParameter(paramName);
            if (paramVal == null) {
                throw new IllegalArgumentException("参数缺失，请检查参数" + paramName + "是否存在。");
            }
            Class clz = invocation.getParameterTypes()[i];
            Object arg = null;
            if (clz == String.class) {
                arg = paramVal;
            } else if (List.class.isAssignableFrom(clz)) {
                Type genericType = invocation.getGenericParameterTypes()[i];
                if (genericType instanceof ParameterizedType) {
                    Class genericClass = (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                    arg = JSON.parseArray(JSON.toJSONString(paramVal), genericClass);
                } else {
                    throw new IllegalArgumentException("参数异常，获取list泛型失败。请检查参数" + paramName + "泛型类型");
                }
            } else {
                arg = JSON.parseObject(paramVal, clz);
            }
            args[i] = arg;
        }

        return args;
    }

    /**
     * json
     *
     * @param bodyMap
     * @param invocation
     * @return
     * @throws IOException
     */
    public Object[] getJSONBody(Map<String, Object> bodyMap, Invocation invocation) throws IOException {
        Object[] args = new Object[invocation.getParameterNames().length];

        for (int i = 0; i < invocation.getParameterNames().length; i++) {
            String keyName = invocation.getParameterNames()[i];
            Object keyVal = bodyMap.get(keyName);
            if (keyVal == null) {
                throw new IllegalArgumentException("参数缺失，请检查参数" + keyName + "是否存在。");
            }
            Class clz = invocation.getParameterTypes()[i];
            Object arg = null;
            if (clz == String.class) {
                arg = keyVal;
            } else if (clz == ByteBuffer.class) {
                BASE64Decoder decoder = new BASE64Decoder();
                //Base64解码
                byte[] b = decoder.decodeBuffer(keyVal + "");
                for (int j = 0; j < b.length; ++j) {
                    if (b[j] < 0) {//调整异常数据
                        b[j] += 256;
                    }
                }

                arg = ByteBuffer.wrap(b);
            } else if (List.class.isAssignableFrom(clz)) {
                Type genericType = invocation.getGenericParameterTypes()[i];
                if (genericType instanceof ParameterizedType) {
                    Class genericClass = (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                    arg = JSON.parseArray(JSON.toJSONString(keyVal), genericClass);
                } else {
                    throw new IllegalArgumentException("参数异常，获取list泛型失败。请检查参数" + keyName + "泛型类型");
                }
            } else {
                arg = JSON.parseObject(JSON.toJSONString(keyVal), clz);
            }
            args[i] = arg;
        }

        return args;
    }

    public void setMapping(Map<String, Invocation> mapping) {
        this.mapping = mapping;
        genMappingBasic();
    }

    private void genMappingBasic() {
        if (this.mapping != null) {
            for (Map.Entry<String, Invocation> entry : mapping.entrySet()) {
                Invocation invocation = entry.getValue();
                if (invocation.getMethod() != null) {
                    ParamBasic paramBasic = new ParamBasic();
                    paramBasic.setName(invocation.getMethod().getName());
                    paramBasic.setParameterTypes(invocation.getMethod().getParameterTypes());
                    paramBasic.setReturnType(invocation.getMethod().getReturnType());
                    mappingBasic.put(entry.getKey(), paramBasic);
                }
            }
        }
    }

    private Object getFieldValueByName(String fieldName, Object o, Boolean isNeeded) throws UndefinedReturnCodeFieldException {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            if (isNeeded) {
                throw new UndefinedReturnCodeFieldException("返回值中读取不到:[\"" + fieldName + "\"]的值，" +
                        "请检查[\"\" + fieldName + \"\"]的get方法是否存在");
            } else {
                return null;
            }
        }
    }

    /**
     * 根据消息类型获取返回值(非thrift对象返回)
     *
     * @param msgType
     * @param result
     * @return
     */
    public String handleMsgType(String msgType, Object result) {
        Object msgData = getFieldValueByName(Constant.FIELD_MSG_DATA, result, Boolean.TRUE);
        String msg = null;
        switch (msgType.toLowerCase()) {
            case "string":
                msg = msgData + "";
                break;
            default:
                msg = JSON.toJSONString(msgData);
                break;
        }
        return msg;
    }
}
