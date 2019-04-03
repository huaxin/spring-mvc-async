package com.ericsson.example.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet(name = "helloServlet", urlPatterns = "/firstServlet",asyncSupported = true)
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet (HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        PrintWriter writer = response.getWriter();
        AsyncContext asyncContext = request.startAsync();
        asyncContext.addListener(new MyAsyncListener());

        ServletInputStream servletInputStreams = request.getInputStream();
        //异步读取（实现了非阻塞式读取）
        servletInputStreams.setReadListener(new MyReadListener(servletInputStreams,asyncContext));
        // 直接输出到页面的内容(不等异步完成就直接给页面)
        ServletOutputStream servletOutputStream = response.getOutputStream();
        //.....
        servletOutputStream.setWriteListener(new MyWriteListener(servletOutputStream,asyncContext));

        AsyncContext finalAsyncContext = asyncContext;
        asyncContext.start(new Runnable() {
            @Override
            public void run () {
                String msg = task();
                writer.println(msg);
                finalAsyncContext.complete();
            }
        });
    }

    private String task () {
        long start = System.currentTimeMillis();
        try {
            int i = ThreadLocalRandom.current()
                    .nextInt(1, 5);
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "time to complete long task " + (System.currentTimeMillis() - start);
    }
}
