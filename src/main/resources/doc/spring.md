#####注解
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
    @EnableTransactionManageMent(proxy-target-class=true)
        表示开启事务支持，在spring boot项目中一般配置在启动类上。proxy-target-class属性值决定是基于接口还是基于类的代理被创建。默认是false。
    注：
    当子类加载到spring容器中，父类会自动加载到容器中，不需要配置注解。
#####spring中的Full模式和Lite模式
    @Configuration(proxyBeanMethods = false)
        ProxyBeanMethods=true或不写，是Full模式，=false是Lite模式。Full模式和Lite模式均是针对spring配置类而言的。
    Lite模式：当@Bean方法没有使用@Configuration注释的类中声明时，被称为在Lite模式下处理。包括在@Component中声明的@Bean方法，甚至只是在一个
        普通的类中声明的Bean方法，都被认为是Lite版的配置类。和Full模式的@Configuration不同，Lite模式的@Bean方法不能声明Bean之间的依赖关系，因此
        这样的@Bean方法不应该调用其他@Bean方法。每个这样的方法实际上只是一个特定Bean引用的工厂方法（factory-method），没有任何特殊的运行时语义。
    何时为Lite模式：
       没有标注@Configuration的类里有@Bean方法称为Lite模式，如类上有@Component、@ComponentScan、@Import、@ImportResource注解。Spring5.2之后  
       标注有@Configuration(proxyBeanMethods = false),对应spring boot2.2.0，内置所有@Configuration配置类都改为了
       @Configuration(proxyBeanMethods=false),目的：降低启动时间，为Cloud Native继续做准备。
    优点：运行时不需要给对应类生成CGLIB子类，提高运行性能，降低启动时间。可以该配置类当作一个普通类使用，即@Bean方法可以是private、可以是final
    缺点：不能声明@Bean之间的依赖，即不能通过方法调用来依赖其他的bean.
    
    Full模式：标注有@Configuration注解或@Configuration(proxyBeanMethods=true)的类被称为full模式的配置类，
    优点：支持常规Java调用相同类的@Bean方法而保证是容器内的bean,有效规避了在Lite模式下操作难以跟踪的细微错误。
    缺点：运行时会给该类生成一个CGLIB子类放进容器，有一定的性能、时间开销（这个开销在spring boot这种有大量配置类的情况下是不容忽视的，这也是
    Spring5.2新增了proxyBeanMethods属性的直接原因。正因为被代理了所以@Bean方法不可以是private，final.）
    该模式总结：
        该模式下，配置类会被CGIB增强（生成代理对象），放进IOC容器内的是代理对象
        该模式下，对内部类是没有限制的，可以是full模式也可以是Lite模式
        该模式下，配置类内部可以通过方法调用来处理依赖，并且保证是同一个实例，都指向IOC中的哪个单例
        该模式下，@Bean方法不能被Private、final修饰，（因为方法需要被复写）
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
#####spring aop的实现原理
    SpringAop的实现是基于代理模式。SpringAop会在CGLIB/JDK动态代理之间进行切换。
    静态代理：AspectJ(编译时增强)使用的是静态代理，指：AOP框架在编译阶段生成AOP代理类，它会在编译阶段将AspectJ植入到Java字节码中，运行时是增强后的
        AOP对象。
    动态代理：SpringAop使用 的是动态代理，指：Aop框架不会去修改字节码，而是每次运行时在内存中临时为方法生成一个AOP对象，这个AOP对象包含了目标对象的
        全部方法，并且在特定的切点做了增强处理，并回调原对象方法。
#####springmvc和struts2区别
    1.应用方便上
    2.加构方面：每一个Action对应一个Request，是类从面的拦截器；而springMvc是一个方法对应一个request,是方法从面的Servlet；
#####@Transactional无效问题（可参考com\zhy\service\Impl\ToTrasactionalImpl.java 或 com\zhy\kafka\spring\KafkaProducer.java）
    开启事务，实际上应用的是springAop的动态代理，在内部方法内调用其他事务方法，说明调用的事务方法是目标对象的方法，不是代理目标的方法。
#####springboot开启事务管理的相关配置
    Spring的事务处理中，通用的事务处理流程是由抽象事务管理器AbstractPlatformTransactionManager来提供的，而具体的底层事务处理实现，
    由PlatformTransactionManager的具体实现类来实现，如 DataSourceTransactionManager 、JtaTransactionManager和 HibernateTransactionManager等。   
    使用mybatis作为持久层框架，spring boot 会自动配置一个 DataSourceTransactionManager，我们只需在方法（或者类）加上 @Transactional 注解，就
    自动纳入 Spring 的事务管理了。