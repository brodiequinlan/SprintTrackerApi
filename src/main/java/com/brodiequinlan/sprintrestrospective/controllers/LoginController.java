package com.brodiequinlan.sprintrestrospective.controllers;

import com.brodiequinlan.sprintrestrospective.models.Login;
import com.brodiequinlan.sprintrestrospective.models.Token;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/api/login")
@RestController
public class LoginController {
    /*
        Debating putting this in another controller, but for now we're using randomly generated keys for tokens within this class
        which means if i want to issue a token on registration to bypass the login screen we need to keep it in here for now (just until getting a server specific key)
     */
    @RequestMapping(value = "/register/{username}/{password}", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Token register(@PathVariable String username, @PathVariable String password) {
        return Login.register(username, password);
    }

    @RequestMapping(value = "/{username}/{password}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Token issue(@PathVariable String username, @PathVariable String password) {
        return Login.login(username, password);
    }

    @RequestMapping(value = "/validate/{user}/{token}", method = RequestMethod.GET)
    public boolean validate(@PathVariable String token, @PathVariable String user) {
        return Login.validate_token(token, user);
    }

}
