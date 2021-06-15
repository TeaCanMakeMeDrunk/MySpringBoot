package cqu.lidong.ioc_aop_demo.util;

import cqu.lidong.ioc_aop_demo.service.impl.TestServiceImpl;
import cqu.lidong.spring.aop.annotation.Aspect;
import cqu.lidong.spring.aop.proxy.AspectProxy;
import cqu.lidong.spring.aop.proxy.Proxy;
import cqu.lidong.spring.aop.proxy.ProxyFactory;
import cqu.lidong.spring.ioc.bean.util.BeanPostProcessor;
import cqu.lidong.spring.ioc.annotation.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 实现BeanPostProcessor接口，可以干预Bean的创建初始化，自定义修改Bean 实现AoP等
 */
@Component
public class TestBeanPostProcessor implements BeanPostProcessor {
    //存放类以及对应的 AoP通知
    private Map<Class, List<Proxy>> proxyMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (beanName.equals("testService")) {
            ((TestServiceImpl) bean).setBeanName("实现Aware接口方法后，进行修改，postProcessBeforeInitialization修改之后的beanName");
            System.out.println("beanName == testService, 初始化之前, 写自己逻辑，修改bean什么的 或者 返回一个新的bean");
        }
//        System.out.println("初始化之前, 写自己逻辑，修改bean什么的 或者 返回一个新的bean");
        return bean;
    }

    /**
     * 实现AoP
     *
     * @param bean
     * @param beanName
     * @return
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // System.out.println("beanName == testService, 初始化之后, 写自己逻辑，修改bean什么的 或者 返回一个新的bean, 此外，AoP就是在这个里面实现的");

        // 实现AoP逻辑 生成代理对象
        // 1.记录一个类 存在哪些代理
        // （代理类应该优先加载, setProxyListOfClass放在初始化IoC容器第一步就行,先忽略,这里使用懒加载的方式模拟，所有代理都加载完后才加载需要代理的类，不然proxyList不全）
        setProxyListOfClass(bean);
        //2.使用代理工厂创造代理对象，覆盖bean
        bean = createProxyObject(bean);
        return bean;
    }

    /**
     * 使用代理工厂创造代理对象，覆盖bean
     *
     * @param bean
     * @return
     */
    private Object createProxyObject(Object bean) {
        Class clazz = bean.getClass(); //根据对象获得类
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
        }
        if (proxyMap.containsKey(clazz)) {
            //覆盖Bean容器里目标类对应的实例, 下次从Bean容器获取的就是代理对象了
            List<Proxy> proxyList = proxyMap.get(clazz);
            Object proxyObject = ProxyFactory.createProxy(bean, clazz, proxyList);
            // 创建代理对象后，TestService里面的属性全变为null了
            // 解决方案: 使用JDK的方式创建代理对象  写一个接口 TestService TestServiceImpl
            bean = proxyObject;
        }
        return bean;
    }

    /**
     * 记录一个类 存在哪些代理
     *
     * @param bean
     */
    private void setProxyListOfClass(Object bean) {
        Class clazz = bean.getClass(); //根据对象获得类

        // 如果当前bean 继承了 AspectProxy 且 使用了切面注解  AspectProxy实现了Proxy接口
        if (AspectProxy.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(Aspect.class)) {

            Aspect Aspect = (Aspect) clazz.getAnnotation(Aspect.class);
            try {
                //继承了 AspectProxy 且 使用了切面注解 的clazz（EfficientAspect） 这个类里面指定了哪个类使用AoP 获取出来
                //获得需要代理的类
                Class clazz1 = Class.forName(Aspect.pkg() + "." + Aspect.cls());
                // 一个类 可能存在多个代理
                List<Proxy> proxyList = proxyMap.getOrDefault(clazz1, new ArrayList<>());
                proxyList.add((Proxy) bean);
                proxyMap.put(clazz1, proxyList);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
