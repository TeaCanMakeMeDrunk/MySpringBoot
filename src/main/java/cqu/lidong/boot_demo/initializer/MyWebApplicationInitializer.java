package cqu.lidong.boot_demo.initializer;

import javax.servlet.*;
import java.util.Set;

/**
 *  tomcat会调用实现了ServletContainerInitializer接口的类，
 *  本质是通过SPI机制 发现实现了ServletContainerInitializer接口的函数。
 *  而继承WebApplicationInitializer这个接口也可以让tomcat调用，是因为HandlesTypes注解，调用了onStartup方法,里面在调用了继承WebApplicationInitializer这个接口的类的onStartup方法。
 *  步骤：
 *      1.SPI机制发现SpringServletContainerInitializer = A
 *      2.调用A里面的onStartup方法
 *      3.A里面的onStartup方法调用所有扫描到的 实现WebApplicationInitializer的类 的onStartup方法
 *  @ HandlesTypes({WebApplicationInitializer.class}) //扫描所有实现WebApplicationInitializer的类
 * public class SpringServletContainerInitializer implements ServletContainerInitializer {
 *      //ServletContainerInitializer SPI机制发现 /META-INF/services/javax.servlet.ServletContainerInitializer(接口名)
 *      //里面写实现类的包地地址
 *
 *      //webAppInitializerClasses就是所有扫描到的WebApplicationInitializer类
 *      public void onStartup(@Nullable Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {
 *      }
 * }
 */
public class MyWebApplicationInitializer extends MyServlet implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        //加载SpringMVC ---- 负责注册servlet，最终给web容器（tomcat） 替代配置文件spring-mvc.xml

        //tomcat对象内部利用SPI机制 收集A={实现了ServletContainerInitializer这个接口的所有类}，然后遍历A，调用A.onStartup()
        //把一个servlet给tomcat(servletContext就是tomcat对象)
        ServletRegistration.Dynamic registration = servletContext.addServlet("app", getServlet());

        registration.setLoadOnStartup(1);
        //servlet的访问路径
        registration.addMapping("/");
    }
}

