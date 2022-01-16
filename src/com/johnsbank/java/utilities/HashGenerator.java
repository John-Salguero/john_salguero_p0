package com.johnsbank.java.utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    private static final HashGenerator instance = new HashGenerator();
    private HashGenerator() {}
    public static HashGenerator getInstance() { return instance;}
    private static MessageDigest SHA256Alg;

    private static final char[] HEX_ARRAY = { // Used for transforming the Hash byte data into human-readable form
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'};

    {
        try {
            SHA256Alg = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 could not be used!", e);
        }

    }

    /**
     * The method that securely Hashes User's confidential information
     */
    public String getMessageDigest(String pass){

        // Hash the message to securely save and compare
        byte[] hash = SHA256Alg.digest(pass.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    /**
     * Transforms the given byte array into a hexadecimal number in string format
     * @param bytes the byte array to transform
     * @return a hexadecimal representation of the array in string format
     */
    private String bytesToHex(byte[] bytes) {
        // array of characters in ascii form representing the data in byte array
        char[] hexChars = new char[bytes.length * 2];

        // convert each byte into a hexadecimal value and convert that into ascii format
        for (int j = 0; j < bytes.length; j++) {
            int curByte = bytes[j];
            // bit-shift the byte by 4 to retrieve the higher WORD and use that as an index into the array to convert
            hexChars[j * 2] = HEX_ARRAY[(curByte >>> 4) & 0x0F];
            // AND the lower WORD by 16 to isolate it and use that as an index into the array to convert
            hexChars[j * 2 + 1] = HEX_ARRAY[curByte & 0x0F];
        }
        return new String(hexChars);
    }
}
