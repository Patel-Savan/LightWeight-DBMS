package org.example.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Hash class for hashing password
 */
public class Hash {
    /**
     * performs hashing operation on given password
     * Took reference for hashPassword from JavaTPoint
     * @param password user input password
     * @return hashed password as string
     */
    public static String hashPassword(String password){         //learned it from JavaTPoint

        String hashedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");

            byte[] digest = md.digest(password.getBytes());

            BigInteger hashedInt = new BigInteger(1,digest);

            hashedPassword = hashedInt.toString(16);

        }catch(Exception e){
            return null;
        }
        return hashedPassword;

    }
}
