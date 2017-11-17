package com.ineat.tutorials.springkeycloaktutorials;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SpringKeycloakTutorialsSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();

        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest().permitAll()
                .and().formLogin().disable() // <-- this will disable the login route
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
