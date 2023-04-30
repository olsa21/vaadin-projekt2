package org.vaadin.example.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.login.LoginPage;

@EnableWebSecurity
@Configuration
/**
 * Enthält die Konfiguration für den Login
 */
public class SecurityConfig extends VaadinWebSecurity {
    private final SpecificationsService service;

    public SecurityConfig(SpecificationsService service) {
        this.service = service;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginPage.class);
    }

    /*@Bean
    public UserDetailsService userDetailsService(){
        return new DatebaseUserDetailsService(service);
    }*/
}