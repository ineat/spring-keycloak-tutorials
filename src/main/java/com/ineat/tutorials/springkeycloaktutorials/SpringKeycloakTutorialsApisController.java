package com.ineat.tutorials.springkeycloaktutorials;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * A Sample controller used to expose Keycloak Secured routes
 */
@RestController
public class SpringKeycloakTutorialsApisController {

    @RequestMapping(path = {"/", "/unsecured"})
    public String noSecuredEndpoint(){
        return "This is an unsecured endpoint payload";
    }


    @RequestMapping(
        path = "/admin",
        method = RequestMethod.GET, // @RequestMapping default assignment
       produces = MediaType.APPLICATION_JSON_VALUE // TIP : use org.springframework.http.MediaType for MimeType instead of hard coded value
    )
    public String adminSecuredEndpoint(){
        return "This is an ADMIN endpoint payload";
    }

    @RequestMapping("/user")
    public String userSecuredEndpoint(){
        return "This is an USER endpoint payload";
    }
}
