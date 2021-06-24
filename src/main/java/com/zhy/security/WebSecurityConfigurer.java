package com.zhy.security;

import com.zhy.authenticationHandler.AuthenticationDeniedHandler;
import com.zhy.authenticationHandler.AuthenticationLogoutHandler;
import com.zhy.intercepor.security.MyAccessDecisionManager;
import com.zhy.intercepor.security.MyFilterInvocationSecurityMetadataSource;
import com.zhy.service.Impl.DatabaseUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private DatabaseUserDetailsService userDetailsService;
    @Autowired
    @Qualifier("authenticationSuccuessHandler")
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    @Qualifier("authenticationFailHandler")
    private AuthenticationFailureHandler failureHandler;
    @Autowired
    @Qualifier("authenticationDeniedHandler")
    private AuthenticationDeniedHandler deniedHandler;
    @Autowired
    @Qualifier("authenticationEntryPointImpl")
    private AuthenticationEntryPoint entryPoint;
    @Autowired
    private MyFilterInvocationSecurityMetadataSource filterMetadataSource; //权限过滤器（当前url所需要的访问权限）
    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;//权限决策器
    @Autowired
    private AuthenticationLogoutHandler authenticationLogoutHandler;

    /**
     * 核心过滤器配置：主要用来对静态资源的配置
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(getUrls());
    }

    /**
     * 安全过滤器链配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//配置路径拦截
                .anyRequest().authenticated()//任何请求没有匹配的都需要进行验证
                /**
                 * 权限处理配置
                 */
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(filterMetadataSource);
                        o.setAccessDecisionManager(myAccessDecisionManager);
                        return o;
                    }
                })
//                .and()
//                    .addFilter(new JwtAuthenticationFilter(authenticationManager()))
//                    .addFilter(new JwtAuthorizationFilter(authenticationManager()))
//                    //不需要session
//                    .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                    .loginPage("/login/in")//指定自定义登录页面，也可以用页面地址 /login.html
                     //当使用自定义的页面，登录action发生了改变，为此需要重新指定登录地址：用loginProcessingUrl("")
                    .loginProcessingUrl("/login/logIn")//设置登录时的请求地址
                    .usernameParameter("username").passwordParameter("password")
                    .failureHandler(failureHandler)
                    .failureUrl("/login/in?error")
                    .successHandler(successHandler)
                .and()
                .logout()
                    .logoutUrl("/login/loginOut")
                    .logoutSuccessHandler(authenticationLogoutHandler)//指定成功注销后处理类
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler)//验证权限不足
//                .authenticationEntryPoint(entryPoint)//用来解决匿名用户访问无权限资源时的异常
                .and()
                /**
                 * Spring Security3默认关闭csrf,Spring Security4默认启动了csrf,为了防止跨站提交攻击
                 * CSRF:跨站请求伪造,设置csrf().disable();
                 */
                .csrf().disable().httpBasic()
                ;
    }


    /**
     * 认证管理器配置：认证登录的用户信息
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    /**
     * 获取IndexController中的不需要拦截的请求
     * @return
     */
    private String[] getUrls() {
        List<String> urls = getUrlList();
        return urls.stream().toArray(String[]::new);
    }
    /**
     * 获取一些静态的不需要拦截的请求
     * @return
     */
    private List<String> getUrlList(){
        List<String> urls = new ArrayList<>();
        urls.add("/login/in");
        urls.add("/**/*.js");
        urls.add("/**/*.css");
        urls.add("/login/error");
        urls.add("/verify/**");
        urls.add("/qr/**");
        urls.add("/annotation/**");
        urls.add("/user/selectUser");
        return urls;
    }

    /**
     * 注解使用：@EnableWebSecurity
     *      1.组合注解，该注解中引入了WebSecurityConfiguration.class配置类，该配置类中注入了bean:springSecurityFilterChain,
     *          这是Spring Security的核心过滤器，这是请求的认证入口。
     *      2.使用了@EnableGlobalAuthentication注解，该注解引入了AuthenticationConfiguration配置类，该类是配置认证相关的核心类，
     *          主要作用:向spring容器中注入AuthenticationManagerBuilder（使用了创造者模式），它能创造AuthenticationManager(身份认证接口).
     *
     * @EnableGlobalMethodSecurity
     *  开启spring方法级安全。该注解提供：prePostEnabled、securedEnabled、jsr250Enabled三种不同机制来实现同一种功能。
     *  prePostEnabled=true:解锁 @PreAuthorize和 @PostAuthorize两个注解。
     *  secured:注解用来定义业务方法的安全配置。在需要安全【角色/权限等】的方法上指定。并且只有那些角色/权限的用户才可以调用该方法。
     *  jsr250e:JSR-250安全控制注解，这属于Java-EE级别的安全防范。
     */
}
