package com.bidomi.astrid;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringVersion;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.SpringSecurityCoreVersion;

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
