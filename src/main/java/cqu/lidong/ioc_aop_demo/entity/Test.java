package cqu.lidong.ioc_aop_demo.entity;

import cqu.lidong.spring.ioc.annotation.Component;

@Component
public class Test {
    public Test() {
    }

    public String getInfo() {
        return "test::getInfo()";
    }
}
