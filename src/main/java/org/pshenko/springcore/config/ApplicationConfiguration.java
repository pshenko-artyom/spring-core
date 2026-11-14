package org.pshenko.springcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Scanner;

@Configuration
@ComponentScan("org.pshenko.springcore")
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {
    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }
}
