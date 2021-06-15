package cqu.lidong.boot_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class DispatcherServletAutoConfiguration {

    //把DispatcherServlet放入到spring IOC容器
    @Bean
    public static DispatcherServlet getDispatcherServlet(){
        return new DispatcherServlet();
    }
}
