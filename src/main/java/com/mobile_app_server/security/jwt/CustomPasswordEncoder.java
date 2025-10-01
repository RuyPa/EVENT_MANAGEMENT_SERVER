package com.mobile_app_server.security.jwt;

import com.mobile_app_server.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CustomPasswordEncoder  implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {

        return Base64.getEncoder().encodeToString(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String storedPasswordWithHashAndSalt) {

        String[] parts = storedPasswordWithHashAndSalt.split(":");

        String hash = parts[0];
        String salt = parts[1];

        String hashInput = hashWithSalt(rawPassword.toString(), salt);

        return hash.equals(hashInput);
    }

    private String hashWithSalt(String password, String saltBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashed = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
