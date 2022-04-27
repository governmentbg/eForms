package com.bulpros.eformsgateway.eformsintegrations.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AES_GCM_Utils {

    private AES_GCM_Utils() {
    }

    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    public static String decrypt(byte[] encryptedText, byte[] secretKey) throws Exception {
        byte[] IV = new byte[GCM_IV_LENGTH];
        ByteBuffer bb = ByteBuffer.wrap(encryptedText);
        bb.get(IV);
        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
        // Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);
        return new String(decryptedText, UTF_8);
    }


}
