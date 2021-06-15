package cqu.lidong.spring.aop.proxy;

import cqu.lidong.ioc_aop_demo.util.TestBeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ProxyFactory {
    /**
     * JDK创建方法（需要类实现接口）：输入bean对象，一个目标类和一组Proxy接口实现, 输出一个代理对象
     */
    @SuppressWarnings("unchecked")
    public static Object createProxy(Object bean, final Class<?> targetClass, final List<Proxy> proxyList) {
        return java.lang.reflect.Proxy.newProxyInstance(TestBeanPostProcessor.class.getClassLoader(), targetClass.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object targetObject, Method targetMethod, Object[] methodParams) throws Throwable {
                return new ProxyChain(targetClass, bean, targetMethod, null, methodParams, proxyList).doProxyChain();
            }
        });
    }

    /**
     * cglib创建方法：输入一个目标类和一组Proxy接口实现, 输出一个代理对象
     */
//    public static Object createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
//
//        // TODO new ProxyChain() 出来的代理对象  属性都是null
//        // 解决方案: 使用JDK的方式创建代理对象  写一个接口 TestService TestServiceImpl
//        Object proxyBean =  Enhancer.create(targetClass, new MethodInterceptor() {
//
//            /**
//             * 代理方法, 每次调用目标方法时都会先创建一个 ProxyChain 对象, 然后调用该对象的 doProxyChain() 方法.
//             */
//            @Override
//            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
//                return new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, methodParams, proxyList).doProxyChain();
//            }
//        });
//        System.out.println("clazz:" + proxyBean);
//        return proxyBean;
//    }
}
