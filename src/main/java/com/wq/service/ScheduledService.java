package com.wq.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 测试定时任务的服务
 */
@Service
public class ScheduledService {

    // 在一个特定的时间执行这个方法
    // 总共六个参数: 秒 分 时 日 月 周几
    @Scheduled(cron = "0 * * * * 0-7")
    public void hello(){
        System.out.println("你被执行了");
    }
}
