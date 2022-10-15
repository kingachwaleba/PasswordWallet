package com.example.passwordwallet.utils;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class SecureUtils {

    public static String getSalt() throws NoSuchAlgorithmException {
        // The name of the pseudo-random number generation (PRNG) algorithm supplied by the SUN provider.
        // This algorithm uses SHA-1 as the foundation of the PRNG.
        // It computes the SHA-1 hash over a true-random seed value concatenated with a 64-bit counter
        // which is incremented by 1 for each operation. From the 160-bit SHA-1 output, only 64 bits are used.
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        Base64.Encoder enc = Base64.getEncoder();

        return enc.encodeToString(salt);
    }
}
