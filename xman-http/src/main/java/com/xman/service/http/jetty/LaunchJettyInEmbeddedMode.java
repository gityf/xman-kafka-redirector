package com.xman.service.http.jetty;

import com.xman.service.http.annotation.HttpService;
import com.xman.service.http.annotation.HttpServiceMethod;
import com.xman.service.http.common.Invocation;
import com.xman.service.http.exception.DuplicatedMappingURIException;
import com.xman.service.http.exception.SpringContextNullException;
import com.xman.service.http.handler.DispatchHandler;
import com.xman.service.http.scan.LoadPackageClasses;
import com.xman.service.http.exception.UnknownScanPackageException;
import com.xman.service.http.reflect.ReflectionWithSpringUtil;
import org.eclipse.jetty.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LaunchJettyInEmbeddedMode {

    // jetty启动端口，默认端口为8181
    private int port;
    // 支持多包扫描
    private String[] scanPackages;
    private Server server;
    private ApplicationContext context;

    /**
     * start up jetty
     *
     * @throws Exception
     */
    public void startup() throws Exception {

        if (StringUtils.isEmpty(this.scanPackages)) {
            throw new UnknownScanPackageException("未定义需要扫描的包名");
        }
        if (this.context == null) {
            throw new SpringContextNullException("spring context Null error.Http模块依赖Spring上下文");
        }
        if (this.port == 0) {
            this.port = 8181;
        }
        if (this.server == null) {
            this.server = new Server(port);
        }

        Map<String, Invocation> mappings = getMappings();
        DispatchHandler handler = new DispatchHandler();
        handler.setMapping(mappings);
        server.setHandler(handler);

        server.setStopAtShutdown(true);

        server.start();
    }

    /**
     * shutdown jetty
     *
     * @throws Exception
     */
    public void shutdown() throws Exception {
        if (this.server != null) {
            try {
                this.server.stop();
            } catch (Exception e) {
                throw e;
            }
        }
    }

    private Map<String, Invocation> getMappings() throws Exception {
        Map<String, Invocation> mapping = new HashMap<String, Invocation>();

        LoadPackageClasses loadPackageClasses = new LoadPackageClasses(scanPackages, HttpService.class);

        Set<Method> methods = loadPackageClasses.getMethodSet();
        for (Method method : methods) {
            Object obj = context.getBean(method.getDeclaringClass());
            // use spring reflect @ 2015/12/4 for try catch
            Invocation invocation = ReflectionWithSpringUtil.getInvocation(obj, method);
            HttpServiceMethod annotation = method.getAnnotation(HttpServiceMethod.class);
            if (annotation != null) {
                String URI = annotation.URI();
                if (!URI.startsWith("/")) {
                    URI = "/" + URI.trim();
                }
                if (mapping.containsKey(URI)) {
                    throw new DuplicatedMappingURIException("配置的映射URI:{" + URI + "}重复异常。");
                }
                mapping.put(URI, invocation);
            }
        }

        return mapping;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}