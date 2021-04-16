spring security过滤器

    1.WebAsyncManagerIntegrationFilter:（异步方式）提供了对securityContext和WebAsyncManager的集成。方式是通过
      SecurityContextCallableProcessingInterceptor的beforeConcurrentHanding(NativeWebRequest,Callable)方法来将SecurityContext设置到Callable
      上。其实就是把SecurityContext设置到异步线程中，使其也能获取到用户上下文认证信息。
    2.SecurityContextPersistenceFilter:(同步方式)在请求前从SecurityContextRepository(默认实现是HttpSesseionSecurityContextRepository)获取信息
      并填充SecurityContextHolder(如果没有，则创建一个新的ThreadLocal的SecurityContext)，并在请求完成并清空SecurityContextHolder并更新
      SecurityContextRepository.
    3.HeaderWriterFilter:用来给http响应添加一些Header，比如X-Frame-Options,X-SS-Protection*,X-Content-Type-Options.
    4.CsrfFilter:默认开启，用于防止csrf攻击的过滤器
    5.LogoutFilter:处理注销的过滤器。
    6.UsernamePasswordAuthenticationFilter:表单提交了username和password,被封装成UsernamePasswordAuthenticationToken对象进行一系列的认证，便是
      通过这个过滤器完成的，即调用AuthenticationManager.authenticate()。在表单认证的方法中，这是最关键的过滤器。具体过程是：
      （1）调用AbstractAuthenticationProcessingFilter.doFilter()方法执行过滤器。
      （2）调用UsernamePasswordAuthenticationFilter.attemptAuthentication()方法。
      （3）调用AuthenticationManager.authenticate()方法（实际上委托给AuthenticationPrivider的实现类来处理）
    7.DefaultLoginPageGeneratingFilter & DefaultLogoutPageGeneratingFilter:如果没有配置/login及login page,系统则会自动配置这两个Filter.
    8.BasicAuthenticationFilter:处理HTTP请求的基本授权头，将结果放入SecurityContextHolder。
    9.RequestCacheAwareFilter:内部维护了一个RequestCache,用于缓存request请求
    10.SecurityContextHolderAwareRequestFilter:此过滤器对ServletRequest进行了一次封装，使request具有更加丰富的API
    11.AnonymousAuthenticationFilter:匿名身份过滤器，spring security为了兼容未登录的访问，走了一套认证流程，只是一个匿名的身份。它位于身份认证
       过滤器（e.g.UsernamePasswordAuthenticationFilter）之后，意味着只有在上述身份过滤器执行完毕后，SecurityContext依旧没有用户信息，
       AnonymousAuthenticationFilter该过滤器才有意义。
    12.SessionManagementFilter:和session相关的过滤器，内部维护了一个SessionAuthenticationStrategy来执行任何与session相关的活动。
    13.ExceptionTranslationFilter:异常转换过滤器，该过滤器本身不处理异常，而是将认证过程出现的异常（AccessDeniedException and 
       AuthenticationException)交给内部维护的一些类去处理。它位于springSecurityFilterChain的后方，用来转换中各链路中出现的异常，将其转换。
       它将Java中的异常和HTTP的响应连接在了一起，这样处理异常时，不用考虑密码错误跳到什么页面，账号锁定该如何，只需要关注自己的业务逻辑，抛出异常即可
       如果该过滤器检测到AuthenticationException,则将会交给内部的AuthenticationEntryPoint处理，如果检查到AccessDeniedException,首先判断用户是
       不是匿名用户，如果是匿名访问，则同上运行AuthenticationEntryPoint,否则委托给AccessDeniedHandler去处理，而AccessDeniedHandler的默认实现是
       AccessDeniedHandlerImpl.
    14.FilterSecurityInterceptor:这个过滤器决定了访问特定路径应该具备的权限，这些受限的资源访问需要什么权限和角色，这些判断和处理都由该类执行的。
       （1）调用FilterSecurityInterceptor.invoke()方法执行过滤器
       （2）调用AbstractSecurityInterceptor.beforeInvocation()方法
       （3）调用AccessDecisionManager.decide()方法决策判断是否有该权限。