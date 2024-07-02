package com.mgmtp.cfu.service.impl;

import com.mgmtp.cfu.DTO.LoginRequest;
import com.mgmtp.cfu.DTO.LoginResponse;
import com.mgmtp.cfu.repository.UserRepository;
import com.mgmtp.cfu.service.IAuthService;
import com.mgmtp.cfu.service.IJwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;
    private final UserRepository userRepository;
    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }
        catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid username or password");
        }
        if(authentication.isAuthenticated()){
            var user=userRepository.findByUsername(loginRequest.getUsername()).get();
            var accessToken=jwtService.generatedClaim(user.getUsername(), List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name())));
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .build();
        }else {
            throw new BadCredentialsException("Authentication failed: Invalid credentials");
        }
    }
}
