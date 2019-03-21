package com.bidomi.astrid;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AstridApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AstridApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AstridApplication.class, args);
        System.out.println("app started");
//        System.out.println("Spring version: " + SpringVersion.getVersion()
//                + "Spring Security version" + SpringSecurityCoreVersion.getVersion());
    }
}
