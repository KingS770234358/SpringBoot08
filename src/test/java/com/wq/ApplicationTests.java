package com.wq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class ApplicationTests {

    // 注入发送邮件的核心实现类
    @Autowired
    JavaMailSender mailSender;

    @Test // 1.测试发送简单的邮件消息
    void contextLoads() {
        // 1.1 先获得简单邮件消息对象
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 1.2 看看这个对象有哪些方法 setTo 发送给谁  setFrom 从哪里来 setText 发送的文本 setSubject 发送的主题
        mailMessage.setSubject("王强你好!");
        mailMessage.setText("唧唧复唧唧,木兰当户织");
        mailMessage.setTo("3095674456@qq.com");
        // 这个是经过qq邮箱转发的 所以需要注明邮件的来源
        mailMessage.setFrom("770234358@qq.com");
        mailSender.send(mailMessage);
    }
    @Test // 2.测试发送复杂的邮件消息
    void contextLoads2() throws MessagingException {
        // 2.1 先获得复杂邮件消息对象 MimeMessage 两种创建方式
        // MimeMessage mimeMessage = new MimeMessage();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 2.2开始组装 (纯文本 图片 附件) 使用MimeMessageHelper类帮助组装
        // 构造函数参数列表(MimeMessage对象,是否支持多文件multipart,编码"utf-8")
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"utf-8");
        // 2.3 点进MimeMessageHelper查看如何使用(有多个重载 编码 是否multipart)
        // 2.4 开始组装
        // 看看这个对象有哪些方法 setTo 发送给谁  setFrom 从哪里来 setText 发送的文本 setSubject 发送的主题
        // 2.4.1 正文
        mimeMessageHelper.setSubject("王强你好! ---- 多文件multipart");
        // setText第二个参数可以设置 是否解析HTML
        mimeMessageHelper.setText("<p style='color: red'>唧唧复唧唧,木兰当户织</p>",true);
        // 2.4.2 附件 (参数1:对方接受到的文件名,参数2:File)====这边创建File的时候使用文件的绝对路径即可
        mimeMessageHelper.addAttachment("1.jpg",new File("C:\\Users\\KingS\\Desktop\\狂神ssm截图和笔记备份等等\\07Ajax执行大致流程.PNG"));
        // 2.4.3 其他基本信息
        mimeMessageHelper.setTo("3095674456@qq.com");
        // 这个是经过qq邮箱转发的 所以需要注明邮件的来源
        mimeMessageHelper.setFrom("770234358@qq.com");
        mailSender.send(mimeMessage);
    }

}
