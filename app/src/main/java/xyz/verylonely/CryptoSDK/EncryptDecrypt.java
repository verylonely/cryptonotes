package xyz.verylonely.CryptoSDK;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public abstract class EncryptDecrypt {

    private final static int SALT_SIZE = 16;
    private final static String ALGO = "AES/CBC/PKCS5Padding";

    public static String EncryptString(SecretKey key, String string) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGO);

        byte[] iv = generateIv();

        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] cipherText = cipher.doFinal(string.getBytes(StandardCharsets.UTF_8));

        byte[] cipherTextAndSalt = new byte[iv.length + cipherText.length];

        System.arraycopy(iv, 0, cipherTextAndSalt, 0, iv.length);
        System.arraycopy(cipherText, 0 , cipherTextAndSalt, iv.length, cipherText.length);

        return Base58.encode(cipherTextAndSalt);
        //return Base64.getEncoder().encodeToString(cipherTextAndSalt);
    }

    public static String DecryptString(String string, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        //byte[] cipherTextAndSalt = Base64.getDecoder().decode(string);
        byte[] cipherTextAndSalt = Base58.decode(string);

        byte[] iv = new byte[SALT_SIZE];
        byte[] cipherText = new byte[cipherTextAndSalt.length - SALT_SIZE];

        System.arraycopy(cipherTextAndSalt, 0, iv, 0, iv.length);
        System.arraycopy(cipherTextAndSalt, iv.length, cipherText, 0, cipherText.length);

        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }


    public static byte[] generateIv() {
        byte[] iv = new byte[SALT_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }


}
