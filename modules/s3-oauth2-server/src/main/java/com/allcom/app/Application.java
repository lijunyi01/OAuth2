package com.allcom.app;

import com.allcom.App;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by ljy on 2018/4/27.
 * ok
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = App.class)
public class Application implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>{

    @Value("${system.embeddedtomcatport}")
    private int tomcatport;

    @Override
    public void customize(ConfigurableServletWebServerFactory server) {
        server.setPort(tomcatport);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);

    }
}
