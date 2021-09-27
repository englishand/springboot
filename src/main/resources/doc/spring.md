@component和@bean的区别：
  
    1.@Component(和@Service和@Repository)都用于自动监测和使用类路径扫描自动配置bean.注释类和bean之间存在隐式的一对一映射关系（即每个类一个bean）。
      这种方法对需要逻辑处理的控制非常有限，因为它纯粹是声明式的。该注解表明一个类作会作为组件类，并告诉spring要为这个类创建bean.
    2.@Bean用于显示声明单个bean,而不是让spring自动执行它。它将bean的声明和类的定义分离，并允许您精确的创建和配置bean.
      @Bean经常和@Configuration注解搭配使用。@Bean注解的方法返回值是对象，可以在方法中为对象设置属性。通常方法体中包含了最终产生bean实例的逻辑。
      当引用第三方库中的类需要配到spring容器时，则只能通过@Bean来实现。
    3.两者目的都是一样的，都属注册bean到spring容器中。
@Valid
    
    作用：用于检查对象属性是否符合配置规定。通常和BindingResult(存放验证结果)和配置项(@NotEmpty、@Email等配置项)一起使用。
@RequestBody
    
    1.该注解用来处理content-type不是默认的application/x-www-form-urlcoded编码的内容，比如说：application/json或application/xml等。
@Configuration
    
    1.表明当前类是一个配置类，是方法bean的源
    2.将@Configuration配置的类的beanDefinitioin属性赋值为full类型的，保证当前类类型 可以转变为cglib类型
    3.将@Configuration配置的类由普通类型转变为cglib代理类型，后会生成cglib代理对象，通 过代理对象的方法拦截器,
    　　可以解决类内部方法bean之间发生依赖调用的时候从容器中去获取，避免了多例的出现
注：
    
    当子类加载到spring容器中，父类会自动加载到容器中，不需要配置注解。
#####spring boot自动装配原理（参考：https://blog.csdn.net/qq_36986015/article/details/107488437）
    @SpringBootApplication注解，该注解是复合注解，包括:1.@SpringBootConfiguration:该注解的底层是一个@Configuration注解，被@Configuration注解
    修饰的类是一个IOC容器，支持JavaConfig的方式进行配置；2.@ComponentScan:扫描注解，默认扫描当前类所在的包及其子包下包含的注解，将@Controller/
    @Service/@Component/@Repository等注解的类加载到ioc容器中3.@EnableAutoConfiguration:这个注解表明启动自动装配，里面包含连个比较重要的注解，
    @AutoConfigurationPackage和@Import。
    1.@AutoConfigurationPackage和@ComponentScan一样，将主配置类所在包及其子包里面的组件扫描到IOC容器中，但区别是：@AutoConfigurationPackage
    扫描@Entity、@MapperScan等第三方依赖的注解，@ComponentScan只扫描@Controller等常见的注解。
    2.@Import(AutoConfigurationImportSelector.class)是自动装配的核心注解。
    最终，@EnableAutoConfiguration注解通过@SpringBootApplication注解被间接的标记在了springboot的启动类上，SpringApplication.run方法的内部就会
    执行selectImports方法，进而找到所有JavaConfig配置类全限定名对应的class，然后将所有自动配置类加载到IOC容器中。
    那么这些类是如何获取默认属性值的呢？
    SprigBoot启动的时通过@EnableAutoConfiguration注解找到META-INF/spring.factories文件中的所有自动配置类。并对其加载，这些自动配置类都是以
    AutoConfiguration结尾命名的。它实际上就是一个JavaConfig形式的IOC容器配置类，通过以Properties结尾命名的类中取得在全局配置文件中配置的属性，如：
    server.port。
    以ServletWebServerFactoryAutoConfiguration为例，它是Servlet容器的自动配置类，在该类上开启了
    @EnableConfigurationProperties(ServerProperties.class)注解，最终找到了ServerProperties类。在全局配置的属性如：server.port等，通过
    @ConfigurationProperties注解，绑定到对应的xxxProperties配置实体类上封装为一个bean,然后再通过@EnableConfigurationProperties注解导入到Spring
    容器中。
#####springmvc和struts2区别
    1.应用方便上
    2.加构方面：每一个Action对应一个Request，是类从面的拦截器；而springMvc是一个方法对应一个request,是方法从面的Servlet；
#####spring介绍