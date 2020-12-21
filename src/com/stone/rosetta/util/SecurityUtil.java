package com.stone.rosetta.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class SecurityUtil {
    private SecretKeyFactory secretKeyFactory;
    private byte[] salt;

    public SecurityUtil() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        salt = new byte[16];
        secureRandom.nextBytes(salt);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(salt);
        secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    }

    public byte[] generatePassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, 16, 128);
        return secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
    }


}
