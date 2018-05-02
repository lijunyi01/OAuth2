package com.allcom.customerize;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by ljy on 2018/5/2.
 * ok
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    //高版本编译器下，实现接口也可以用@Override
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (accessToken instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken token = ((DefaultOAuth2AccessToken) accessToken);
            token.setValue(getNewToken());

            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken instanceof DefaultOAuth2RefreshToken) {
                token.setRefreshToken(new DefaultOAuth2RefreshToken(getNewToken()));
            }

            Map<String, Object> additionalInformation = new HashMap<>();
            additionalInformation.put("client_id", authentication.getOAuth2Request().getClientId());
            token.setAdditionalInformation(additionalInformation);

            return token;
        }
        return accessToken;
    }

    private String getNewToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
