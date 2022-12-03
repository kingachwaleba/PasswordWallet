package com.example.passwordwallet.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
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

    // Encrypt a string with AES algorithm.
    public static String encrypt(String data, Key key) throws Exception {
        if (data.equals("") || key == null)
            throw new IllegalArgumentException();

        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    // Decrypt a string with AES algorithm.
    public static String decrypt(String encryptedData, Key key) throws Exception {
        if (encryptedData.equals("") || key == null)
            throw new IllegalArgumentException();

        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decoderValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decoderValue);
        return new String(decValue);
    }

    public static byte[] calculateMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static Key generateKey(String password) {
        return new SecretKeySpec(calculateMD5(password), "AES");
    }
}
