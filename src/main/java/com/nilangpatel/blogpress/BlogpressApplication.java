package com.nilangpatel.blogpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class BlogpressApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogpressApplication.class, args);
    }

}
