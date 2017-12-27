package com.sagarandcompany.ServerSideValidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class ServerSideValidationApplication {
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("ValidationMessages");
        return messageSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerSideValidationApplication.class, args);
    }
}
