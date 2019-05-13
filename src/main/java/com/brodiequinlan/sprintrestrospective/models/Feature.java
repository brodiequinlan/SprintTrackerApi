package com.brodiequinlan.sprintrestrospective.models;

import java.util.List;


public class Feature {
    public final String name;
    public final String points;
    //used in serialization DO NOT MAKE PRIVATE or you'll break the API
    //NOT unused, used in serialization at the endpoint
    @SuppressWarnings({"WeakerAccess", "unused"})
    public final List<String> users;
    //NOT unused, used in serialization at the endpoint
    @SuppressWarnings("unused")
    public String id;
    public String requester;

    public Feature(String id, String name, String points, String requester, List<String> users) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.requester = requester;
        this.users = users;
    }
}
