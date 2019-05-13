package com.brodiequinlan.sprintrestrospective.models;

public class Project {
    public final String name;
    public final String owner;
    //used in serialization DO NOT MAKE PRIVATE or you'll break the API
    //NOT unused, used in serialization at the endpoint
    @SuppressWarnings({"WeakerAccess", "unused"})
    public String id;

    public Project(String name, String owner, String id) {
        this.name = name;
        this.owner = owner;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
