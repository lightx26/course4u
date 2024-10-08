package com.mgmtp.cfu.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import spock.lang.Specification
import spock.lang.Subject

class GlobalExceptionHandlerSpec extends Specification {

    @Subject
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler()

    def "should handle BadCredentialsException with BAD_REQUEST status"() {
        given:
        def exception = new BadCredentialsException("Invalid credentials")

        when:
        def response = globalExceptionHandler.handleIllegalArgumentException(exception)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
    }

    def "should handle CourseNotFoundException with NOT_FOUND status"() {
        given:
        def exception = new CourseNotFoundException("Course not found")

        when:
        def response = globalExceptionHandler.handleCourseNotFoundException(exception)

        then:
        response.statusCode == HttpStatus.NOT_FOUND

    }

    def "should handle IllegalStateException with INTERNAL_SERVER_ERROR status"() {
        given:
        def exception = new IllegalStateException("Illegal state")

        when:
        def response = globalExceptionHandler.handleIllegalStateException(exception)

        then:
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR

    }

    def "should handle ServerErrorRuntimeException with INTERNAL_SERVER_ERROR status"() {
        given:
        def exception = new ServerErrorRuntimeException("Server error")

        when:
        def response = globalExceptionHandler.handleException(exception)

        then:
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
    }

    def "should handle AccessDeniedException with FORBIDDEN status"() {
        given:
        def exception = new AccessDeniedException("Access denied")

        when:
        def response = globalExceptionHandler.handleAccessDeniedException(exception)

        then:
        response.statusCode == HttpStatus.FORBIDDEN

    }

    def "should handle AccountExistByEmailException with CONFLICT status"() {
        given:
        def exception = new AccountExistByEmailException("Account already exists")

        when:
        def response = globalExceptionHandler.handleAccountExistByEmailException(exception)

        then:
        response.statusCode == HttpStatus.CONFLICT

    }

    def "should handle BadRequestRunTimeException with BAD_REQUEST status"() {
        given:
        def exception = new BadRequestRuntimeException("Bad request")

        when:
        def response = globalExceptionHandler.handleBadRequestRunTimeException(exception)

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
    }

    def "should handle ForbiddenException with FORBIDDEN status"() {
        given:
        def exception = new ForbiddenException("Forbidden access")

        when:
        def response = globalExceptionHandler.handleForbiddenException(exception)

        then:
        response.statusCode == HttpStatus.FORBIDDEN
    }

    def "should handle MaxUploadSizeExceededException with PAYLOAD_TOO_LARGE status"() {
        given:
        def exception = new MaxUploadSizeExceededException(1024L)

        when:
        def response = globalExceptionHandler.handleMaxSizeException(exception)

        then:
        response.statusCode.value() == 413

    }

    def "should handle RegistrationStatusNotFoundException with 400 status"(){
        given:
            def exception = new RegistrationStatusNotFoundException("BadStatus")

        when:
            def response = globalExceptionHandler.handleRegistrationStatusNotFoundException(exception)

        then:
            response.statusCode.value() == 400
    }

    def "should handle RegistrationFieldNotFoundException with 400 status"(){
        given:
            def exception = new RegistrationFieldNotFoundException("BadOrderBy")

        when:
            def response = globalExceptionHandler.handleRegistrationFieldNotFoundException(exception)

        then:
            response.statusCode.value() == 400
    }

    def "should handle DuplicateCourseException with 409 status"(){
        given:
        def exception = new DuplicateCourseException("Bad")

        when:
        def response = globalExceptionHandler.handleDuplicateCourseException(exception)

        then:
        response.statusCode.value() == 409
    }
}

