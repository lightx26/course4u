package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.dto.MailContentUnit
import com.mgmtp.cfu.service.IResourceService
import jakarta.mail.internet.MimeMessage
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender
import spock.lang.Specification
import spock.lang.Subject

class EmailServiceImplSpec extends Specification {
    JavaMailSender emailSender = Mock();
    IResourceService resourceService = Mock(ResourceServiceImpl)
    @Subject
    EmailServiceImpl emailService = new EmailServiceImpl(emailSender, resourceService)

    def "SendMessage: return ok"() {
        given:
        def to = "test@example.com"
        def subject = "Test Subject"
        def templateName = "email-template.xml"
        emailService.isEnableEmailSender = true
        emailService.emailAddress = ""
        List<MailContentUnit> mailContentUnits = List.of(
                MailContentUnit.builder().id("title").build(),
        );
        Resource mockResource = new ClassPathResource("email/template/email-template.xml")
        resourceService.loadResourceByName(_ as String) >> (mockResource)
        MimeMessage message = Mock(MimeMessage)
        emailSender.createMimeMessage() >> message
        when:
        long startTime = System.currentTimeMillis()
        emailService.sendMail(to, subject, templateName, mailContentUnits)
        long endTime = System.currentTimeMillis()
        then:
        endTime - startTime < 11000
        mockResource != null
    }

    def "SendMessage: return no ok"() {
        given:
        def to = "test@example.com"
        def subject = "Test Subject"
        def templateName = "email-template.xml"
        emailService.isEnableEmailSender = true
        emailService.emailAddress = ""
        List<MailContentUnit> mailContentUnits = List.of(
                MailContentUnit.builder().id("greeting").build(),
        );
        resourceService.loadResourceByName(_ as String) >> (null)
        MimeMessage message = Mock(MimeMessage)
        emailSender.createMimeMessage()>>message
        when:
        long startTime = System.currentTimeMillis()
        emailService.sendMail(to, subject, templateName, mailContentUnits)
        long endTime = System.currentTimeMillis()
        then:
        endTime - startTime < 11000
    }
}