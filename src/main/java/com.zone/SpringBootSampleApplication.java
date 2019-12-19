package com.zone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
@ServletComponentScan
public class SpringBootSampleApplication {
    public static void main(String[] args){
        SpringApplication.run(SpringBootSampleApplication.class,args);
    }
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
