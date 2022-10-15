package com.example.passwordwallet.encoders;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha512PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return getPasswordWithSHA512(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }

    public static String getPasswordWithSHA512(String text) {
        try {
            // Get an instance of SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // Calculate message digest of the input string - returns byte array
            byte[] messageDigest = md.digest(text.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashText = new StringBuilder(no.toString(16));

            // Add preceding 0s to make it 32 bit
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }

            // Return the HashText
            return hashText.toString();
        } catch (NoSuchAlgorithmException exception) { // If wrong message digest algorithm was specified
            throw new RuntimeException(exception);
        }
    }
}