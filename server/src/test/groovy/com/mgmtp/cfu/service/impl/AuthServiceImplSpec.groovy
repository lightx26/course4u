package com.mgmtp.cfu.service.impl


import com.mgmtp.cfu.dto.authdto.LoginRequest
import com.mgmtp.cfu.dto.authdto.SignUpRequest
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.Gender
import com.mgmtp.cfu.enums.Role
import com.mgmtp.cfu.repository.UserRepository
import com.mgmtp.cfu.service.IEmailService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification
import spock.lang.Subject
import com.mgmtp.cfu.exception.AccountExistByEmailException
import java.time.LocalDate

class AuthServiceImplSpec extends Specification {

    def authenticationManager = Mock(AuthenticationManager)
    def passwordEncoder = new BCryptPasswordEncoder()
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
    IEmailService emailService=Mock(EmailServiceImpl);

    @Subject
    AuthServiceImpl authService = new AuthServiceImpl(authenticationManager, jwtService, userRepository,emailService)

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


    def "handleSignUpNewUser returns response indicating email already in use"() {
        given:
        def signUpRequest = new SignUpRequest(username: "user", email: "user@mgm-tp.com", password: "Password1!", confirmPassword: "Password1!", fullname: "fullname", dateofbirth: "01/01/2000", gender: "MALE")
        userRepository.findAllByEmail(signUpRequest.getEmail()) >> [
                User.builder().email("user@mgm-tp.com").role(Role.USER).build()
        ]

        when:
        authService.handleSignUpNewUser(signUpRequest)

        then:
        def e = thrown(AccountExistByEmailException)
        e.message == "Email is already in use."
    }


    def "handleSignUpNewUser successfully registers a new user"() {
        given:
        def signUpRequest = new SignUpRequest(username: "newuser", email: "newuser@mgm-tp.com", password: "Password1!", confirmPassword: "Password1!", fullname: "New User", dateofbirth: "01/01/2000", gender: "MALE")
        def user = User.builder()
                .username("newuser")
                .password(passwordEncoder.encode("Password1!"))
                .fullName("New User")
                .email("newuser@mgm-tp.com")
                .dateOfBirth(LocalDate.parse("2000-01-01"))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build()

        userRepository.findAllByEmail(signUpRequest.getEmail()) >> []
        userRepository.save(_) >> user

        when:
        def response = authService.handleSignUpNewUser(signUpRequest)

        then:
        response.message == "User registered successfully"
        response.username == "newuser"
    }

}