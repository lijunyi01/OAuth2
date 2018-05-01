package com.allcom.app;

import com.allcom.App;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;

/**
 * Created by ljy on 2018/4/27.
 * ok
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = App.class)
public class Application{

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Application.class);

    }
}
