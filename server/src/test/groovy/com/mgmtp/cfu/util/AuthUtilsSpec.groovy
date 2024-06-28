package com.mgmtp.cfu.util

import com.mgmtp.cfu.entity.User
import spock.lang.Specification
import spock.lang.Subject
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

class AuthUtilsSpec extends Specification {
    @Subject
    AuthUtils authUtils
    def "getCurrentUser should return the current user from the security context"() {
        given:
        def mockAuthentication = Mock(Authentication)
        def mockSecurityContext = Mock(SecurityContext)
        SecurityContextHolder.setContext(mockSecurityContext)
        def expectedUser = User.builder().id(1).username("username").build()

        when:
        mockSecurityContext.getAuthentication() >> mockAuthentication
        mockAuthentication.getCredentials() >> expectedUser

        then:
        AuthUtils.getCurrentUser() == expectedUser
    }

    def cleanup() {
        // Clean up the SecurityContext after the test
        SecurityContextHolder.clearContext()
    }
}
