package org.vaadin.example;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.LoginPage;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {
    private final SpecificationsService service;

    public SecurityConfig(SpecificationsService service) {
        this.service = service;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.authorizeHttpRequests().requestMatchers("/images/*.png").permitAll();
        super.configure(http);
        setLoginView(http, LoginPage.class);
    }

    @Bean
    public UserDetailsService users() {
        List<MitarbeiterEntity> mitarbeiterEntities = service.findAllUser("");

        ArrayList<UserDetails> userDetails = new ArrayList<>();

        for (MitarbeiterEntity mitarbeiterEntity : mitarbeiterEntities) {
            userDetails.add(User.builder()
                    .username(mitarbeiterEntity.getBenutzername())
                    .password("{SHA-256}"+mitarbeiterEntity.getPasswort())
                    .roles("USER")
                    .build());
        }

        //return new InMemoryUserDetailsManager(user, admin);
        return new InMemoryUserDetailsManager(userDetails);
    }
}