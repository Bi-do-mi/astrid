package com.bidomi.astrid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootExceptionReporter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class AstridApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AstridApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(AstridApplication.class, args);
        System.out.println("app started");
        System.out.println("Spring version: " + SpringVersion.getVersion());
    }
}
