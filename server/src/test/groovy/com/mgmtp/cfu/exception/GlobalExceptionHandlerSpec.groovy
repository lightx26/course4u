package com.mgmtp.cfu.exception

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import spock.lang.Specification
import spock.lang.Subject

class GlobalExceptionHandlerSpec extends Specification {
    @Subject
    def globalExceptionHandler=new GlobalExceptionHandler()

    def "test handleException for InternalAuthenticationServiceException"() {
        when:
        ResponseEntity<?> response = globalExceptionHandler.handleException()

        then:
        response.statusCode.value() == 500
        response.body == '{"message":"Server encountered an internal error."}'
    }

    def "test handleIllegalArgumentException for IllegalArgumentException"() {
        given:
        Exception exception = new IllegalArgumentException("Invalid argument")

        when:
        ResponseEntity<?> response = globalExceptionHandler.handleIllegalArgumentException(exception)

        then:
        response.statusCode.value() == 400
        response.body == '{"message":"Invalid argument"}'
    }

    def "test handleIllegalArgumentException for BadCredentialsException"() {
        given:
        Exception exception = new BadCredentialsException("Bad credentials")

        when:
        ResponseEntity<?> response = globalExceptionHandler.handleIllegalArgumentException(exception)

        then:
        response.statusCode.value() == 400
        response.body == '{"message":"Bad credentials"}'
    }

    def "test handleIllegalStateException"() {
        given:
        Exception exception = new IllegalStateException("Illegal state")

        when:
        ResponseEntity<?> response = globalExceptionHandler.handleIllegalStateException(exception)

        then:
        response.statusCode.value() == 500
        response.body == '{"message":"Illegal state"}'
    }
}
