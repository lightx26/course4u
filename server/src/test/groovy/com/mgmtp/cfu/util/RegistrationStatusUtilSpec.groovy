package com.mgmtp.cfu.util

import com.mgmtp.cfu.enums.RegistrationStatus
import spock.lang.Specification

class RegistrationStatusUtilSpec extends Specification {
    def "should return true if registration status is closable"() {
        expect:
        RegistrationStatusUtil.isCloseableStatus(status)
        where:
        status << [RegistrationStatus.DONE, RegistrationStatus.VERIFYING, RegistrationStatus.DOCUMENT_DECLINED, RegistrationStatus.VERIFIED]
    }

    def "should return false if registration status is not accepted"() {
        expect:
        !RegistrationStatusUtil.isCloseableStatus(status)
        where:
        status << [RegistrationStatus.DRAFT, RegistrationStatus.DISCARDED, RegistrationStatus.SUBMITTED, RegistrationStatus.DECLINED, RegistrationStatus.APPROVED, RegistrationStatus.CLOSED]
    }
}
