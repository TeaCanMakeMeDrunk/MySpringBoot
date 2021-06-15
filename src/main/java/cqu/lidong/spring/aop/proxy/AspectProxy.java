package cqu.lidong.spring.aop.proxy;

import java.lang.reflect.Method;

public class AspectProxy implements Proxy {

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        //开始增强
        try {
            if (intercept(method, params)) {
                before(method, params);
                result = proxyChain.doProxyChain();
                after(method, params);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            error(method, params, e);
            throw e;
        } finally {
            //模拟 返回切面 的增强
            returnAdvice();
        }

        return result;
    }

    /**
     * 切入点判断
     */
    public boolean intercept(Method method, Object[] params) {
        return true;
    }

    /**
     * 前置增强   还有一个around切面
     */
    public void before(Method method, Object[] params) {
    }

    /**
     * 后置增强
     */
    public void after(Method method, Object[] params) {
    }

    /**
     * 异常增强
     */
    public void error(Method method, Object[] params, Throwable e) {
    }

    /**
     * 返回增强 return
     */
    public void returnAdvice() {
    }

}
