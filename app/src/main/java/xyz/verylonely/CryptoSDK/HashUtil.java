package xyz.verylonely.CryptoSDK;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class HashUtil {

    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA512 = "SHA-512";
    public static final String MD5 = "MD5";

    public static String bytesToHex(byte[] data)
    {
        StringBuilder hexString = new StringBuilder(2 * data.length);
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(0xff & data[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String hashFromBytes(byte[] data, String algo)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance(algo);
            byte[] hash = digest.digest(data);
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;

    }
}
