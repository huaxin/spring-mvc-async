package com.ericsson.example.web;

import com.ericsson.example.web.service.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AsyncTaskController {

    @Autowired
    private AsyncTask asyncTask;

    @RequestMapping("/async/v3")
    public String doit() throws InterruptedException{
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName());
        String result = asyncTask.startTask();
        System.out.println(result);
        //耗时操作
        asyncTask.asyncTask();

        long currentTimeMillis1 = System.currentTimeMillis();
        return result+"task任务总耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms";

    }

    @RequestMapping("/async/v2")
    public String doTask2() throws InterruptedException{
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName());
        asyncTask.task1();
        asyncTask.task2();
        asyncTask.task3();
        long currentTimeMillis1 = System.currentTimeMillis();
        return "task任务总耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms";

    }

    @RequestMapping("/async")
    public String doTask() throws InterruptedException{
        long currentTimeMillis = System.currentTimeMillis();
        this.task1();
        this.task2();
        this.task3();
        long currentTimeMillis1 = System.currentTimeMillis();
        return "task任务总耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms";
    }

    @Async
    public void task1() throws InterruptedException{
        long currentTimeMillis = System.currentTimeMillis();
        Thread.sleep(1000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task1任务耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
    }

    @Async
    public void task2() throws InterruptedException{
        long currentTimeMillis = System.currentTimeMillis();
        Thread.sleep(2000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task2任务耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
    }
    @Async
    public void task3() throws InterruptedException{
        long currentTimeMillis = System.currentTimeMillis();
        Thread.sleep(3000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task3任务耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
    }
}
