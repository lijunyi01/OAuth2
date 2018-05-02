package com.allcom.conf;

import com.allcom.customerize.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by ljy on 2018/5/1.
 * ok
 */
@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.BASIC_AUTH_ORDER)
@Order(-10000)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder encoder;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
//    public WebSecurityConfig(PasswordEncoder passwordEncoder) {
    public WebSecurityConfig(PasswordEncoder passwordEncoder,CustomAuthenticationProvider customAuthenticationProvider) {
        this.encoder = passwordEncoder;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.inMemoryAuthentication()
//                .withUser("user").password(encoder.encode("123")).roles("USER")
//                .and()
//                .withUser("admin").password(encoder.encode("123")).roles("ADMIN");
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.authorizeRequests()
//                .antMatchers("/oauth/authorize").authenticated()
//                .and()
//                .httpBasic().realmName("OAuth Server");
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/oauth/index").permitAll()
                .antMatchers("/oauth/login").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/oauth/check_token").permitAll()
                .antMatchers("/oauth/confirm_access").permitAll()
                .antMatchers("/oauth/error").permitAll()
                .antMatchers("/oauth/my_approval_page").permitAll()
                .antMatchers("/oauth/my_error_page").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/oauth/login")          //未登录时访问受保护资源都跳到这个url，get方法
                .loginProcessingUrl("/oauth/login.do")  //登录请求拦截的url,也就是form表单提交时指定的action，post方法
                .failureUrl("/oauth/login?err=1");         //登录失败跳转的url
    }
}
