package com.ericsson.example.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class TestSseEmitterController {
    private static final Logger logger = LoggerFactory.getLogger(TestSseEmitterController.class);

    private DeferredResult<String> deferredResult = new DeferredResult<>();

    private SseEmitter sseEmitter = new SseEmitter();

    /**
     * 返回SseEmitter对象
     *
     * @return
     */
    @RequestMapping("/testSseEmitter")
    public SseEmitter testSseEmitter() {
        return sseEmitter;
    }

    /**
     * 向SseEmitter对象发送数据
     *
     * @return
     */
    @RequestMapping("/setSseEmitter")
    public String setSseEmitter() {
        try {
            sseEmitter.send(System.currentTimeMillis());
        } catch (IOException e) {
            logger.error("IOException!", e);
            return "error";
        }

        return "Succeed!";
    }

    /**
     * 将SseEmitter对象设置成完成
     *
     * @return
     */
    @RequestMapping("/completeSseEmitter")
    public String completeSseEmitter() {
        sseEmitter.complete();

        return "Succeed!";
    }
}
