package com.zhy.test.service.Impl;

import com.zhy.test.entity.User;
import com.zhy.test.exception.securityException.MyUsernameNotFoundException;
import com.zhy.test.service.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 跟踪UserDetailsService,身份认证的调用流程
 *  AbstractAuthenticationProcessingFilter的doFilter方法，调用->
 *  UsernamePasswordAuthenticationFilter的attemptAuthenticatoin方法，调用->
 *  ProviderManager的authenticate方法，调用->
 *  AbstractUserDetailsAuthenticationProvider的authenticate方法，调用->
 *  当前对象的loadUserByUsername方法。
 *  通过以上过滤器
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 在用户登陆时，spring会调用这个方法去获得user的信息（密码等），以对比页面传过来的用户名和密码是否正确。
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user==null){
            throw new MyUsernameNotFoundException("用户名: "+username+" 不存在");
        }
        List<String> roleCodeList = userRepository.queryUserOwnedRoleCodes(username);

        List<GrantedAuthority> authorities =
                roleCodeList.stream().map(e -> new SimpleGrantedAuthority(e)).collect(Collectors.toList());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(),authorities
        );
        return userDetails;
    }
}
