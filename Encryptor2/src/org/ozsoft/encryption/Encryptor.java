package org.ozsoft.encryption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encryption library using 128-bit AES cryptography with a shared key. <br />
 * <br />
 * 
 * This is a programmer-friendly wrapper around the Java Cryptography Extention
 * (JCE) API.
 * 
 * @author Oscar Stigter
 */
public class Encryptor {

    /** Key algorithm. */
    private static final String KEY_ALGORITHM = "AES";

    /** Cipher algorithm. */
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /** The cipher engine. */
    private final Cipher cipher;

    /** The encoded key. */
    private SecretKeySpec key;

    /**
     * Constructs an <code>Encryptor</code>.
     * 
     * @throws EncryptionException
     *             if the cipher could not be created
     */
    public Encryptor() throws EncryptionException {
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        } catch (Exception e) {
            throw new EncryptionException("Could not create cipher", e);
        }
    }
    
    /**
     * Sets the shared key based on a password.
     * 
     * @param password
     *            The password.
     * 
     * @throws IllegalArgumentException
     *             If the byte array is null or empty.
     * @throws EncryptionException
     *             If the shared key could not be generated.
     */
    public void setKey(String password) throws EncryptionException {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Null or empty password");
        }
        
        try {
            byte[] passwordData = password.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            key = new SecretKeySpec(md.digest(passwordData), KEY_ALGORITHM);
        } catch (Exception e) {
            throw new EncryptionException("Could not generated shared key", e);
        }
    }

    /**
     * Sets the shared key based on a byte array. <br />
     * <br />
     * 
     * Uses an MD5 hash of the byte array to generate the shared key.
     * 
     * @param data
     *            The byte array.
     * 
     * @throws IllegalArgumentException
     *             If the byte array is null or empty.
     * @throws EncryptionException
     *             If the shared key hash could not be generated.
     */
    public void setKey(byte[] data) throws EncryptionException {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Null or empty password");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            key = new SecretKeySpec(md.digest(data), KEY_ALGORITHM);
        } catch (Exception e) {
            throw new EncryptionException("Could not generated shared key", e);
        }
    }

    /**
     * Encrypts a byte array.
     * 
     * @param cleartext
     *            The byte array with the cleartext.
     * 
     * @return A byte array with the ciphertext.
     * 
     * @throws IllegalArgumentException
     *             If the cleartext is null or empty.
     * @throws IllegalStateException
     *             If no shared key has been set.
     * @throws EncryptionException
     *             If the byte array could not be encrypted.
     */
    public byte[] encrypt(byte[] cleartext) throws EncryptionException {
        if (cleartext == null || cleartext.length == 0) {
            throw new IllegalArgumentException("Null or empty cleartext");
        }
        if (key == null) {
            throw new IllegalStateException("No shared key set");
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(cleartext);
        } catch (Exception e) {
            throw new EncryptionException("Could not encrypt data", e);
        }
    }

    /**
     * Decrypts a byte array.
     * 
     * @param ciphertext
     *            The byte array with the ciphertext.
     * 
     * @return A byte array with the cleartext.
     * 
     * @throws IllegalArgumentException
     *             If the ciphertext is null or empty.
     * @throws IllegalStateException
     *             If no shared key has been set.
     * @throws EncryptionException
     *             If the byte array could not be decrypted.
     */
    public byte[] decrypt(byte[] ciphertext) throws EncryptionException {
        if (ciphertext == null || ciphertext.length == 0) {
            throw new IllegalArgumentException("Null or empty ciphertext");
        }
        if (key == null) {
            throw new IllegalStateException("No shared key set");
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(ciphertext);
        } catch (Exception e) {
            throw new EncryptionException("Could not decrypt data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Encrypts a String.
     * 
     * @param cleartext
     *            The String with the cleartext.
     * 
     * @return A String with the ciphertext.
     * 
     * @throws EncryptionException
     *             If the String could not be encrypted.
     */
    public String encrypt(String cleartext) throws EncryptionException {
        if (cleartext == null || cleartext.length() == 0) {
            throw new IllegalArgumentException("Null or empty cleartext");
        }
        if (key == null) {
            throw new IllegalStateException("No shared key set");
        }
        
        String ciphertext = null;
        
        try {
            byte[] clearData = cleartext.getBytes("UTF-8");
            byte[] cipherData = encrypt(clearData);
            ciphertext = bytesToHex(cipherData);
        } catch (UnsupportedEncodingException e) {
            // This should never happen.
            throw new EncryptionException("Could not encoding UTF-8 string: " + e.getMessage());
        }
        
        return ciphertext;
    }
    
    /**
     * Decrypts a String.
     * 
     * @param ciphertext
     *            The String with the ciphertext.
     * 
     * @return A String with the plaintext.
     * 
     * @throws EncryptionException
     *             If the String could not be decrypted.
     */
    public String decrypt(String ciphertext) throws EncryptionException {
        if (ciphertext == null || ciphertext.length() == 0) {
            throw new IllegalArgumentException("Null or empty ciphertext");
        }
        if (key == null) {
            throw new IllegalStateException("No shared key set");
        }
        
        String cleartext = null;
        
        try {
            byte[] cipherData = hexToBytes(ciphertext);
            byte[] clearData = decrypt(cipherData);
            cleartext = new String(clearData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // This should never happen.
            throw new EncryptionException("Could not decoding UTF-8 string: " + e.getMessage());
        }
        
        return cleartext;
    }
    
    /**
     * Encrypts a file. <br />
     * <br />
     * 
     * The encrypted file is placed in the same directoy with an '.enc' suffix.
     * 
     * @param path
     *            The path to the file.
     * 
     * @throws EncryptionException
     *             If the file could not be encrypted.
     */
    public void encryptFile(String path) throws EncryptionException {
        File sourceFile = new File(path);
        File targetFile = new File(path + ".enc");
        try {
            // Write encrypted check block.
            OutputStream os = new FileOutputStream(targetFile);
            byte[] checkBlock = encrypt(new byte[31]);
            os.write(checkBlock);
            os.close();

            // Append encrypted file contents.
            cipher.init(Cipher.ENCRYPT_MODE, key);
            FileInputStream fis = new FileInputStream(sourceFile);
            os = new CipherOutputStream(new FileOutputStream(targetFile, true), cipher);
            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            os.close();
            fis.close();
        } catch (Exception e) {
            throw new EncryptionException("Could not encrypt file: " + e.getMessage(), e);
        }
    }

    /**
     * Decrypts a file.
     * 
     * @param path
     *            The path to the file.
     * 
     * @throws EncryptionException
     *             If the file could not be decrypted.
     */
    public void decryptFile(String path) throws EncryptionException {
        File sourceFile = new File(path);
        String targetPath = path.substring(0, path.length() - 4);
        File targetFile = new File(targetPath);
        try {
            InputStream is = new FileInputStream(sourceFile);
            byte[] checkBlock = new byte[32];
            is.read(checkBlock);
            if (!byteArraysEqual(checkBlock, encrypt(new byte[31]))) {
                throw new EncryptionException("Incorrect key");
            }
            try {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } catch (Exception e) {
                throw new EncryptionException("Could not initialize cipher");
            }
            CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(targetFile), cipher);
            byte[] buffer = new byte[8192];
            int len;
            while ((len = is.read(buffer)) > 0) {
                cos.write(buffer, 0, len);
            }
            cos.close();
            is.close();
        } catch (IOException e) {
            throw new EncryptionException("Could not decrypt file: " + e.getMessage(), e);
        }
    }

    /**
     * Converts a byte array to a hexadecimal String.
     * 
     * @param data
     *            The byte array.
     * 
     * @return The hexadecimal String
     */
    private static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int b : data) {
            if (b < 0) {
                b += 256;
            }
            String hex = Integer.toHexString(b);
            if (hex.length() < 2) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    
    /**
     * Converts a hexadecimal String to a byte array.
     * 
     * @param hex
     *            The hexadecimal String.
     * 
     * @return The byte array.
     */
    private static byte[] hexToBytes(String hex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int length = hex.length();
        for (int i = 0; i < length; i += 2) {
            baos.write((byte) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return baos.toByteArray();
    }

    /**
     * Checks whether two byte arrays are equal.
     * 
     * @param a1
     *            The first byte array.
     * @param a2
     *            The second byte array.
     * 
     * @return True if equal, otherwise false.
     */
    private static boolean byteArraysEqual(byte[] a1, byte[] a2) {
        boolean isEqual = (a1.length == a2.length);
        for (int i = 0; i < a1.length && isEqual; i++) {
            isEqual = (a1[i] == a2[i]);
        }
        return isEqual;
    }

}
