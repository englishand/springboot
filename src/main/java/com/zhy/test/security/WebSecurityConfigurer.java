package com.zhy.test.security;

import com.zhy.test.authenticationHandler.AuthenticationDeniedHandler;
import com.zhy.test.authenticationHandler.AuthenticationLogoutHandler;
import com.zhy.test.intercepor.security.MyAccessDecisionManager;
import com.zhy.test.intercepor.security.MyFilterInvocationSecurityMetadataSource;
import com.zhy.test.service.DatabaseUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
//    @Autowired
//    @Qualifier("authenticationEntryPointImpl")
//    private AuthenticationEntryPoint entryPoint;
    @Autowired
    private MyFilterInvocationSecurityMetadataSource filterMetadataSource; //权限过滤器（当前url所需要的访问权限）
    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;//权限决策器
    @Autowired
    private AuthenticationLogoutHandler authenticationLogoutHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(filterMetadataSource);
                        o.setAccessDecisionManager(myAccessDecisionManager);
                        return o;
                    }
                })
                .and()
                .formLogin().loginProcessingUrl("/login/loginIn")
                .usernameParameter("username").passwordParameter("password")
                .failureHandler(failureHandler)
                .successHandler(successHandler)
//                .successForwardUrl("/login/loginIn")
                .permitAll()
                .and()
                .logout().logoutUrl("/login/loginOut")
//                .logoutSuccessHandler(authenticationLogoutHandler)
                .permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler);
//                .authenticationEntryPoint(entryPoint);

    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
