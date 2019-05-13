package com.brodiequinlan.sprintrestrospective.security;


import java.security.SecureRandom;
import java.util.Random;

public class Salt {

    /* Assign a string that contains the set of characters you allow. */
    private static final String symbols = "ABCDEFGJKLMNPRSTUVWXYZ0123456789";

    private final static Random random = new SecureRandom();


    public static String generateSalt() {
        char[] buf = new char[16];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols.charAt(random.nextInt(symbols.length()));
        return new String(buf);
    }

}