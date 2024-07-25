package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.service.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl implements IJwtService {
    @Value("${course4u.jwt.secret.key}")
    private String secretKey;

    private SecretKey secret;

    @PostConstruct
    private void init() {
        this.secret = Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimResole) {
        final Claims claims = extractAllClaims(token);
        return claimResole.apply(claims);
    }


    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generatedClaim(String username, Collection<? extends GrantedAuthority> claims) {
        Instant now = Instant.now();
        Date issueDate = Date.from(now);
        Date expirationDate = Date.from(now.plusSeconds(60 * 60));
        return Jwts.builder()
                .claim("Authorization", claims)
                .subject(username)
                .issuer("course-for-you-backend-app")
                .audience().add("course-for-you-client-app").and()
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .signWith(secret).compact();

    }


}
