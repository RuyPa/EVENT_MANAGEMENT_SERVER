package com.mobile_app_server.utils;


import com.mobile_app_server.entity.UserEntity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtils {

    public static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String rawPassword, String passwordSalt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(Base64.getDecoder().decode(passwordSalt));
        byte[] hash = digest.digest(rawPassword.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String rawPassword, UserEntity userEntity) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return hashPassword(userEntity.getPasswordHash(), userEntity.getPasswordSalt()).equals(rawPassword);
    }
}
