package cqu.lidong.spring.ioc;

import cqu.lidong.spring.ioc.annotation.*;
import cqu.lidong.spring.ioc.bean.util.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ApplicationContext 类似 BeanFactory
 */
public class MyApplicationContext {

    //存放扫描到的类（被app加载器加载后）
    private List<Class> res = new ArrayList<>();

    //负责保存认为类定义的信息 懒加载？单例？原型？　类上的一些注解信息
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    //单例池
    private final Map<String, Object> singletonPoolMap = new ConcurrentHashMap<>();

    //存放BeanPostProcessor
    List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();


    /**
     * 初始化IoC容器
     *
     * @param configClass  配置的扫描路径
     */
    public MyApplicationContext(Class configClass) {
        //初始化IoC容器步骤
        // 1.扫描，得到Class, 进行类加载
        List<Class> classList = scanClass(configClass);
        // 2.BeanDefinition 负责保存人为定义的类信息 类 懒加载？单例？原型？　类上的一些注解信息
        setBeanDefinition(classList);
        // 3.基于Class单例实例化（反射），创建Bean（仅限饿汉式的单例模式）
        instanceSingletonBean();
    }

    /**
     * 实例化单例Bean
     */
    private void instanceSingletonBean() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            // 如果是单例模式 并且 不是懒加载
            if (beanDefinition.getScope().equals("singleton") && !beanDefinition.isLazy()) {
                if(!singletonPoolMap.containsKey(beanName)){ //单例池没有才创建Bean
                    //创建Bean
                    Object bean = doCreateBean(beanName, beanDefinition);
                    singletonPoolMap.put(beanName, bean);
                }
            }
        }
    }

    /**
     * 创建Bean的过程(Bean生命周期)
     * BeanPostProcessor接口 允许用户在Bean初始化（第4步）的前后 实现自己的逻辑 比如初始化之前实现xx逻辑 初始化之后实现xx逻辑
     */
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition)  {
        System.out.println("======创建Bean_" + beanName +"======");
        try {
            // 1.实例化
            System.out.println("创建Bean_步骤1：实例化");
            Class beanClass = beanDefinition.getBeanClass();
            Constructor declaredConstructor = beanClass.getDeclaredConstructor();
            Object bean = declaredConstructor.newInstance();//创建对象

            // 2.beanClass里面的属性填充(依赖注入)，实现注解Autowired的功能
            System.out.println("创建Bean_步骤2：属性填充（依赖注入）");
            Field[] declaredFields = beanClass.getDeclaredFields();//获得类的属性  比如TestService获得“Test test”这一属性
            for (Field field: declaredFields) {
                if(field.isAnnotationPresent(Autowired.class)){
                    //Autowired根据名字找Bean，Resource根据类名找Bean
                    Object autowiredBean = getBean(field.getName());
                    field.setAccessible(true); //私有变量private 可访问
                    field.set(bean, autowiredBean); //将bean对象中 属性"field ==“Test test” " 设置为 autowiredBean
                }

            }

            //3. Aware回调，实现Aware接口方法，如果有(这里只模拟一种)
            System.out.println("创建Bean_步骤3：实现Aware接口方法, 如果有的话");
            if(bean instanceof BeanNameAware){
                //方法里面写自己的逻辑，Spring只负责执行
                ((BeanNameAware) bean).setBeanName(beanName);
            }

            if(bean instanceof MyApplicationContextAware){
                //方法里面写自己的逻辑，Spring只负责执行
                ((MyApplicationContextAware) bean).setMyApplicationContextAware(this);
            }

            //BeanPostProcessor 允许用户在创建Bean步骤的前后 实现自己的逻辑 比如初始化之前实现xx逻辑 初始化之后实现xx逻辑
            //初始化之前  xxx
            System.out.println("创建Bean_步骤4：初始化之前, postProcessBeforeInitialization");
            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList){
                bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            }

            //4. 初始化 InitializingBean接口
            System.out.println("创建Bean_步骤4：初始化, postProcessBeforeInitialization");
            if(bean instanceof InitializingBean){
                //方法里面写自己的逻辑，Spring只负责执行
                ((InitializingBean) bean).afterPropertiesSet();
            }

            //初始化之后  xxx  AoP在这里实现的
            System.out.println("创建Bean_步骤4：初始化之后, postProcessBeforeInitialization");
            for(BeanPostProcessor beanPostProcessor : beanPostProcessorList){
                bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            }

            System.out.println("======创建Bean_" + beanName +"完成======\n");
            return bean;
        } catch (NoSuchMethodException |InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据扫描到的需要放入容器的类集合，设置类对应的BeanDefinition
     * 用户可以根据beanName获取对应的BeanDefinition
     *
     * @param classList
     */
    private void setBeanDefinition(List<Class> classList) {
        for (Class clazz : classList) {
            BeanDefinition beanDefinition = new BeanDefinition();
            //保存类
            beanDefinition.setBeanClass(clazz);

            if (clazz.isAnnotationPresent(Scope.class)) {
                Scope scope = (Scope) clazz.getAnnotation(Scope.class);
                beanDefinition.setScope(scope.value());
            } else { //默认是单例
                beanDefinition.setScope("singleton");
            }

            if (clazz.isAnnotationPresent(Lazy.class)) {
                beanDefinition.setLazy(true);
            } else {
                beanDefinition.setLazy(false);
            }

            //获得beanName，没有定义就默认为类的第一个字母小写
            String beanName = "";
            Component component = (Component) clazz.getAnnotation(Component.class);
            if (!component.value().equals("")) {
                beanName = component.value();
            } else {
                String[] splitStr = clazz.toString().split("\\.");
                String first = splitStr[splitStr.length - 1].substring(0, 1);
                String second = splitStr[splitStr.length - 1].substring(1);
                beanName = first.toLowerCase() + second;
            }
//            System.out.println("beanName:" + beanName);
            beanDefinitionMap.put(beanName, beanDefinition);
        }
    }

    /**
     * 获得扫描注解，根据扫描路径得到路径下Class集合
     *
     * @param configClass
     * @return
     */
    private List<Class> scanClass(Class configClass) {
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String scanPath = componentScanAnnotation.value();
//        System.out.println(scanPath); //这个是包路径 cqu.lidong.demo.service 扫描的应该是class文件 target目录下
        scanPath = scanPath.replace(".", "/");

        //获得类加载器 app加载（负责classpath下的， 其实文件目录就是tartget\classes\下）
        ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
        URL url = classLoader.getResource(scanPath);
        File file = new File(url.getFile());
        classLoaderAccordingFile(file);
        return res;
    }

    /**
     * 递归所有文件,如果是.class文件（类），则进行类加载
     * @param file
     */
    private void classLoaderAccordingFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
//                System.out.println(f.getAbsolutePath()); //F:\MySpringBoot\target\classes\cqu\lidong\demo\service\TestService.class
                //loadClass 需要传入 String path = "cqu.lidong.demo.service.TestService"
                if(f.isDirectory()){
                    //递归所有目录，找.class文件
                    classLoaderAccordingFile(f);
                }else{ //必定是.class文件的，进行类加载
                    classLoaderAccordingFile(MyApplicationContext.class.getClassLoader(), f);
                }
            }
        }
    }

    /**
     * 重载classLoaderAccordingFile方法，进行类加载
     * 如果类实现了BeanPostProcessor，记录下来
     * @param classLoader
     * @param f
     */
    private void classLoaderAccordingFile(ClassLoader classLoader, File f) {
        String absolutePath = f.getAbsolutePath();
        String path = absolutePath.substring(absolutePath.indexOf("cqu"), absolutePath.indexOf(".class"));
        path = path.replace("\\", ".");
        try {
            //要先进行类加载，后面才能进行实例化
            Class<?> clazz = classLoader.loadClass(path);
            // 过滤，如果这些类没有写相应的注解 就没必要放入Spring容器
            if (clazz.isAnnotationPresent(Component.class)) {
                res.add(clazz);

                //如果clazz实现了BeanPostProcessor接口, 就创建对象存beanPostProcessorList里面
                //记录一下，直到哪些类实现了BeanPostProcessor接口
                if(BeanPostProcessor.class.isAssignableFrom(clazz)){
                    BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                    beanPostProcessorList.add(beanPostProcessor);
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从容器中获得Bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        // bean是单例的 还是 原型的？　BeanDefinition获得
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition.getScope().equals("singleton")){
            if (!beanDefinition.isLazy()) {
                //如果不是懒加载（那么就是饿汉式），从单例池拿Bean
                System.out.println("对象名:" + beanName + ", 饿汉式单例, 直接单例池拿数据");
            }else{ //（懒汉式，拿的时候才实例化）
                System.out.println("对象名:" + beanName + ", 懒汉式单例, 拿的时候才实例化, 创建Bean");
                if(!singletonPoolMap.containsKey(beanName)){ //单例池没有才创建Bean
                    //创建Bean
                    Object bean = doCreateBean(beanName, beanDefinition);
                    singletonPoolMap.put(beanName, bean);
                }
            }
            return singletonPoolMap.get(beanName);
        }else if(beanDefinition.getScope().equals("prototype")){
            System.out.println("原型模式，使用原型实例指定创建对象的种类,并且通过拷贝原型对象创建新的对象");
            //创建Bean
            return doCreateBean(beanName, beanDefinition);
        }
        return null;
    }
}
