package com.ericsson.example.async;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;


@Controller
public class CallableController {

    @RequestMapping("/")
    @ResponseBody
    public Callable<String> handleRequest (HttpServletRequest r) {
        System.out.println("asyncSupported: " + r.isAsyncSupported());
        System.out.println(Thread.currentThread().getName());

        return new Callable<String>() {
            @Override
            public String call () throws Exception {
                System.out.println(Thread.currentThread().getName());
                return "some string";
            }
        };
    }
}
