
package crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;



public class AESUtils {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;


    public static SecretKey generateKey(String password) throws Exception {
        SecureRandom random = new SecureRandom(password.getBytes());
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE, random);
        return keyGen.generateKey();
    }


    public static void encryptFile(SecretKey key, File inputFile, OutputStream outputStream) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        try (FileInputStream fis = new FileInputStream(inputFile);
             CipherOutputStream cos = new CipherOutputStream(outputStream, cipher)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
    }


    public static void decryptFile(SecretKey key, InputStream inputStream, File outputFile) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        try (CipherInputStream cis = new CipherInputStream(inputStream, cipher);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }


    public static void saveKey(SecretKey key, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(key.getEncoded());
        }
    }


    public static SecretKey loadKey(String filePath) throws IOException {
        byte[] keyBytes = new byte[32];
        try (FileInputStream fis = new FileInputStream(filePath)) {
            fis.read(keyBytes);
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

}
