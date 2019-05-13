package com.brodiequinlan.sprintrestrospective.models;

import java.util.List;


public class Feature {
    public final String name;
    public final String points;

    //this CANNOT have weaker access, it is used in serialization and making it private leads to it not being serialized.
    @SuppressWarnings({"WeakerAccess", "unused"})
    public final List<String> users;
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
