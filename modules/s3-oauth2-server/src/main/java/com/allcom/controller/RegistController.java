package com.allcom.controller;

import com.allcom.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ljy on 2018/5/2.
 * ok
 */
@RestController
public class RegistController {

    final AccountService accountService;

    @Autowired
    public RegistController(AccountService accountService) {
        this.accountService = accountService;
    }

    //http://localhost:8080/oauth/regist?userName=ljy&password=123321
    @GetMapping("/oauth/regist")
    public String regist(String userName, String password) {

        if(accountService.regist(userName,password)){
            return "regist ok";
        }else {
            return "regist failed";
        }

    }
}
