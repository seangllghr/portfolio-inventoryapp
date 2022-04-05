package com.mobile2app.inventoryapp;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {

    private static final byte[] HEX_ARRAY = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);

    /***
     * Convert an array of bytes into a string of hex digits
     *
     * Source: https://stackoverflow.com/a/9855338
     * @param bytes a {@code byte[]} containing the bytes to convert
     * @return a {@code String} containing the hex representation of the bytes
     */
    private static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    /***
     * Generate a hash from the provided password and salt strings
     *
     * Source: https://android-developers.googleblog.com/2013/02/using-cryptography-to-store-credentials.html
     * @param password a {@code char[]} containing the password
     * @param salt a {@code byte[]} containing the salt
     * @return a {@code String} containing the hexadecimal password hash
     * @throws NoSuchAlgorithmException when provided with an invalid algorithm
     * @throws InvalidKeySpecException when the key spec is invalid
     */
    public static String generateHash(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        final int iterations = 1000;
        final int outputKeyLength = 256;
        char[] passArray = password.toCharArray();
        byte[] saltArray = hexToBytes(salt);

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passArray, saltArray, iterations, outputKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        byte[] hashPassArray = secretKey.getEncoded();

        return bytesToHex(hashPassArray);
    }

    /***
     * Returns a random salt for password hashing
     *
     * @return a 16-byte random salt
     */
    public static String getNextSalt() {
        final SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    /***
     * Convert a string of hex digits to bytes.
     *
     * Source: https://stackoverflow.com/a/50714115
     * @param hexString a {@code String} containing the hex digits
     * @return an array of {@code byte}s
     * @throws IllegalArgumentException if the string contains an odd number of digits
     */
    private static byte[] hexToBytes(String hexString) throws IllegalArgumentException {
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have 2 characters per byte!");
        }
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(hexString.substring(index, index + 2), 16);
            bytes[i] = (byte) v;
        }
        return bytes;
    }

}
