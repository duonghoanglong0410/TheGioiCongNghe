package com.example.thegioicongnghe;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.example.thegioicongnghe.User.Controller", "com.example.thegioicongnghe.Admin.Controller"})
public class AppConfig {
    // Configuration code
}
