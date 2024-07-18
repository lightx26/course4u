package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.DocumentType
import com.mgmtp.cfu.enums.RegistrationStatus
import com.mgmtp.cfu.enums.Role
import com.mgmtp.cfu.exception.BadRequestRunTimeException
import com.mgmtp.cfu.exception.ForbiddenException
import com.mgmtp.cfu.repository.DocumentRepository
import com.mgmtp.cfu.repository.NotificationRepository
import com.mgmtp.cfu.repository.RegistrationRepository
import com.mgmtp.cfu.repository.UserRepository
import com.mgmtp.cfu.service.IEmailService
import com.mgmtp.cfu.util.DocumentUtils
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Subject
import spock.lang.TempDir

import java.nio.file.Path

class DocumentServiceImplSpec extends Specification {

    DocumentRepository documentRepository = Mock()
    RegistrationRepository registrationRepository = Mock()
    IEmailService emailService = Mock()
    UserRepository userRepository = Mock()
    NotificationRepository notificationRepository = Mock()
    @TempDir
    Path tempDir

    @Subject
    DocumentServiceImpl documentService=new DocumentServiceImpl(documentRepository,registrationRepository,emailService,userRepository,notificationRepository)

    def setup() {
        documentService = new DocumentServiceImpl(
                documentRepository, registrationRepository, emailService,
                userRepository, notificationRepository
        )
        def authentication = Mock(Authentication) {
            getCredentials() >> new User(id: 1L, username: "user1")
        }
        SecurityContextHolder.context.authentication = authentication

    }

    def cleanup() {
        SecurityContextHolder.context.authentication = null

    }


    def "test submitDocument with valid inputs"() {
        given:
        Long id = 1L
        MultipartFile certificateFile = Mock(MultipartFile)
        certificateFile.getOriginalFilename() >> "originalFilename.pdf"
        certificateFile.getSize() >> 1
        MultipartFile paymentFile = Mock(MultipartFile)
        paymentFile.getOriginalFilename() >> "originalFilename.pdf"
        paymentFile.getSize() >> 1
        MultipartFile[] certificates = [certificateFile]
        MultipartFile[] payments = [paymentFile]
        certificateFile.transferTo(_ as File)>>{};
        paymentFile.transferTo(_ as File)>>{};
        User user = new User(id: 1L, username: "user1")
        Registration registration = new Registration(id: id, user: user, status: RegistrationStatus.DONE)

        registrationRepository.findById(id) >> Optional.of(registration)
        userRepository.findAllByRole(Role.ACCOUNTANT) >> [new User(id: 2L, username: "accountant1", email: "acc1@example.com")]

        when:
        documentService.setDocumentStorageDir(tempDir.toString())
        documentService.submitDocument(certificates, payments, id)

        then:
        1 * notificationRepository.saveAll(_)
        1 * emailService.sendMessage(_, _, _, _)

    }


    def "test submitDocument when registration is not found"() {
        given:
        Long id = 1L
        MockMultipartFile certificateFile = new MockMultipartFile("certificate", "certificate.pdf", "application/pdf", "dummy".bytes)
        MockMultipartFile paymentFile = new MockMultipartFile("payment", "payment.pdf", "application/pdf", "dummy".bytes)
        MultipartFile[] certificates = [certificateFile]
        MultipartFile[] payments = [paymentFile]

        registrationRepository.findById(id) >> Optional.empty()

        when:
        documentService.setDocumentStorageDir(tempDir.toString())
        documentService.submitDocument(certificates, payments, id)

        then:
        thrown(BadRequestRunTimeException)
    }

    def "test submitDocument when user has no permission"() {
        given:
        Long id = 1L
        MockMultipartFile certificateFile = new MockMultipartFile("certificate", "certificate.pdf", "application/pdf", "dummy".bytes)
        MockMultipartFile paymentFile = new MockMultipartFile("payment", "payment.pdf", "application/pdf", "dummy".bytes)
        MultipartFile[] certificates = [certificateFile]
        MultipartFile[] payments = [paymentFile]
        User user = new User(id: 1L, username: "user1")
        User otherUser = new User(id: 2L, username: "user2")
        Registration registration = new Registration(id: id, user: otherUser, status: RegistrationStatus.DONE)

        registrationRepository.findById(id) >> Optional.of(registration)

        when:
        documentService.setDocumentStorageDir(tempDir.toString())
        documentService.submitDocument(certificates, payments, id)

        then:
        thrown(ForbiddenException)
    }

    def "test submitDocument when registration status is not DONE"() {
        given:
        Long id = 1L
        MockMultipartFile certificateFile = new MockMultipartFile("certificate", "certificate.pdf", "application/pdf", "dummy".bytes)
        MockMultipartFile paymentFile = new MockMultipartFile("payment", "payment.pdf", "application/pdf", "dummy".bytes)
        MultipartFile[] certificates = [certificateFile]
        MultipartFile[] payments = [paymentFile]
        User user = new User(id: 1L, username: "user1")
        Registration registration = new Registration(id: id, user: user, status: RegistrationStatus.CLOSED)

        registrationRepository.findById(id) >> Optional.of(registration)

        when:
        documentService.setDocumentStorageDir(tempDir.toString())
        documentService.submitDocument(certificates, payments, id)

        then:
        thrown(BadRequestRunTimeException)
    }
}
