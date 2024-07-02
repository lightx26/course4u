package com.mgmtp.cfu.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.function.Function;

public interface IJwtService {
    Claims extractAllClaims(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimResole);

    String extractUsername(String token);

    String generatedClaim(String username, Collection<? extends GrantedAuthority> claims);
}
