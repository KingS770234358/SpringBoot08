package com.wq.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    // 想拿到生产者提供的方法 要去注册中心拿
    @Reference
    TicketService TICKET_SERVICE;

    public void buyTicket(int num){
        String msg = TICKET_SERVICE.getTicket(num);
        System.out.println(msg);
    }

}
