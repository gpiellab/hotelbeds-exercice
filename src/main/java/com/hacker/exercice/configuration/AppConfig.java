package com.hacker.exercice.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


//MAIN CONFIG CLASS

@Configuration
@ComponentScan(basePackages = "com.hacker.exercice")
@PropertySource("classpath:application.properties")
public class AppConfig {

}
