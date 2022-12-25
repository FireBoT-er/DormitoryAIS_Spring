package com.cws._7s_cr.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/Students");
        registry.addViewController("/login").setViewName("Shared/login");
        registry.addViewController("/logout").setViewName("Shared/login");
    }
}
