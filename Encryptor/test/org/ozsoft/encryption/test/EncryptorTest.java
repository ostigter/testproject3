package org.ozsoft.encryption.test;

import org.ozsoft.encryption.Encryptor;

/**
 * Test driver for the Encryptor class.
 * 
 * @author Oscar Stigter
 */
public class EncryptorTest {

    public static void main(String[] args) throws Exception {
//      byte[] clearText;
//      byte[] cipherText;
//      long startTime;
//      long duration;
//
//      startTime = System.currentTimeMillis();
//      Encryptor encryptor = new Encryptor();
//      duration = System.currentTimeMillis() - startTime;
//      System.out.println("Created encryptor in " + duration + " ms.");
//
//      // Create a password.
//      byte[] password = "aBc#123!".getBytes();
//
//      // Create a key based on the password.
//      startTime = System.currentTimeMillis();
//      encryptor.setPassword(password);
//      duration = System.currentTimeMillis() - startTime;
//      System.out.println("Key created in " + duration + " ms.");
//
//      // Encrypt clear text.
//      clearText = "This is just an example.".getBytes();
//      startTime = System.currentTimeMillis();
//      cipherText = encryptor.encrypt(clearText);
//      duration = System.currentTimeMillis() - startTime;
//      System.out.println("Text encrypted in " + duration + " ms.");
//
//      // Decrypt cipher text.
//      startTime = System.currentTimeMillis();
//      clearText = encryptor.decrypt(cipherText);
//      duration = System.currentTimeMillis() - startTime;
//      System.out.println("Text decrypted in " + duration + " ms.");
//
//      // Create a large data block.
//      int textSize = 10 * 1024 * 1024; // 10 MB
//      clearText = new byte[textSize];
//      Random random = new Random();
//      for (int i = 0; i < textSize; i++) {
//          clearText[i] = (byte) random.nextInt(256);
//      }
//
//      // Encrypt large data block.
//      startTime = System.currentTimeMillis();
//      cipherText = encryptor.encrypt(clearText);
//      duration = System.currentTimeMillis() - startTime;
//      System.out.println("Large text block encrypted in  " + duration + " ms.");
//
//      // Decrypt large data block.
//      startTime = System.currentTimeMillis();
//      clearText = encryptor.decrypt(cipherText);
//      duration = System.currentTimeMillis() - startTime;
//      System.out.println("Large data block decrypted in " + duration + " ms.");
//      
//      // Encrypt file.
//      startTime = System.currentTimeMillis();
//      encryptor.encryptFile("Secret.doc");
//      duration = System.currentTimeMillis() - startTime;
//      System.out.println("Encypted file in " + duration + " ms.");
//
//      // Decrypt file.
//      startTime = System.currentTimeMillis();
//      encryptor.decryptFile("Secret.doc.enc");
//      duration = System.currentTimeMillis() - startTime;
//      System.out.println("Decypted file in " + duration + " ms.");
      
      Encryptor encryptor = new Encryptor();
      encryptor.setKey("aBc#123!");
      
      String cleartext = "This is a very secret message.";
      System.out.format("Cleartext:  '%s'\n", cleartext);
      
      String ciphertext = encryptor.encrypt(cleartext);
      System.out.format("Ciphertext: '%s'\n", ciphertext);
      
      cleartext = encryptor.decrypt(ciphertext);
      System.out.format("Cleartext:  '%s'\n", cleartext);
  }

}
