package com.mgmtp.cfu.service.impl

import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path

class UploadServiceImplSpec extends Specification {

    UploadServiceImpl uploadService = new UploadServiceImpl()

    @TempDir
    Path tempDir

    def "should upload thumbnail and return filename with correct extension"() {
        given:
        MultipartFile thumbnail = Mock(MultipartFile)
        String originalFilename = "image.png"
        thumbnail.getOriginalFilename() >> originalFilename
        thumbnail.isEmpty() >> false
        byte[] content = "dummy image content".getBytes()
        thumbnail.getBytes() >> content

        when:
        String result = uploadService.uploadThumbnail(thumbnail, tempDir.toString())

        then:
        result.endsWith(".jpg")
    }
    def "should return UUID filename with .jpg extension when thumbnail is null"() {
        when:
        String result = uploadService.uploadThumbnail(null, tempDir.toString())

        then:
        result.endsWith(".jpg")
    }

    def "should return UUID filename with .jpg extension when thumbnail is empty"() {
        given:
        MultipartFile thumbnail = Mock(MultipartFile)
        thumbnail.isEmpty() >> true

        when:
        String result = uploadService.uploadThumbnail(thumbnail, tempDir.toString())

        then:
        result.endsWith(".jpg")
    }


}
