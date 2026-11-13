package org.pshenko.springcore.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.pshenko.springcore")
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {
}
