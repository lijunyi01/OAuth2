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
@SessionAttributes({ "authorizationRequest" })
public class OAuthController {

    @RequestMapping({ "/oauth/my_approval_page" })
    public String getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request.getAttribute("scopes"));
        List<String> scopeList = new ArrayList<String>();
        for (String scope : scopes.keySet()) {
            scopeList.add(scope);
        }
        model.put("scopeList", scopeList);
        return "oauth_approval";
    }

    @RequestMapping({ "/oauth/my_error_page" })
    public String handleError(Map<String, Object> model, HttpServletRequest request) {
        Object error = request.getAttribute("error");
        String errorSummary;
        if (error instanceof OAuth2Exception) {
            OAuth2Exception oauthError = (OAuth2Exception) error;
            errorSummary = HtmlUtils.htmlEscape(oauthError.getSummary());
        } else {
            errorSummary = "Unknown error";
        }
        model.put("errorSummary", errorSummary);
        return "oauth_error";
    }

//    @RequestMapping({ "/oauth/login" })
//    public String login(String err, ModelMap modelMap) {
//        if (StringUtils.hasLength(err)) {
//            modelMap.put("err", err);
//        }
//        return "login";
//    }
}
