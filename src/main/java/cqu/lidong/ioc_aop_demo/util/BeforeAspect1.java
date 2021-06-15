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
public class BeforeAspect1 extends AspectProxy {

    private long begin;

    /**
     * 切入点判断
     */
    @Override
    public boolean intercept(Method method, Object[] params) {
        return method.getName().equals("getInfo"); //getInfo方法切入
    }

    @Override
    public void before(Method method, Object[] params) {
        System.out.println("======================== before_1增强 ========================");
    }

}
