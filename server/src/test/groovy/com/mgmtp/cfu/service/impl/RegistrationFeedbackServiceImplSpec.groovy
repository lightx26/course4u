package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.entity.RegistrationFeedback
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.repository.RegistrationFeedbackRepository
import com.mgmtp.cfu.util.AuthUtils
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Subject

class RegistrationFeedbackServiceImplSpec extends Specification {
    RegistrationFeedbackRepository registrationFeedbackRepository = Mock(RegistrationFeedbackRepository);

    @Subject
    RegistrationFeedbackServiceImpl registrationFeedbackService = new RegistrationFeedbackServiceImpl(registrationFeedbackRepository)

    User currentAdmin;
    def setup() {
        // Mocking SecurityContext and Authentication
        SecurityContext securityContext = Mock(SecurityContext)
        Authentication authentication = Mock(Authentication)

        // Setting up the current user
        currentAdmin = new User(id: 1L, username: "admin")

        // Setting up the SecurityContextHolder
        SecurityContextHolder.setContext(securityContext)
        securityContext.getAuthentication() >> authentication
        authentication.getCredentials() >> currentAdmin

        // Mocking AuthUtils to return the current user
        GroovyMock(AuthUtils, global: true)
        AuthUtils.getCurrentUser() >> currentAdmin
    }

    def "should send feedback"() {
        given:
        def feedbackContent = "this is a feedback"
        def registration = new Registration(id: 1L)

        when:
        registrationFeedbackService.sendFeedback(registration, feedbackContent)

        then:
        1 * registrationFeedbackRepository.save(_) >> { RegistrationFeedback registrationFeedback ->
            registrationFeedback.comment == feedbackContent
            registrationFeedback.registration == registration
            registrationFeedback.user == currentAdmin
        }
    }
}
