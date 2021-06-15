package cqu.lidong.ioc_aop_demo.util;

import cqu.lidong.spring.aop.annotation.Aspect;
import cqu.lidong.spring.aop.proxy.AspectProxy;
import cqu.lidong.spring.ioc.annotation.Component;

import java.lang.reflect.Method;

/**
 * 指定某一个类的方法 实现AoP
 */
@Component
@Aspect(pkg = "cqu.lidong.ioc_aop_demo.service.impl", cls = "TestServiceImpl")
public class AfterAspect extends AspectProxy {

    /**
     * 切入点判断
     */
    @Override
    public boolean intercept(Method method, Object[] params) {
        return method.getName().equals("getInfo"); //getInfo方法切入
    }

    @Override
    public void after(Method method, Object[] params){
        System.out.println("======================== after增强 ========================");
    }
}
