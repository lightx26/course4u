package com.mgmtp.cfu.util

import com.mgmtp.cfu.entity.User
import spock.lang.Specification

class EmailUtilSpec extends Specification {
    def "generateSubject: return subject"() {
        given:
        def subject = "Test Subject"
        when:
        def result = EmailUtil.generateSubject(subject)
        then:
        result == "Course4U - Test Subject"
    }

    def "generateSubject: return null"() {
        given:
        def subject = null
        when:
        def result = EmailUtil.generateSubject(subject)
        then:
        result == null
    }

    def "generateTitle: return MailContentUnit of title"() {
        given:
        def title = "title"
        when:
        def result = EmailUtil.generateTitle(title)
        then:
        result.id == "title"
        result.innerContent.get(0).getText() == "title"
        result.style == null
    }

    def "generateTitle: return null"() {
        given:
        def title = null
        when:
        def result = EmailUtil.generateTitle(title)
        then:
        result == null
    }

    def "updateTitleStyle: return MailContentUnit of title wrapper"() {
        given:
        Map<String, String> titleBackgroundColorMap = Map.of(
                "Important Deadline", "background-color: #F7C631;",
                "Registration Approved", "background-color: #50C878;",
                "Document Approved", "background-color: #50C878;",
                "Registration Declined", "background-color: #F44336;",
                "Document Declined", "background-color: #F44336;",
                "Registration Closed", "background-color: #808080;",
                "Verification of Course Documents", "background-color: #29CE1B;",
                "Welcome to Course4U!", "background-color: #861fa2;"
        )
        when:
        def result = EmailUtil.updateTitleStyle(title)
        then:
        result.id == "title-wrapper"
        result.style == titleBackgroundColorMap.get(title)

        where:
        title << ["Important Deadline", "Registration Approved", "Document Approved", "Registration Declined", "Document Declined", "Registration Closed", "Verification of Course Documents", "Welcome to Course4U!"]
    }

    def "updateTitleStyle: return null"() {
        given:
        def title = null
        when:
        def result = EmailUtil.updateTitleStyle(title)
        then:
        result == null
    }

    def "generateGreeting: return MailContentUnit of greeting"() {
        given:
        def greeting = "Dear {name},"
        def user = new User(fullName: "User")
        when:
        def result = EmailUtil.generateGreeting(greeting, user)
        then:
        result.id == "greeting"
        result.innerContent.get(0).getText() == "Dear User,"
    }

    def "generateGreeting: return null"() {
        given:
        def greeting = null
        def user = new User(fullName: "User")
        when:
        def result = EmailUtil.generateGreeting(greeting, user)
        then:
        result == null
    }

    def "generateApproveContent: return MailContentUnit of approve email"() {
        given:
        def courseName = "Course Name"
        when:
        def result = EmailUtil.generateApproveContent(courseName)
        then:
        result.id == "content"
        result.innerContent.get(0).getText() == "Congratulations! Your registration for course "
        result.innerContent.get(1).getText() == courseName
        result.innerContent.get(2).getText() == " has been approved. You can now log in and start exploring a variety of courses tailored to your interests. Happy learning!"
    }

    def "generateDeclineContent: return MailContentUnit of approve email"() {
        given:
        def courseName = "Course Name"
        when:
        def result = EmailUtil.generateDeclineContent(courseName)
        then:
        result.id == "content"
        result.innerContent.get(0).getText() == "Unfortunately! Your registration for course "
        result.innerContent.get(1).getText() == courseName
        result.innerContent.get(2).getText() == " has been declined. You can now check the information about registration carefully and then re-submit registration. Happy learning!"
    }

    def "generateCloseContent: return MailContentUnit of approve email"() {
        given:
        def courseName = "Course Name"
        when:
        def result = EmailUtil.generateCloseContent(courseName)
        then:
        result.id == "content"
        result.innerContent.get(0).getText() == "Your registration for course "
        result.innerContent.get(1).getText() == courseName
        result.innerContent.get(2).getText() == " has been closed. You can review the reasons below:"
        result.innerContent.get(3).getName() == "ul"
        result.innerContent.get(4).getText() == "We encourage you to review the registration process carefully. If you wish to register again in the future, please ensure all documents are accurate and complete. Thank you for your understanding, and we hope to assist you in your learning journey soon!"
    }

    def "generateNotifyAccountantContent: return MailContentUnit of notify accountant email"() {
        given:
        def content = "content"
        when:
        def result = EmailUtil.generateNotifyAccountantContent(content)
        then:
        result.id == "content"
        result.innerContent.get(0).getText() == content
    }

    def "generateWelcomeContent: return MailContentUnit of welcome email"() {
        when:
        def result = EmailUtil.generateWelcomeContent()
        then:
        result.id == "content"
        result.innerContent.get(0).getText() == "Welcome to Course4U! Thank you for using our application. Discover and enroll in a variety of courses tailored to your interests. Happy learning!"
    }

    def "generateDeadlineEmailContent: return MailContentUnit of deadline email"() {
        given:
        def courseName = "Course Name"
        def deadline = "2021-12-31"
        when:
        def result = EmailUtil.generateDeadlineEmailContent(courseName, deadline)
        then:
        result.id == "content"
        result.innerContent.get(0).getText() == "The deadline for the "
        result.innerContent.get(1).getText() == courseName
        result.innerContent.get(2).getText() == " course is approaching. Please make sure to complete all necessary tasks and submit your work by the due date."
        result.innerContent.get(3).getText() == "Deadline: " + deadline + " ICT."
    }

    def "generateDocumentDeclinedContent: return MailContentUnit of document declined email"() {
        given:
        def message = "message"
        when:
        def result = EmailUtil.generateDocumentDeclinedContent(message)
        then:
        result.id == "content"
        result.innerContent.get(0).getText() == message
    }
}
