08分布式Dubbo_Zookeeper_SpringBoot
Nginx代理服务器
理论部分详见[08分布式理论.pdf]

传输过程要求对象[序列化====方便数据传输]
RPC的核心 通讯 和 序列化

Dubbo就是专注解决RPC这一系列问题的项目 2018年重启该项目 
Dubbo 3.x ### 一些Error 和 exception 可能是网路问题 也可能是对方服务不存在的问题

http SpringCloud 是一个生态 更强大

前台 [中台] 后台

1.Dubbo
·运行原理(架构)[十分重要 根据图08Dubbo运行过程.PNG结合pdf理解]

2.Zookeeper是一个出色的注册中心 还有其他的 比如 阿里巴巴的Nacos Redis
可以管理这些东西: Hadoop Hive 等等等等
负责服务的[注册]和[发现]

3.下载安装ZooKeeper3.5.6
http://mirror.bit.edu.cn/apache/zookeeper/zookeeper-3.5.6/
##################################################################################################
# 说明
# 原来从目前的最新版本3.5.5开始，带有bin名称的包才是我们想要的下载可以直接使用的里面有编译后的二进制的包，
# 而之前的普通的tar.gz的包里面是只是源码的包无法直接使用。
#################################################################################################
· 管理员身份运行bin目录下的zkServer.cmd
[调试技巧 防止闪退 打开文件 在里面最后加入一行pause就可以看到报错的明确信息]
· 创建Zookeeper的配置文件[直接在conf文件夹下 copy一份zoo_sample.cfg 改名为 zoo.cfg]
  要想正常启动就需要有这个zoo.cfg配置文件
  这里面初始配置了[根目录 和 端口2181 和 admin.serverPort=2182]
· 启动客户端zkCli.cmd连接测试[以后的客户端就是java程序]
  zk: localhost:2181(CONNECTING) 0
  第一条命令返回 WatchedEvent state:SyncConnected type:None path:null 是在激活?
  ls / 查看当前所有的节点
  create -e /wq 123 新建节点 key为/wq 值为123
  get /wq 获取节点的值
====>ZooKeeper是一个注册服务的中心

4.Dubbo的下载和安装(不是一个服务软件 是一个jar包 是一个maven项目)
  托管在github上:https://github.com/apache/dubbo-admin/tree/master
  · 查看Dubbo的配置 就在dubbo-admin/src/main/resources下的application.properties
    确认配置是否合适 否则作相应修改
  · 在项目目录下(dubbo-admin-master下)使用maven打包 mvn clean package -Dmaven.test.skip=true
    生成的包在dubbo-admin-master/dubbo-admin/target目录下
  · 运行jar包 java -jar xxx.jar(双击也可以执行) 没打开ZooKeeper的情况下会持续报错
  · 打开ZooKeeper服务端 dubbox运行窗口出现Tomcat started on port(s): 7001 (http) with context path ''
  · 浏览器访问localhost:7001端口 用户名密码都是 root
====>这个dubbo-admin就是一个监控管理后台的网页 可以查看注册了哪些服务 哪些服务被消费(调用/使用)了
====>Dubbo是那个jar包才是核心

5.编写代码使用Dubbo和ZooKeeper
1.首先创建两个子module分别为生产者和消费者web
2.在两个module的全局配置文件里分别配置两个module使用的端口8001 8002 
3.在生产者里编写服务接口TicketService及其实现类TicketServiceImpl
4.生产者寻找服务中心注册自己的方法
  · pom.xml中导入依赖 [Apache dubbo启动器依赖] 和 [maven仓库里的github上的zookeeper依赖] 
    共计四个依赖,还需要剔除slf4j log4j12依赖
    <!-- 使用dubbo的启动器依赖 -->
    <dependency>
        <groupId>org.apache.dubbo</groupId>
        <artifactId>dubbo-spring-boot-starter</artifactId>
        <version>2.7.3</version>
    </dependency>
    <!-- 使用ZooKeeper需要的依赖 -->
    <!-- zkclient 使用ZooKeeper注册中心所需要的依赖 -->
    <dependency>
        <groupId>com.github.sgroschupf</groupId>
        <artifactId>zkclient</artifactId>
        <version>0.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.curator</groupId>
        <artifactId>curator-recipes</artifactId>
        <version>4.2.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.zookeeper</groupId>
        <artifactId>zookeeper</artifactId>
        <version>3.5.6</version>
        <!-- 排除slf4j-log4j12 防止日志冲突 -->
        <exclusions>
            <exclusion>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-log4j12</artifactId>
            </exclusion>
            <exclusion>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
  · 开始配置生产者的全局配置文件
    ## 生产者服务的配置
    dubbo:
      application:
      ## 生产者要告诉ZooKeeper注册中心自己这个服务应用的名字(暴露服务)
        name: 08dubbo_zookeeper_producer
      ## 要去哪个注册中心注册的地址
      registry:
        address: zookeeper://127.0.0.1:2181
      ## 注册服务应用里的那些东西 扫描这个包下 带有@Service注解的类
      scan:
        base-packages: com.wq.service [这里少了个s、、、dubbo一直扫描不到...]
  · 在TicketServiceImpl方法上加注解 先用@Component注入到服务应用的Spring容器中
    再用[dubbo的 @Service注解]注入到注册中心中,[因此上面不要用@Service注解 而用@Component注解]
  · 查看被注册到了那个端口 开启ZooKeeper和Dubbo 运行生产者 浏览器访问localhost:7001查看
    服务被绑定到20880端口
==============================消费者====================================
5.在消费这里调用生产者的方法 编写UserService 调用注册中心的方法
  · 同样要导入依赖(如上)
  · 在消费者应用全局配置文件application.yaml配置去哪里拿服务
    ## 消费者的服务配置
    # 消费者拿服务的时候 需要告诉注册中心自己是谁(暴露自己的服务名)
    dubbo:
     application:
      name: 08dubbo_zookeeper_consumer
     registry:
      address: zookeeper://127.0.0.1:2181
  · 使用注册中心中注册的方法 需要使用dubbo的@Reference注解远程引用
    在目录下需要一个跟生产者[路径相同的一个TicketService接口]
    // 消费者应用这个类隔壁有个TicketService的接口
    import org.springframework.stereotype.Service;
    @Service [这里使用的是Spring的@Service注解 注入到容器中 与dubbo注册到注册中心中的不一样]
    public class UserService {
        // 想拿到生产者提供的方法 要去注册中心拿
        @Reference
        TicketService TICKET_SERVICE;
        public void buyTicket(){
            String msg = TICKET_SERVICE.getTicket(5);
            System.out.println(msg);
        }
  · 在测试目录下写测试使用消费者应用的服务
####步骤总结
前提ZooKeeper服务已开启
1. 提供者提供服务
   1.导入依赖
   2.全局配置文件配置注册中心地址,服务发现名,和要扫描的包
   3.在需要注册的服务类上使用[dubbo的]@Service注解
2. 消费者使用服务
   1.导入相同的依赖
   2.全局配置文件配置注册中心地址,配置自己的服务名
   3.从远程注入服务