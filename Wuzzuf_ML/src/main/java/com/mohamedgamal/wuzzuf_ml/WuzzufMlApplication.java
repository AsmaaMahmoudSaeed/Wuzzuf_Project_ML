package com.mohamedgamal.wuzzuf_ml;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WuzzufMlApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(WuzzufMlApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }
}