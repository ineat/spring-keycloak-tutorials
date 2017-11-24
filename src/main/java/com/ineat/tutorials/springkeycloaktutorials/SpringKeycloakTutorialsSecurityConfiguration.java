package com.ineat.tutorials.springkeycloaktutorials;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SpringKeycloakTutorialsSecurityConfiguration {

    @KeycloakConfiguration
    @ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
    public static class KeycloakConfigurationAdapter extends KeycloakWebSecurityConfigurerAdapter {
        /**
         * Defines the session authentication strategy.
         */
        @Bean
        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            // required for bearer-only applications.
            return new NullAuthenticatedSessionStrategy();
        }

        /**
         * Registers the KeycloakAuthenticationProvider with the authentication manager.
         */
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
            // simple Authority Mapper to avoid ROLE_
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(keycloakAuthenticationProvider);
        }


        /**
         * Configuration spécifique à keycloak (ajouts de filtres, etc)
         * @param http
         * @throws Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http.httpBasic().disable();

            http
                    // disable csrf because of API mode
                    .csrf().disable()
                    .formLogin().disable()
                    .sessionManagement()
                        // use previously declared bean
                        .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


                    // keycloak filters for securisation
                    .and()
                        .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                        .addFilterBefore(keycloakAuthenticationProcessingFilter(), X509AuthenticationFilter.class)
                        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())

                    // delegate logout endpoint to spring security

                    .and()
                        .logout()
                        .addLogoutHandler(keycloakLogoutHandler())
                        .logoutUrl("/logout").logoutSuccessHandler(
                            // logout handler for API
                            (HttpServletRequest request, HttpServletResponse response, Authentication authentication) ->
                                    response.setStatus(HttpServletResponse.SC_OK)
                         )
                    .and()
                        // manage routes securisation here
                        .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()


                        .antMatchers("/logout", "/", "/unsecured").permitAll()
                        .antMatchers("/user").hasRole("USER")
                        .antMatchers("/admin").hasRole("ADMIN")

                        .anyRequest().denyAll();
        }
    }
}
