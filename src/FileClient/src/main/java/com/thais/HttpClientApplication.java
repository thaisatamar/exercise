package com.thais;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class HttpClientApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(HttpClientApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}