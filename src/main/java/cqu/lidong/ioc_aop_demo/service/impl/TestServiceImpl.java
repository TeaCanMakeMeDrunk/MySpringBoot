package cqu.lidong.ioc_aop_demo.service.impl;


import cqu.lidong.ioc_aop_demo.entity.Test;
import cqu.lidong.ioc_aop_demo.service.TestService;
import cqu.lidong.spring.ioc.annotation.Autowired;
import cqu.lidong.spring.ioc.annotation.Component;
import cqu.lidong.spring.ioc.annotation.Lazy;
import cqu.lidong.spring.ioc.annotation.Scope;
import cqu.lidong.spring.ioc.bean.util.BeanNameAware;
import cqu.lidong.spring.ioc.bean.util.InitializingBean;

@Component("testService")
//下面两个注解属于BeanDefinition 定义的信息 懒加载？单例？原型？
//@Lazy
@Scope //默认单例
//@Scope("prototype")
public class TestServiceImpl implements TestService, BeanNameAware, InitializingBean {

    @Autowired
    private Test test;

    private String beanName; //想把这个复制为 bean的名字 testService（默认的，可以Component设置）, 实现BeanNameAware接口。*Aware获得Spring容器里的一些东西

    private String init;

    @Override
    public void setBeanName(String beanName) {
        //写自己的逻辑，Spring只负责执行
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {
        //初始化逻辑，写自己的逻辑，Spring只负责执行
        this.init = "bean初始化逻辑 在属性填充之后";
    }

    public void getInfo(){
        System.out.println("============ 开始正常方法的调用 ============");
        System.out.println("开始测试依赖注入");
        System.out.println("Autowired_test_success:" + test.getInfo());
        System.out.println("结束测试依赖注入");
        System.out.println("beanName:" + beanName);
        System.out.println("init_test:" + init);
        System.out.println("============ 结束正常方法的调用 ============");
    };


}
