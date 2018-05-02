package com.allcom.customerize;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;

import javax.sql.DataSource;

/**
 * Created by ljy on 2018/5/2.
 * ok
 */
public class CustomJdbcAuthorizationCodeServices extends JdbcAuthorizationCodeServices {
    private RandomValueStringGenerator generator;

    public CustomJdbcAuthorizationCodeServices(DataSource dataSource,int codeLength) {
        super(dataSource);
        this.generator = new RandomValueStringGenerator(codeLength);
    }

    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = this.generator.generate();
        store(code, authentication);
        return code;
    }
}
