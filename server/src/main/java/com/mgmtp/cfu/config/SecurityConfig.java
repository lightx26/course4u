package com.mgmtp.cfu.config;

import com.mgmtp.cfu.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true,prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("api/admin/**", "api/admin").hasRole("ADMIN");
                    authorize.requestMatchers("api/accountant/**", "api/accountant").hasRole("ACCOUNTANT");
                    authorize.requestMatchers("api/auth/","api/auth/**", "/h2-console", "/h2-console/**").permitAll();
                    authorize.requestMatchers("api/thumbnail/**").permitAll();
                    authorize.requestMatchers("/swagger-ui/**", "v3/api-docs/**").permitAll();
                    authorize.requestMatchers(new AntPathRequestMatcher("/error")).permitAll() ;
                    authorize.requestMatchers("api/auth/","api/auth/**", "/h2-console/**","/h2-console" ).permitAll();
                    authorize.requestMatchers("api/avatar/**").permitAll();
                    authorize.anyRequest().authenticated();
                });
        http.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
