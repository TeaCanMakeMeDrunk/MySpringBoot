package cqu.lidong.spring.ioc.bean.util;

/**
 * BeanPostProcessor接口 允许用户在Bean初始化（第4步）的前后 实现自己的逻辑 比如初始化之前实现xx逻辑 初始化之后实现xx逻辑
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object bean, String beanName);
}