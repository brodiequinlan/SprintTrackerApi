package com.brodiequinlan.sprintrestrospective.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static String sha512(String password) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-512");
            byte[] pass = password.getBytes();
            sha.update(pass);
            byte[] digest = sha.digest();
            StringBuilder hexDigest = new StringBuilder();
            for (byte b : digest) hexDigest.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            return hexDigest.toString();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
