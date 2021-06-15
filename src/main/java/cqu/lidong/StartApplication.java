package cqu.lidong;

import cqu.lidong.boot_demo.config.SpringBootScanAddressConfig;
import cqu.lidong.boot_demo.webServer.WebServer;
import cqu.lidong.boot_demo.webServer.impl.MyTomcat;
import cqu.lidong.ioc_aop_demo.service.TestService;
import cqu.lidong.ioc_aop_demo.util.ScanAddressConfig;
import cqu.lidong.spring.ioc.MyApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

//@SpringBootApplication
// 如果是正常的springboot启动的话 加这个注解 然后main里面写SpringApplication.run(StartApplication.class, args);
//就可以加载controller那个地址映射了
//我们模拟的就是这个注解下 底层原理
public class StartApplication {
    public static void main(String[] args) throws Exception {
        // =================== 模拟IoC与AoP ===================
        // 自定义的Spring 初始化IoC  非懒加载模式创建bean（static）
        System.out.println("------------------------------容器中创建Bean------------------------------");
        MyApplicationContext ac = new MyApplicationContext(ScanAddressConfig.class);

        //测试 原型 or 懒加载单例
//        System.out.println("getBean_testService:" + ac.getBean("testService"));
//        System.out.println("getBean_testService:" + ac.getBean("testService"));
//        System.out.println("getBean_testService:" + ac.getBean("testService"));
        System.out.println("------------------------------容器中创建Bean完成------------------------------\n");

        System.out.println("------------------------------测试Autowired依赖注入 以及 AoP------------------------------");
        //测试Autowired 依赖注入 以及 AoP   AoP的话从Spring容器里面拿到的是代理对象
        TestService testService = (TestService) ac.getBean("testService");
        testService.getInfo();
        System.out.println("-------------------------------完成Autowired依赖注入 以及 AoP-------------------------------\n");




        // =================== 模拟SpringBoot零配置（需导入Spring MVC依赖） ===================
        //1.加载spring web应用和配置（初始化IoC）
        //完成原来spring里面的配置 applicationContext.xml文件里面的扫描 springBean扫描
//        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
//        ac.register(SpringBootScanAddressConfig.class);
//        ac.refresh();
//
////        2.启动内嵌tomcat
//        WebServer ws = new MyTomcat();
//        ws.getServer();

        //3.之后把DispatcherServlet加载到IoC容器 然后加载到tomcat，tomcat会调用WebApplicationInitializer这个接口的onStartup实现方法

        //1.初始化ioc
        //2.启动Tomcat
        //3.SPI机制 + Servlet3.0规范，任何容器启动时都会调用META-INF/services/javax.servlet.ServletContainerInitializer 里面的实现类的onStartup()
        // DispatcherServlet 先加载到IoC（DispatcherServletAutoConfiguration） 再加载到tomcat(利用tomcat实现的SPI来启动onStartup，而不是我们实现的，用Spring的SPI机制)
        // 类似WebApplicationInitializer，不过本质还是调用ServletContainerInitializer这个方法

        //为什么都要想办法取代调用实现ServletContainerInitializer接口的类方法呢？
        //除了基本功能，还可以自己添加一个listener监听器等
        //SpringMVC的核心就是DispatcherServlet，DispatcherServlet实质也是一个HttpServlet。DispatcherServlet负责请求分发，所有的请求都由经过它来统一分发给相应的controller。


        //Mysql加载driver的时候指定了应用加载器（应该是先给最上层的启动类加载器的）打破了双亲委派机制
        //所有驱动都是DriverManager，去加载。而DriverManager是java的启动类，是由启动类加载器加载
        //但mysql驱动不能由启动类去加载, DriverManager是用来管理所有驱动的管理器，属于Java的启动类，但是mysql驱动不属于java的启动类
        //所以就在DriverManager加载mysql驱动时候，直接指定了类加载器为应用程序类加载器
    }
}
