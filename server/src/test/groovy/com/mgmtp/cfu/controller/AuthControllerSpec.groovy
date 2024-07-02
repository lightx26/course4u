package com.mgmtp.cfu.controller

import com.mgmtp.cfu.DTO.LoginRequest
import com.mgmtp.cfu.DTO.LoginResponse
import com.mgmtp.cfu.service.impl.AuthServiceImpl
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
        authService.authenticate(_) >> LoginResponse.builder().accessToken("This is valid access token.").build();
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
        authService.authenticate(_) >> LoginResponse.builder().accessToken("This is valid access token.").build();
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
}