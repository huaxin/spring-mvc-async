package com.ericsson.example.web.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AsyncTask {

    public String startTask() throws InterruptedException{
        Thread.sleep(1000);
        return "starting....";
    }

    public void asyncTask() throws InterruptedException{
        //耗时操作
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    Thread.sleep(10000);
                    System.out.println("耗时操作.......");
                    long currentTimeMillis1 = System.currentTimeMillis();
                    System.out.println("耗时任务总耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    @Async
    public void task1() throws InterruptedException{
        System.out.println(Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();
        Thread.sleep(1000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task1任务耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
    }

    @Async
    public void task2() throws InterruptedException{
        System.out.println(Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();
        Thread.sleep(2000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task2任务耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
    }
    @Async
    public void task3() throws InterruptedException{
        System.out.println(Thread.currentThread().getName());
        long currentTimeMillis = System.currentTimeMillis();
        Thread.sleep(3000);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("task3任务耗时:"+(currentTimeMillis1-currentTimeMillis)+"ms");
    }
}
