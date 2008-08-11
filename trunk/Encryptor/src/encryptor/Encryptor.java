package encryptor;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;


/**
 * Encrypts and decrypts data with 128 bits AES cryptography using a password
 * based key.
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

    /** Cipher. */
    private final Cipher cipher;

    /** Key. */
    private SecretKeySpec key;


    /**
     * Constructs an <code>Encryptor</code>.
     *
     * @throws  EncryptionException  if the cipher could not be created
     */
    public Encryptor() throws EncryptionException {
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		} catch (Exception e) {
			throw new EncryptionException("Could not create cipher", e);
		}
	}


    /**
     * Sets the password.
     *
     * Uses an MD5 hash of the password to generate the key.
     *
     * @param  password  the password
     *
     * @throws  IllegalArgumentException
     *              if the password is null or empty
     * @throws  EncryptionException
     *              if the MD5 hash could not be created from the password
     */
    public void setPassword(byte[] password) throws EncryptionException {
		// Check aguments.
		if (password == null || password.length == 0) {
			throw new IllegalArgumentException("Null or empty password");
		}
		
		MessageDigest md; 
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			throw new EncryptionException(
					"Could not create message digest for password", e);
		}
		
        key = new SecretKeySpec(md.digest(password), KEY_ALGORITHM);
	}
	
	
    /**
     * Encrypts a byte array of cleartext to ciphertext.
     * 
     * @param  clearText  the cleartext
     * 
     * @return  the ciphertext
     * 
     * @throws  IllegalArgumentException
     *              if the cleartext is null or empty
     * @throws  IllegalStateException
     *              if no password has been set
     * @throws  EncryptionException
     *              if the encryption failed
     */
    public byte[] encrypt(byte[] clearText) throws EncryptionException {
    	if (clearText == null || clearText.length == 0) {
    		throw new IllegalArgumentException("Null or empty cleartext");
    	}
    	if (key == null) {
    		throw new IllegalStateException("No password set");
    	}
    	
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
	        return cipher.doFinal(clearText);
		} catch (Exception e) {
			throw new EncryptionException("Could not encrypt data", e);
		}
	}


    /**
     * Decrypts a byte array of ciphertext to cleartext.
     * 
     * @param  cipherText  the ciphertext
     * 
     * @return  the cleartext
     * 
     * @throws  IllegalArgumentException
     *              if the ciphertext is null or empty
     * @throws  IllegalStateException
     *              if no password has been set
     * @throws  EncryptionException
     *              if the decryption failed
     */
    public byte[] decrypt(byte[] cipherText) throws EncryptionException {
    	if (cipherText == null || cipherText.length == 0) {
    		throw new IllegalArgumentException("Null or empty ciphertext");
    	}
    	if (key == null) {
    		throw new IllegalStateException("No password set");
    	}
    	
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
	        return cipher.doFinal(cipherText);
		} catch (Exception e) {
			throw new EncryptionException(
			        "Could not decrypt data: " + e.getMessage());
		}
	}
    
    
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
            os = new CipherOutputStream(
                    new FileOutputStream(targetFile, true), cipher);
            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            os.close();
            fis.close();
        } catch (Exception e) {
            throw new EncryptionException(
                    "Could not encrypt file: " + e.getMessage());
        }
    }


    public void decryptFile(String path) throws EncryptionException {
        File sourceFile = new File(path);
        String targetPath = path.substring(0, path.length() - 4);
        File targetFile = new File(targetPath);
        try {
            InputStream is = new FileInputStream(sourceFile);
            byte[] checkBlock = new byte[32];
            is.read(checkBlock);
            if (!compareBlocks(checkBlock, encrypt(new byte[31]))) {
                throw new EncryptionException("Incorrect password");
            }
            try {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } catch (Exception e) {
                throw new EncryptionException("Could not initialize cipher");
            }
            CipherOutputStream cos = new CipherOutputStream(
                    new FileOutputStream(targetFile), cipher);
            byte[] buffer = new byte[8192];
            int len;
            while ((len = is.read(buffer)) > 0) {
                cos.write(buffer, 0, len);
            }
            cos.close();
            is.close();
        } catch (IOException e) {
            throw new EncryptionException(
                    "Could not decrypt file: " + e.getMessage());
        }
    }
    
    
    private boolean compareBlocks(byte[] block1, byte[] block2) {
        boolean isEqual = (block1.length == block2.length);
        for (int i = 0; i < block1.length && isEqual; i++) {
            isEqual = (block1[i] == block2[i]);
        }
        return isEqual;
    }


}
