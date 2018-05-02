package com.allcom.conf;

import com.allcom.customerize.CustomJdbcAuthorizationCodeServices;
import com.allcom.customerize.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

//import org.springframework.core.env.Environment;

/**
 * Created by ljy on 2018/5/1.
 * ok
 */
@Configuration
@EnableAuthorizationServer
public class OAuthConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${system.codelength}")
    private int codeLength;

//    private final Environment env;
    private final DataSource dataSource;

    //自定义授权页面
    //private final AuthorizationEndpoint authorizationEndpoint;
    @Autowired
    AuthorizationEndpoint authorizationEndpoint;

    @Autowired
    public OAuthConfig(DataSource dataSource) {
//    public OAuthConfig(DataSource dataSource, AuthorizationEndpoint authorizationEndpoint) {
//        this.env = env;
        this.dataSource = dataSource;
//        this.authorizationEndpoint = authorizationEndpoint;
    }

    //Constructor  早于 @Autowired 早于 @PostConstruct；这里Constructor 和 @Autowired一体了，之后@PostConstruct的方法被自动调用
    //配合mvc （OAuthController类）自定义授权页面
    @PostConstruct
    public void init() {
        authorizationEndpoint.setUserApprovalPage("forward:/oauth/my_approval_page");
        authorizationEndpoint.setErrorPage("forward:/oauth/my_error_page");
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        //自定义生成授权码
        //默认规则是：6位随机英数字。
        //可以通过扩展AuthorizationCodeServices来覆写已有的生成规则。通过覆写createAuthorizationCode()方法可以设置成任意的生成规则。
        //return new JdbcAuthorizationCodeServices(dataSource);

        //这里实现返回32位随机英数字。
        return new CustomJdbcAuthorizationCodeServices(dataSource,codeLength);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    //自定义生成令牌
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    //【/oauth/token】默认采用的是HTTP Basic Auth（org.springframework.security.web.authentication.www.BasicAuthenticationFilter），所以需要在HTTP的header里提供clientId和clientSecret的Base64值。(base64(clientId:clientSecret))
    // 通过以下设置，可以通过参数的形式传递clientId和clientSecret的值。
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource); // oauth_client_details
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.approvalStore(approvalStore())                            // oauth_approvals
                .authorizationCodeServices(authorizationCodeServices())    // oauth_code
                .tokenStore(tokenStore());                                 // oauth_access_token & oauth_refresh_token
    }
}
