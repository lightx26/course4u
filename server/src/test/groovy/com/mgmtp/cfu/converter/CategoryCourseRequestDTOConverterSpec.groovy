package com.mgmtp.cfu.converter

import com.mgmtp.cfu.dto.coursedto.CourseRequest
import spock.lang.Specification
import spock.lang.Subject

class CategoryCourseRequestDTOConverterSpec extends Specification {
    @Subject
    def converter = new CategoryCourseRequestDTOConverter()

    def 'test convert: ok - return null'() {
        when:
            def result = converter.convert("")
        then:
            result == null
    }
    def 'test convert: ok - empty list'() {
        when:
            def result = converter.convert("[]")
        then:
            result.isEmpty()
    }
    def 'test convert: ok - normal case'() {
        when:
            def result = converter.convert(source)
        then:
            result.size() == 2
            result[0].label == "label-1"
            result[0].value == "value-1"
            result[1].label == "label-2"
            result[1].value == "value-2"
        where:
            source << ["""[
                    {
                        "value": "value-1",
                        "label": "label-1"
                    },
                    {
                        "value": "value-2",
                        "label": "label-2"
                    }
            ]""", """{
                        "value": "value-1",
                        "label": "label-1"
                    },
                    {
                        "value": "value-2",
                        "label": "label-2"
                    }"""]
    }
    def 'test convert: throw exception with wrong input'() {
        when:
            converter.convert("wrong-json")
        then:
            thrown(Exception)
    }
}
