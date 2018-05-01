package com.allcom.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by ljy on 2018/4/27.
 * ok
 */
public class Config {

    public static final String OAUTH_CLIENT_ID = "oauth_client";
    public static final String OAUTH_CLIENT_SECRET = "oauth_client_secret";
    public static final String RESOURCE_ID = "my_resource_id";
    public static final String[] SCOPES = { "read", "write" };

    @Configuration
    @EnableAuthorizationServer
    static class OAuthAuthorizationConfig extends AuthorizationServerConfigurerAdapter {
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            PasswordEncoder encoder = passwordEncoder();
            clients.inMemory()
                    .withClient(OAUTH_CLIENT_ID)
                    .secret(encoder.encode(OAUTH_CLIENT_SECRET))
                    .resourceIds(RESOURCE_ID)
                    .scopes(SCOPES)
                    .authorities("ROLE_USER")
                    .authorizedGrantTypes("authorization_code", "refresh_token","implicit")
                    //以下行会限制客户端填的redirecturi只能是特定的内容；有以下内容可以加强安全
//                    .redirectUris("http://default-oauth-callback.com")
                    .accessTokenValiditySeconds(60*30) // 30min
                    .refreshTokenValiditySeconds(60*60*24); // 24h
        }

        //【/oauth/token】默认采用的是HTTP Basic Auth（org.springframework.security.web.authentication.www.BasicAuthenticationFilter），所以需要在HTTP的header里提供clientId和clientSecret的Base64值。(base64(clientId:clientSecret))
        // 通过以下设置，可以通过参数的形式传递clientId和clientSecret的值。
        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.allowFormAuthenticationForClients();
        }

        // 装载BCrypt密码编码器
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Configuration
    @EnableResourceServer
    static class OAuthResourceConfig extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(RESOURCE_ID);
        }
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/api/**").access("#oauth2.hasScope('read')")
                    .antMatchers(HttpMethod.POST, "/api/**").access("#oauth2.hasScope('write')");
        }
    }


    @Configuration
    @EnableWebSecurity
    static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            PasswordEncoder encoder = passwordEncoder();
            auth.inMemoryAuthentication()
                    .withUser("user").password(encoder.encode("123")).roles("USER")
                    .and()
                    .withUser("admin").password(encoder.encode("123")).roles("ADMIN")
                    .and()
                    .passwordEncoder(encoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.authorizeRequests()
                    .antMatchers("/oauth/authorize").authenticated()
                    .and()
                    .httpBasic().realmName("OAuth Server");
        }

        // 装载BCrypt密码编码器
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}
