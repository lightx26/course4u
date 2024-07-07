package com.mgmtp.cfu.controller

import spock.lang.Specification
import com.mgmtp.cfu.controller.HelloController

public class HelloControllerSpec extends Specification {

    def 'Hello World'(){
        given:
        def controller = new HelloController()
        when:
        def result = controller.hello()
        then:
        result == "Hello World"
    }
}
