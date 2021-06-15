package cqu.lidong.spring.ioc.bean.util;

public class BeanDefinition {
    //类名
    private Class beanClass;
    //单例还是原型模式
    private String scope;
    //是否为懒加载
    private boolean isLazy;

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
