package com.mgmtp.cfu.controller

import com.mgmtp.cfu.dto.documentdto.DocumentDTO
import com.mgmtp.cfu.entity.Document
import com.mgmtp.cfu.exception.BadRequestRuntimeException
import com.mgmtp.cfu.service.DocumentService
import com.mgmtp.cfu.service.impl.DocumentServiceImpl
import com.mgmtp.cfu.util.Constant
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification
import spock.lang.Subject

import com.mgmtp.cfu.util.Constant
class DocumentControllerSpec extends Specification {
    def documentService=Mock(DocumentService)
    @Subject
    DocumentController documentController=new DocumentController(documentService)
    def "should get documents by registration id"() {
        given:
        Long registrationId = 1L
        def documents = Document.builder().id(1).build()
        documentService.getDocumentsByRegistrationId(registrationId) >> List.of(documents)
        when:
        def response=documentController.getDocument(registrationId)
        then:
        response.statusCode.value()==200

    }

    def "should throw MaxUploadSizeExceededException if file size exceeds limit"() {
        given:
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain",new byte[Constant.DOCUMENT_SIZE_LIMIT + 100])

        when:
        documentController.submitDocument([file] as MultipartFile[], [] as MultipartFile[], 1L)
        then:
        thrown(MaxUploadSizeExceededException)
    }

    def "should throw BadRequestRunTimeException if file extension is not allowed"() {
        given:
        MultipartFile file = new MockMultipartFile("file", "filename.xyz", "text/plain", new byte[100])

        when:
        documentController.submitDocument([file] as MultipartFile[], [] as MultipartFile[], 1L)

        then:
        thrown(BadRequestRuntimeException)

    }

    def "should throw BadRequestRunTimeException if id is null"() {
        given:
        MultipartFile file = new MockMultipartFile("file", "filename.pdf", "application/pdf", new byte[100])

        when:
        documentController.submitDocument([file] as MultipartFile[], [file] as MultipartFile[], null)

        then:
        thrown(BadRequestRuntimeException)
    }

    def "should throw BadRequestRunTimeException if certificates or payments array is empty"() {
        given:
        MultipartFile file = new MockMultipartFile("file", "filename.pdf", "application/pdf", new byte[100])

        when:
        documentController.submitDocument([] as MultipartFile[], [file] as MultipartFile[], 1L)

        then:
        thrown(BadRequestRuntimeException)
    }

    def "should call documentService.submitDocument when inputs are valid"() {
        given:
        MultipartFile certFile = new MockMultipartFile("certificate", "cert.pdf", "application/pdf", new byte[100])
        MultipartFile payFile = new MockMultipartFile("payment", "pay.pdf", "application/pdf", new byte[100])

        when:
        documentController.submitDocument([certFile] as MultipartFile[], [payFile] as MultipartFile[], 1L)

        then:
        1 * documentService.submitDocument([certFile] as MultipartFile[], [payFile] as MultipartFile[], 1L)
    }



}
