package com.zhy.test.intercepor.security;

import com.zhy.test.entity.Menu;
import com.zhy.test.entity.Role;
import com.zhy.test.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuService menuService;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static final Logger log = LoggerFactory.getLogger(MyFilterInvocationSecurityMetadataSource.class);

    /**
     * 返回本次访问需要的权限，可以有多个权限
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        //去数据库查询资源
        List<Menu> allmenu = menuService.getAllMenu();
        for (Menu menu:allmenu){
            if (antPathMatcher.match(menu.getUrl(),requestUrl)
            && menu.getRoleList().size()>0){
                List<Role> roles = menu.getRoleList();
                int size = roles.size();
                String[] values = new String[size];
                for (int i=0;i<size;i++){
                    values[i] = roles.get(i).getRoleCode();
                }
                log.info("当前访问路径是{},这个url所需要的访问权限是{}", requestUrl, values);
                return SecurityConfig.createList(values);
            }
        }
        /**
         * @Description:如果本方法返回null的话，意味着当前这个请求不需要任何角色就能访问
         * 此处做逻辑控制，如果没有匹配上的，返回一个默认具体权限，防止漏缺资源配置
         */
        log.info("当前访问路径是{},这个url所需要的访问权限是{}", requestUrl, "ROLE_LOGIN");
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    /**
     *@Description: 此处方法如果做了实现，返回了定义的权限资源列表，
     * Spring Security会在启动时校验每个ConfigAttribute是否配置正确，
     * 如果不需要校验，这里实现方法，方法体直接返回null即可。
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * @Description: 方法返回类对象是否支持校验，
     * web项目一般使用FilterInvocation来判断，或者直接返回true
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
