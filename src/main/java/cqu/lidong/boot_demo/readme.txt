这个Demo是实现SpringBoot零配置的（结合SpringMVC）

注意：IoC容器使用内置的，DispatcherServlet直接从容器取

StartApplication里的下面3句是初始化IoC容器的:
AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
ac.register(SpringBootScanAddressConfig.class);
ac.refresh();