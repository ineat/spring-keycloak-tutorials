package com.ineat.tutorials.springkeycloaktutorials;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SpringKeycloakTutorialsApisController.class)
@Import(SpringKeycloakTutorialsSecurityTestConfiguration.class)
public class SpringKeycloakTutorialsSecurityTests {

    @Autowired
    MockMvc mockMvc;


    @Test
    public void testUnsecuredPathIsAllowedForAll() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/unsecured"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("This is an unsecured endpoint payload"));
    }



    @Test
    @WithMockUser
    public void testAdminPathIsNotAllowedForAll() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdmindPathIsOnlyAllowedForAdminProfil() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("This is an ADMIN endpoint payload"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAdmindPathIsNotAllowedForUserProfil() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/admin"))
                .andExpect(status().isForbidden());
    }
}

/***
 * <p>Use this configuration class to test if your path is secured</p>
 * <p>his class use {@link SpringKeycloakTutorialsSecurityConfiguration.CommonSpringKeycloakTutorialsSecuritAdapter} which define all
 * security matchers</p>
 */
@TestConfiguration
@EnableWebSecurity
 class SpringKeycloakTutorialsSecurityTestConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // use the common configuration to validate matchers
        http.apply(new SpringKeycloakTutorialsSecurityConfiguration.CommonSpringKeycloakTutorialsSecuritAdapter());

    }
}
