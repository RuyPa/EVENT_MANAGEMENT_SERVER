package com.mobile_app_server.security.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtUtil {

    private static final String JWT_SECRET =
            "KfWl4gkYFWy2s9YX4ko3BStUmkRjR8/4toz1Lk34DAGB8h7dGmDU6RPYbQnFcbp2TkjF6eH0N7PSM0JkQp6lPg==";
    private static final Integer ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 160;
    private static final Integer REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 7 * 24 * 60;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
    }
    public String generateJwtSecret(Authentication authentication, boolean isAccessToken) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        int expirationTime;


        log.info("Authorities raw: {}", userDetails.getAuthorities());

        userDetails.getAuthorities()
                .forEach(auth -> log.info("Authority: {}", auth.getAuthority()));

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        log.info("Extracted roles: {}", roles);


        if (isAccessToken) {
            expirationTime = ACCESS_TOKEN_EXPIRATION_TIME;
        } else {
            expirationTime = REFRESH_TOKEN_EXPIRATION_TIME;
        }


        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = this.extractUsername(token);
        return username.equals(userDetails.getUsername());
    }

    private boolean isTokenExpired(String token) {

        Date exp = Jwts.parser().setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return exp.before(new Date());
    }
}
