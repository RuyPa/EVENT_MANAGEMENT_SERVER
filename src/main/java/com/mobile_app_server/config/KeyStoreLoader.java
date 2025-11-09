package com.mobile_app_server.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

@Configuration
public class KeyStoreLoader {

    @Value("${app.keystore.path}")
    private String keystorePath;

    @Value("${app.keystore.store-password}")
    private String storePassword;

    @Value("${app.keystore.key-alias}")
    private String keyAlias;

    @Value("${app.keystore.key-password}")
    private String keyPassword;

    @Bean
    public PrivateKey privateKey() throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");

        try (InputStream is = getClass().getResourceAsStream("/signer.p12")) {
            if (is == null) {
                throw new IllegalStateException("Cannot load keystore: " + keystorePath);
            }
            ks.load(is, storePassword.toCharArray());
        }

        return (PrivateKey) ks.getKey(keyAlias, keyPassword.toCharArray());
    }

    @Bean
    public Certificate certificate() throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (InputStream is = getClass().getResourceAsStream("/signer.p12")) {
            ks.load(is, storePassword.toCharArray());
        }
        return ks.getCertificate(keyAlias);
    }
}
