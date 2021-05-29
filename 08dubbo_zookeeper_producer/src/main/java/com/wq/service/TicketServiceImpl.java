package com.wq.service;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

// ZooKeeper:服务的注册与发现
@Service // 加上这个注解就可以被扫描到 项目启动就被自动注册到注册中心
@Component // 使用了dubbo后尽量不要使用Spring的Service注解注入服务 会跟dubbo的Service注解冲突
public class TicketServiceImpl implements TicketService {
    @Override
    public String getTicket(int num) {

        return "buy "+num+" tickets successfully";
    }
}
