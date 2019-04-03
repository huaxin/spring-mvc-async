package com.ericsson.example.async;

import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * Wrapping Callable in WebAsyncTask
 */
@Controller
public class WebAsyncTaskController {

    @RequestMapping("/webAsyncTask")
    @ResponseBody
    public WebAsyncTask<String> handleRequest (HttpServletRequest r) {
        System.out.println("asyncSupported: " + r.isAsyncSupported());
        System.out.println(Thread.currentThread().getName());

        Callable<String> callable = () -> {
            Thread.sleep(200000);
            System.out.println(Thread.currentThread().getName());
            return "WebAsyncTask test";
        };

        ConcurrentTaskExecutor t = new ConcurrentTaskExecutor(
                Executors.newFixedThreadPool(1));
        return new WebAsyncTask<>(10000L, t, callable);
    }

    @RequestMapping("/webAsyncTask2")
    @ResponseBody
    public WebAsyncTask<String> handleRequest2 (HttpServletRequest r) {
        System.out.println("asyncSupported: " + r.isAsyncSupported());
        System.out.println(Thread.currentThread().getName());

        ConcurrentTaskExecutor t = new ConcurrentTaskExecutor(Executors.newFixedThreadPool(1));
        WebAsyncTask<String> asyncTask = new WebAsyncTask<>(10 * 1000L, t, () -> {
            System.out.println(String.format("异步工作线程：%s", Thread.currentThread().getName()));
            // 任务处理时间5s，不超时
            //Thread.sleep(5 * 1000L);
            //return "任务执行完毕";
            //Thread.sleep(15 * 1000L);
            throw new Exception("任务出错了");
        });
        asyncTask.onCompletion(() -> System.out.println("任务执行完成"));
        asyncTask.onTimeout(() -> {
            System.out.println("任务执行超时");
            return "任务执行超时";
        });
        asyncTask.onError(() -> {
            System.out.println("任务执行异常");
            return "任务执行异常";
        });
        System.out.println("继续处理其他事情");
        return asyncTask;
    }
}