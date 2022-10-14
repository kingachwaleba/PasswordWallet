package com.example.passwordwallet.utils;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecureUtils {

    public static String getPasswordWithSHA512(String text) {
        try {
            // Get an instance of SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // Calculate message digest of the input string - returns byte array
            byte[] messageDigest = md.digest(text.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashtext = new StringBuilder(no.toString(16));

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }

            // Return the HashText
            return hashtext.toString();
        } catch (NoSuchAlgorithmException exception) { // If wrong message digest algorithm was specified
            throw new RuntimeException(exception);
        }
    }

    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        Base64.Encoder enc = Base64.getEncoder();

        return enc.encodeToString(salt);
    }
}
