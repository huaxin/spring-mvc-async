package com.ericsson.example.web;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns = "/demo", asyncSupported = true )
public class DemoServlet extends HttpServlet {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            100, 200,
            50000L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(100));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync();

        //**********阻塞执行
        run();
        response.getWriter().write("Hello World!");

        //**********调用AsyncContext的start()方法

        asyncContext.start(() -> {
            run();
            try {
                asyncContext.getResponse().getWriter().write("Hello World!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            asyncContext.complete();
        });

        //**********手动创建线程

        Runnable runnable = () -> {
            this.run();
            try {
                asyncContext.getResponse().getWriter().write("Hello World!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            asyncContext.complete();
        };

        new Thread(runnable).start();

        //************自定义异步线程池
        AsyncContext asyncContext1 = request.startAsync();

        executor.execute(() -> {
            this.run();
            try {
                asyncContext1.getResponse().getWriter().write("Hello World!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            asyncContext.complete();
        });

        //**********servlet 3.1 IO非阻塞
        //ServletInputStream添加了一个ReadListener，并在ReadListener的onAllDataRead()方法中完成了长时处理过程。
        AsyncContext asyncContext2 = request.startAsync();

        ServletInputStream inputStream = request.getInputStream();
        inputStream.setReadListener(new ReadListener() {
            @Override
            public void onDataAvailable() throws IOException {

            }

            @Override
            public void onAllDataRead() throws IOException {
                executor.execute(() -> {
                    run();
                    try {
                        asyncContext.getResponse().getWriter().write("Hello World!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    asyncContext.complete();

                });
            }

            @Override
            public void onError(Throwable t) {
                asyncContext.complete();
            }
        });

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("UTF-8");
    }

    public void run() {
        try {
            System.out.println(String.format("线程：%s 开始处理",Thread.currentThread().getName()));
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
