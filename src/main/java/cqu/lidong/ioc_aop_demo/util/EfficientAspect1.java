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
public class EfficientAspect1 extends AspectProxy {

    private long begin;

    /**
     * 切入点判断
     */
    @Override
    public boolean intercept(Method method, Object[] params) throws Throwable {
        return method.getName().equals("getInfo"); //getInfo方法切入
    }

    @Override
    public void before(Method method, Object[] params) throws Throwable {
        System.out.println("======================== before增强_1 ========================");
        begin = System.currentTimeMillis();
    }
    
    @Override
    public void after(Method method, Object[] params) throws Throwable {
        System.out.println(String.format("time_1: %dms", System.currentTimeMillis() - begin));
        System.out.println("======================== after增强_1 ========================");
    }

    @Override
    public void error(Method method, Object[] params, Throwable e) {
        System.out.println("======================== 异常增强_1 代理失败（注入失败）========================");
    }

    @Override
    public void returnAdvice(Method method, Object[] params) {
        System.out.println("========================= 返回增强_1 =========================");
    }
}
