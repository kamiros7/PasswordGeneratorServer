package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static String generateSHA256Hash(String input) {
        // Convert the byte array to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the input string to bytes
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));


            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }
}
