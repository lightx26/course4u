package com.mgmtp.cfu.security;

import com.mgmtp.cfu.entity.User;
import com.mgmtp.cfu.repository.UserRepository;
import com.mgmtp.cfu.service.IJwtService;
import io.jsonwebtoken.lang.Objects;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    private final UserRepository userRepository;
    private User currentUser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        List<String> headers = Collections.list(request.getHeaderNames());
        String token=null;
        if(headers.stream().anyMatch(header->header.equalsIgnoreCase("Authorization"))) {
            token = request.getHeader("Authorization");
        }
        if (token != null && (token.toUpperCase().startsWith("BEARER "))) {
            String jwt = token.substring(7);
            String username = null;
            try {
                username=jwtService.extractUsername(jwt);
            }catch (Exception e) {
                log.error(e.getMessage());
            }
            if (username != null && !Objects.isEmpty(username) && userRepository.existsByUsername(username)) {
                currentUser = userRepository.findByUsername(username).get();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, currentUser,
                                List.of(new SimpleGrantedAuthority("ROLE_" + currentUser.getRole().name())));
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    public User getCurrentUser() {
        return currentUser;
    }

}
