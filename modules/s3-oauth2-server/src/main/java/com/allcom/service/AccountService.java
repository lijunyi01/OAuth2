package com.allcom.service;

import com.allcom.dao.MysqlDao;
import com.allcom.entity.Account;
import com.allcom.toolkit.GlobalTools;
import com.allcom.toolkitexception.ToolLibException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 2018/5/2.
 * ok
 */
@Service
public class AccountService {

    private static Logger log = LoggerFactory.getLogger(AccountService.class);

    final MysqlDao mysqlDao;

    @Autowired
    public AccountService(MysqlDao mysqlDao) {
        this.mysqlDao = mysqlDao;
    }

    public Account authUser(String userName, String password) {
        Account u = new Account();
        List<Map<String,Object>> mapList = mysqlDao.getAccountInfo(userName);

        if(mapList.size()==1){
            Map<String,Object> map = mapList.get(0);
            if(map.get("user_name")!=null){
                u.setUserName(map.get("user_name").toString());
            }
            if(map.get("password")!=null){
                u.setPassword(map.get("password").toString());
            }
            if(map.get("role_string")!=null){
                u.setRoleString(map.get("role_string").toString());
            }
            if(map.get("email")!=null){
                u.setEmail(map.get("email").toString());
            }
            if(map.get("id")!=null){
                try {
                    int id = GlobalTools.convertStringToInt(map.get("id").toString());
                    u.setId(id);
                } catch (ToolLibException e) {
                    log.error("ToolLibException:{}",e.toString());
                }
            }
        }

//        if (u == null) {
        if(u.getId() == -1) {
            return null;
        }

        if (!u.getPassword().equals(password)) {
            return null;
        }
        return u;
    }
}
