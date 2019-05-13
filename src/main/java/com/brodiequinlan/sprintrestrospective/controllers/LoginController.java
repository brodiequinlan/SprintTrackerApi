package com.brodiequinlan.sprintrestrospective.controllers;

import com.brodiequinlan.sprintrestrospective.models.Login;
import com.brodiequinlan.sprintrestrospective.models.Token;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/api/login")
@RestController
public class LoginController {

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
