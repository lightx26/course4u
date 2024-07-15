package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.service.OpenGraphService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import spock.lang.Specification

class OpenGraphServiceImplSpec extends Specification {

    OpenGraphService openGraphService = new OpenGraphServiceImpl()
    MockWebServer mockWebServer = new MockWebServer()

    def setup() {
        mockWebServer.start()
    }

    def cleanup() {
        mockWebServer.shutdown()
    }

    def "test getCourseInfo with valid URL"() {
        given: "a valid URL returning Open Graph meta tags"
        String url = mockWebServer.url("/course").toString()
        String html = """
            <html>
                <head>
                    <meta property="og:title" content="Course Title">
                    <meta property="og:description" content="Course Description">
                    <meta property="og:image" content="http://example.com/image.jpg">
                </head>
                <body></body>
            </html>
        """
        mockWebServer.enqueue(new MockResponse().setBody(html).setResponseCode(200))

        when: "getCourseInfo is called"
        Map<String, String> result = openGraphService.getCourseInfo(url)

        then: "the Open Graph data is returned correctly"
        result.size() == 3
        result.get("title") == "Course Title"
        result.get("description") == "Course Description"
        result.get("image") == "http://example.com/image.jpg"
    }

    def "test getCourseInfo with invalid URL"() {
        given: "an invalid URL"
        String url = "http://invalid.url"

        when: "getCourseInfo is called"
        Map<String, String> result = openGraphService.getCourseInfo(url)

        then: "an empty map is returned"
        result.isEmpty()
    }
}
