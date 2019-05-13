package com.brodiequinlan.sprintrestrospective.models;

import com.brodiequinlan.sprintrestrospective.database.SqlConnection;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;

public class Login {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    public static Token login(String username, String password) {
        SqlConnection sql = new SqlConnection();
        if (sql.validateUser(username, password)) {
            return new Token(Jwts.builder().setSubject(username).signWith(key).compact(), "successful login");
        } else {
            return new Token("INVALID_AUTH", "Incorrect username or password");
        }

    }

    public static Token register(String username, String password) {
        SqlConnection sql = new SqlConnection();
        String message = sql.addUser(username, password);
        if (message.equals("success")) {
            return new Token(Jwts.builder().setSubject(username).signWith(key).compact(), message);
        } else {
            return new Token("INVALID_REG", message);
        }
    }

    public static boolean validate_token(String token, String user) {
        try {

            if (Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject().equals(user)) return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
