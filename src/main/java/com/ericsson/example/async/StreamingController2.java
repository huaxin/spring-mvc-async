package com.ericsson.example.async;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

@Controller
public class StreamingController2 {

    @RequestMapping("/test")
    public StreamingResponseBody handleRequest (HttpServletRequest r) {
        System.out.println("asyncSupported: "+ r.isAsyncSupported());
        System.out.println(Thread.currentThread().getName());

        return new StreamingResponseBody() {
            @Override
            public void writeTo (OutputStream outputStream) throws IOException {
                System.out.println(Thread.currentThread().getName());
                outputStream.write("from test request".getBytes());
            }
        };
    }
}