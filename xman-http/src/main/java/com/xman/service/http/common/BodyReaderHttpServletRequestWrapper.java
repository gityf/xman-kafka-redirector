package com.xman.service.http.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request)
            throws IOException {
        super(request);
        // content-type: application/x-www-form-urlencoded
        Map<String, String[]> paramMap = this.getParameterMap();
        body = StreamUtil.readBytes(request.getInputStream(), request.getContentLength());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

//    @Override
//    public ServletInputStream getInputStream() throws IOException {
//        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
//        return new ServletInputStream() {
//
//            @Override
//            public int read() throws IOException {
//                return bais.read();
//            }
//        };
//    }

    public byte[] getBody() {
        return this.body;
    }
}
