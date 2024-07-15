import com.mgmtp.cfu.controller.OpenGraphController
import com.mgmtp.cfu.service.OpenGraphService
import spock.lang.Subject
import spock.lang.Specification
import spock.mock.DetachedMockFactory

class OpenGraphControllerSpec extends Specification {
    @Subject
    OpenGraphService openGraphService = Mock()

    @Subject
    OpenGraphController openGraphController = new OpenGraphController(openGraphService: openGraphService)

    def "test getCourseInfo endpoint"() {
        given:
        def url = "https://example.com"
        def mockResult = ["title": "Example Title", "image": "https://example.com/image.jpg"]

        openGraphService.getCourseInfo(url) >> mockResult

        when:
        def result = openGraphController.getCourseInfo(url)

        then:
        result == mockResult
    }
}
