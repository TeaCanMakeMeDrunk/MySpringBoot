package cqu.lidong.boot_demo.webServer.impl;

import cqu.lidong.boot_demo.webServer.WebServer;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class MyTomcat implements WebServer {
    @Override
    public void getServer() throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        //完成监听，告诉tomcat加载哪个项目 类似webapp目录，现在没有页面，所以可以随便指定
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File("src/main/resources/webapp").getAbsolutePath());
        ctx.setReloadable(false);
        WebResourceRoot resources = new StandardRoot(ctx);

        File additionWebInfClasses = new File("target/classes");
        //告诉Tomcat项目的class目录，根目录
        resources.addPreResources(
                new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));

        //先启动tomcat
        tomcat.start();
        //再设置守护线程,服务其他线程
        tomcat.getServer().await();
    }
}
