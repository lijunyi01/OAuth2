package com.allcom.conf;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.io.File;

//import com.allhis.filter.SSLContextGenerator;

//import org.slf4j.Logger;

//import com.allcom.handler.MinaClientHandler;

//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.xbill.DNS.Resolver;
//import org.xbill.DNS.SimpleResolver;

/**
 * Created by ljy on 15/10/16.
 * ok
 */
@Configuration
public class ApplicationConfig {

    @Value("${dataSource.idbuser}")
    private String idbUsername;
    @Value("${dataSource.driver}")
    private String jdbcDriver;
    @Value("${dataSource.idbpass}")
    private String idbPassword;
    @Value("${dataSource.idburl}")
    private String idbUrl;

    //在标注了@Configuration的java类中，通过在类方法标注@Bean定义一个Bean。方法必须提供Bean的实例化逻辑。
    //通过@Bean的name属性可以定义Bean的名称，未指定时默认名称为方法名。
    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        //ClassPathResource 的根目录在本项目是指resources目录
        //ppc.setLocation(new ClassPathResource("/test.properties"));
        ppc.setLocation(new FileSystemResource("/appconf/s2-oauth2-server-db/app.properties"));
        return ppc;
    }

    @Bean
    public static JoranConfigurator readLogbackPropertyFile(){
        File logbackFile = new File("/appconf/s2-oauth2-server-db/logback.xml");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            configurator.doConfigure(logbackFile);
        }
        catch (JoranException e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
        return configurator;

    }

    @Bean
    @Qualifier("jdbctemplate1")
    JdbcTemplate jdbcTemplate(@Qualifier("db1")DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Qualifier("db1")
//    @Scope("prototype")
    DataSource dataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(idbUsername);
        hikariDataSource.setDriverClassName(jdbcDriver);
        hikariDataSource.setPassword(idbPassword);
        hikariDataSource.setJdbcUrl(idbUrl);
        hikariDataSource.setMaximumPoolSize(3);
        hikariDataSource.setConnectionTestQuery("select count(*) from oauth_access_token");
        return hikariDataSource;
    }

    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SslFilter sslFilter(){
//        SslFilter sslFilter = new SslFilter(new SSLContextGenerator().getSslContext());
//        return sslFilter;
//    }

//    @Bean
//    RestTemplate restTemplate(){
//        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpComponentsClientHttpRequestFactory.setReadTimeout(10*1000);
//        httpComponentsClientHttpRequestFactory.setConnectTimeout(5*1000);
//        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
//        return restTemplate;
//    }
//
//    @Bean
//    ObjectMapper objectMapper(){
//        return new ObjectMapper();
//    }

}
