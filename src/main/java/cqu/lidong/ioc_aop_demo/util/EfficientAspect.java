package cqu.lidong.ioc_aop_demo.util;

import cqu.lidong.spring.aop.annotation.Aspect;
import cqu.lidong.spring.aop.proxy.AspectProxy;
import cqu.lidong.spring.ioc.annotation.Component;

import java.lang.reflect.Method;

/**
 * 指定某一个类的方法 实现AoP
 * 不同切面需要单独写一个类 类似 BeforeAspect AfterAspect .. *Aspect
 * 不然无法按序完成 因为是递归调用
 *
 * 以下是正常的调用顺序
 * before增强
 * before_1增强
 * ...
 * after增强
 * after_1增强
 *
 * 如果像EfficientAspect把所有切面都写在一个类 不分开写的话
 * before增强
 * before_1增强（递归调用）
 * ...
 * after_1增强（递归调用）
 * before增强
 */
//@Component
@Aspect(pkg = "cqu.lidong.ioc_aop_demo.service.impl", cls = "TestServiceImpl")
public class EfficientAspect extends AspectProxy {

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
        System.out.println("======================== before增强_2 ========================");
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Method method, Object[] params) {
        System.out.printf("time: %dms%n", System.currentTimeMillis() - begin);
        System.out.println("======================== after增强_2 ========================");
    }

    @Override
    public void error(Method method, Object[] params, Throwable e) {
        System.out.println("======================== 异常增强_2 代理失败（注入失败）========================");
    }

    @Override
    public void returnAdvice() {
        System.out.println("========================= 返回增强_2 =========================");
    }
}
