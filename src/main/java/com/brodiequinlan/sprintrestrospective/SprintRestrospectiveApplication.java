package com.brodiequinlan.sprintrestrospective;

import com.brodiequinlan.sprintrestrospective.database.SqlConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SprintRestrospectiveApplication implements CommandLineRunner {

    public static final boolean DEBUG = true;

    private @Value("${pepper}")
    String pepper;

    private @Value("${URL}")
    String url;

    private @Value("${uname}")
    String username;

    private @Value("${password}")
    String password;


    public static void main(String[] args) {
        SpringApplication.run(SprintRestrospectiveApplication.class, args);
    }

    @Override
    public void run(String... args) {
        //im not really sure of a better way to do this. If i just make the variables in sqlconnection non static and annotate with @Value they end up null
        //so this is a temporary solution i figured out.
        SqlConnection.pepper = pepper;
        SqlConnection.password = password;
        SqlConnection.url = url;
        SqlConnection.username = username;
    }
}
