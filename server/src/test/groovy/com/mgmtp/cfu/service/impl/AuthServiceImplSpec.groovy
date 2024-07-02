package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.DTO.LoginRequest
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.Role
import com.mgmtp.cfu.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import spock.lang.Specification
import spock.lang.Subject

class AuthServiceImplSpec extends Specification {

    def authenticationManager = Mock(AuthenticationManager)
    def jwtService = Mock(JwtServiceImpl) {
        generatedClaim(_, _) >> "This is jwt token."
    }
    def userRepository = Mock(UserRepository) {
        findByUsername("user") >> Optional.of(User.builder().username("user")
                .role(Role.USER)
                .email("user@mgm-tp.com").build())
        findByUsername("admin") >> Optional.of(User.builder().username("admin")
                .role(Role.ADMIN)
                .email("admin@mgm-tp.com").build())
        findByUsername(_) >> Optional.empty()
        findByUsername("failUser") >> Optional.of(User.builder().username("failUser")
                .email("user@mgm-tp.com").build())
    }

    @Subject
    AuthServiceImpl authService = new AuthServiceImpl(authenticationManager, jwtService, userRepository)

    def "Authenticate: return ok"() {
        given:
        authenticationManager.authenticate(_) >> { args ->
            def arg = args[0] as UsernamePasswordAuthenticationToken
            if (arg.getPrincipal() == username && arg.getCredentials() == password)
                Mock(Authentication) {
                    isAuthenticated() >> true
                }
            else
                Mock(Authentication) {
                    isAuthenticated() >> false
                }
        }
        when:
        def loginResponse = authService.authenticate(new LoginRequest(username, password))
        then:
        loginResponse.accessToken.equals("This is jwt token.")
        where:
        username | password
        "user"   | "user_password"
        "admin"  | "admin_password"
    }

    def "Authenticate: thrown BadCredentialsException because isAuthenticated() is false"() {
        given:
        authenticationManager.authenticate(_) >> { args ->
            Mock(Authentication) {
                isAuthenticated() >> false
            }
        }
        when:
        def loginResponse = authService.authenticate(new LoginRequest(username, password))
        then:
        def e = thrown(BadCredentialsException)
        e.message.equals("Authentication failed: Invalid credentials")
        where:
        username   | password
        "failUser" | "user_password"
    }

}