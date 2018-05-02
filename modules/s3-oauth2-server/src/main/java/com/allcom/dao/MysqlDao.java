package com.allcom.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by ljy on 2018/5/2.
 * ok
 */
@Repository
public class MysqlDao {

    final JdbcTemplate jdbcTemplate;

    @Autowired
    public MysqlDao(@Qualifier("jdbctemplate1") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String,Object>> getAccountInfo(String userName){
        return jdbcTemplate.queryForList("select * from account where user_name=?",userName);
    }
}
