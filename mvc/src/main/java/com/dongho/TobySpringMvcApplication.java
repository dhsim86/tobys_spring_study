package com.dongho;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TobySpringMvcApplication {

    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(TobySpringMvcApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

}
