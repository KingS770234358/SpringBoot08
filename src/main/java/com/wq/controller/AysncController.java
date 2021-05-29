package com.wq.controller;
/**
 * 测试异步的Controller
 */

import com.wq.service.AsyncService;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AysncController {
    @Autowired
    AsyncService asyncService;

    @ResponseBody
    @RequestMapping("/testAsync")
    public String testAsync(){
        asyncService.hello(); // 这里会停止3s,导致前台白屏3s 转圈...
        return "hello";
    }
}
