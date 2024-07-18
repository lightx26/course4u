package com.mgmtp.cfu.util

import com.mgmtp.cfu.enums.DocumentType
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Path

class DocumentUtilSpec extends Specification {
    @TempDir
    Path tempDir

    def "storageDocument should store file and return correct path"() {
        given:
        def file = new MockMultipartFile("file", "original.txt", "text/plain", "File content".bytes)
        def storageDir = tempDir.toString()
        def type = DocumentType.CERTIFICATE// Replace with actual enum value

        when:
        def resultPath = DocumentUtils.storageDocument(type, file, storageDir)

        then:
        resultPath.startsWith(storageDir)
        resultPath.contains(type.name())
    }
}
