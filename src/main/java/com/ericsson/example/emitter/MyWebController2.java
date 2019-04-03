package com.ericsson.example.emitter;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class MyWebController2 {

    @RequestMapping("/emitter/test")
    public ResponseBodyEmitter handleRequest () {

        final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    //an HttpMessageConverter will convert BigDecimal in proper format
                    emitter.send(new BigDecimal(i));
                    emitter.send(" - ", MediaType.TEXT_PLAIN);

                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.completeWithError(e);
                    return;
                }
            }
            emitter.complete();
        });

        return emitter;
    }
}