package cqu.lidong.boot_demo.initializer;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;


/**
 * ApplicationContextAware 获取applicationContext的接口
 */
@Component
public class MyServlet implements ApplicationContextAware {
    private static ApplicationContext context;


    public DispatcherServlet getServlet() {
        return context.getBean(DispatcherServlet.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
