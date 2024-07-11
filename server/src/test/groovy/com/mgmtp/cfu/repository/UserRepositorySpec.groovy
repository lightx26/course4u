//package com.mgmtp.cfu.repository
//
//
//import com.mgmtp.cfu.entity.User
//import com.mgmtp.cfu.enums.Role
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
//import spock.lang.Specification
//
//@DataJpaTest
//class UserRepositorySpec extends Specification {
//    @Autowired
//    UserRepository userRepository
//
//    def "findByUsername: returns user when username exists"() {
//        given:
//        def user = User.builder().username("test_user")
//                .password("user_password")
//                .email("user@mgm-tp.com")
//                .role(Role.USER)
//                .build()
//        userRepository.save(user)
//        when:
//        def foundUser = userRepository.findByUsername("test_user")
//        then:
//        foundUser.isPresent()
//        foundUser.get().username == "test_user"
//    }
//
//    def "existsByUsername: returns true when username exists"() {
//        given:
//        def user = User.builder().username("test_user")
//                .password("user_password")
//                .email("user@mgm-tp.com")
//                .role(Role.USER)
//                .build()
//        userRepository.save(user)
//        when:
//        def exists = userRepository.existsByUsername("test_user")
//        then:
//        exists
//    }
//
//    def "existsByUsername: returns false when username does not exist"() {
//        given:
//        when:
//        def exists = userRepository.existsByUsername("non_existent_user")
//        then:
//        !exists
//    }
//}
