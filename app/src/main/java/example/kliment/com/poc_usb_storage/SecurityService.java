package example.kliment.com.poc_usb_storage;

import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ktrajche on 21.09.2017.
 */

public class SecurityService {

  private static SecurityService securityService;

  private static SecretKey secretKey = null;
  private static String algorithm = "AES";

  public static SecurityService getSecurityService(){
    if(securityService == null){
      securityService = new SecurityService();
    }
    return securityService;
  }

  private SecurityService(){

  }

  public SecretKey generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    // Number of PBKDF2 hardening rounds to use. Larger values increase
    // computation time. You should select a value that causes computation
    // to take >100ms.
    final int iterations = 1000;

    // Generate a 256-bit key
    final int outputKeyLength = 256;

    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
    return secretKey;
  }

  public SecretKey generateKey() throws NoSuchAlgorithmException {
    // Generate a 256-bit key
    final int outputKeyLength = 256;
    SecureRandom secureRandom = new SecureRandom();
    // Do *not* seed secureRandom! Automatically seeded from system entropy.
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(outputKeyLength, secureRandom);
    secretKey = keyGenerator.generateKey();
    return secretKey;
  }

  public byte[] encodeFile(SecretKey yourKey, byte[] fileData)
      throws Exception {
    byte[] encrypted = null;
    byte[] data = yourKey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length, algorithm);
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
        new byte[cipher.getBlockSize()]));
    encrypted = cipher.doFinal(fileData);
    return encrypted;
  }

  public byte[] decodeFile(SecretKey yourKey, byte[] fileData)
      throws Exception {
    byte[] decrypted = null;
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.DECRYPT_MODE, yourKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
    decrypted = cipher.doFinal(fileData);
    return decrypted;
  }

  void saveFile(byte[] stringToSave, String encryptedFileName) {
    try {
      File file = new File(Environment.getExternalStorageDirectory() + File.separator, encryptedFileName);

      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
      secretKey = generateKey();
      byte[] filesBytes = encodeFile(secretKey, stringToSave);
      bos.write(filesBytes);
      bos.flush();
      bos.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void decodeFile(String encryptedFileName) {

    try {
      byte[] decodedData = decodeFile(secretKey, FileUtils.readFile(encryptedFileName));
      // String str = new String(decodedData);
      //System.out.println("DECODED FILE CONTENTS : " + str);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }



//  public byte[] getAudioFile() throws FileNotFoundException
//  {
//    byte[] audio_data = null;
//    byte[] inarry = null;
//    AssetManager am = getAssets();
//    try {
//      InputStream is = am.open("Sleep Away.mp3"); // use recorded file instead of getting file from assets folder.
//      int length = is.available();
//      audio_data = new byte[length];
//      int bytesRead;
//      ByteArrayOutputStream output = new ByteArrayOutputStream();
//      while ((bytesRead = is.read(audio_data)) != -1) {
//        output.write(audio_data, 0, bytesRead);
//      }
//      inarry = output.toByteArray();
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//    return inarry;
//
//  }

//  private void playMp3(byte[] mp3SoundByteArray) {
//
//    try {
//      // create temp file that will hold byte array
//      File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
//      tempMp3.deleteOnExit();
//      FileOutputStream fos = new FileOutputStream(tempMp3);
//      fos.write(mp3SoundByteArray);
//      fos.close();
//      // Tried reusing instance of media player
//      // but that resulted in system crashes...
//      MediaPlayer mediaPlayer = new MediaPlayer();
//      FileInputStream fis = new FileInputStream(tempMp3);
//      mediaPlayer.setDataSource(fis.getFD());
//      mediaPlayer.prepare();
//      mediaPlayer.start();
//    } catch (IOException ex) {
//      ex.printStackTrace();
//
//    }
//
//  }

}
