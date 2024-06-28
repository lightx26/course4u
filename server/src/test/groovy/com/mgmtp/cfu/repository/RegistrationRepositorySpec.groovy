package com.mgmtp.cfu.repository

import com.mgmtp.cfu.entity.Registration
import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.RegistrationStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification
@DataJpaTest
class RegistrationRepositorySpec extends Specification {
    @Autowired
    UserRepository userRepository
    @Autowired
    RegistrationRepository registrationRepository

    def'findByUserId: return correct value.'(){
        given:
        def user= User.builder().id(1).build()
        userRepository.save(user)
        registrationRepository.save(Registration.builder().id(1).status(RegistrationStatus.APPROVED).user(user).build())
        registrationRepository.save(Registration.builder().id(2).status(RegistrationStatus.DRAFT).user(user).build())
        when:
        def registrations=registrationRepository.findByUserId(user.id)
        then:
        registrations.size()==2
    }
}
