package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.authdto.LoginRequest
import com.mgmtp.cfu.dto.authdto.LoginResponse
import com.mgmtp.cfu.dto.authdto.SignUpRequest
import com.mgmtp.cfu.dto.authdto.SignUpResponse
import com.mgmtp.cfu.service.impl.AuthServiceImpl
import com.mgmtp.cfu.util.SignUpValidator
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.lang.Subject

class AuthControllerSpec extends Specification {
    def authService = Mock(AuthServiceImpl)
    @Subject
    def authController = new AuthController(authService)

    def "signIn: return correct result (login success)"() {
        given:
        def correctLoginRequest =
                new LoginRequest("user", "password")
        authService.authenticate(_) >> LoginResponse.builder().accessToken("This is valid access token.").build()
        when:
        ResponseEntity<LoginResponse> response = authController.signIn(correctLoginRequest)
        then:
        response.getBody().accessToken.equals("This is valid access token.")
        response.statusCode.value() == 200
    }

    def "signIn: throw exception when request miss required field"() {
        given:
        def wrongLoginRequest =
                new LoginRequest(username, password)
        authService.authenticate(_) >> LoginResponse.builder().accessToken("This is valid access token.").build()
        when:
        authController.signIn(wrongLoginRequest)
        then:
        def e = thrown(IllegalArgumentException)
        where:
        username | password
        "asd"    | null
        null      | "pass"
        null     | null
    }


    def "RegisterUser returns bad request when signup request is invalid"() {
        given:
        def signUpRequest = new SignUpRequest(username: "user", email: "user@invalid", password: "pass!", confirmPassword: "pass!", fullname: "fullname", dateofbirth: "01/01/2025", gender: "MALE")
        SignUpValidator.validateSignUpRequest(_) >> ["Please enter a valid email address.", "Password must be at least 8 characters.", "Password must contain at least one uppercase letter.", "Password must contain at least one number.", "Date of birth cannot be a future date."]

        when:
        def response = authController.registerUser(signUpRequest)

        then:
        response.body.size() == 5
        response.body[0] == "Please enter a valid email address."
        response.body[4] == "Date of birth cannot be a future date."
    }

    def "RegisterUser returns ok response when signup request is valid"() {
        given:
        def signUpRequest = new SignUpRequest(username: "user", email: "user@mgm-tp.com", password: "Password1!", confirmPassword: "Password1!", fullname: "fullname", dateofbirth: "17/11/2003", gender: "MALE")
        def signUpResponse = new SignUpResponse(username: "user", message: "User registered successfully")

        when:
        def response = authController.registerUser(signUpRequest)

        then:
        1 * authService.handleSignUpNewUser(signUpRequest) >> signUpResponse
        response.statusCode.value() == 200
        response.body == signUpResponse
    }
}