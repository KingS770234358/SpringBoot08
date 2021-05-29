package com.wq.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步任务
 * 1.使用多线程实现
 */
@Service
public class AsyncService {
    @Async
    public void hello(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据正在处理...");
    }
}
