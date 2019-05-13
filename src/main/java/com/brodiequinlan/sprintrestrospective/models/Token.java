package com.brodiequinlan.sprintrestrospective.models;

public class Token {
    //used in serialization DO NOT MAKE PRIVATE or you'll break the API
    //NOT unused, used in serialization at the endpoint
    @SuppressWarnings({"WeakerAccess", "unused"})
    public final String token;
    //used in serialization DO NOT MAKE PRIVATE or you'll break the API
    //NOT unused, used in serialization at the endpoint
    @SuppressWarnings({"WeakerAccess", "unused"})
    public final String message;


    Token(String token, String message) {
        this.token = token;
        this.message = message;
    }
}
