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