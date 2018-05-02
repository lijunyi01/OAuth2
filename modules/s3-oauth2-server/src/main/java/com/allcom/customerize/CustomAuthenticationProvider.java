package com.allcom.customerize;

import com.allcom.entity.Account;
import com.allcom.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ljy on 2018/5/2.
 * ok
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AccountService accountService;

    @Autowired
    public CustomAuthenticationProvider(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Account account = accountService.authUser(name, password);
        if (account == null) {
            throw new AuthenticationCredentialsNotFoundException("Account is not found.");
        }

        List<GrantedAuthority> grantedAuths = AuthorityUtils.createAuthorityList(account.getRoleString());
        return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
