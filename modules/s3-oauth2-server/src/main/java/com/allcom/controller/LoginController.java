package com.allcom.controller;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 2018/5/2.
 * ok
 */
@Controller
public class LoginController {

    @RequestMapping("/oauth/login")
    public String login(String err, ModelMap modelMap) {
        if (StringUtils.hasLength(err)) {
            modelMap.put("err", err);
        }
        return "login";
    }
}
