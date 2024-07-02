package com.mgmtp.cfu.service.impl

import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.Role
import com.mgmtp.cfu.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification
import spock.lang.Subject

class UserDetailServiceImplSpec extends Specification {
    def repository = Mock(UserRepository) {
        findByUsername("user") >>  Optional.of(User.builder().username("user")
                .role(Role.USER)
                .email("user@mgm-tp.com").build())
        findByUsername("admin") >>Optional.of(User.builder().username("admin")
                .role(Role.ADMIN)
                .email("admin@mgm-tp.com").build())
        findByUsername("accountant") >> Optional.of(User.builder().username("accountant")
                .role(Role.ACCOUNTANT)
                .email("accountant@mgm-tp.com").build())
        findByUsername(_)>>Optional.empty()

    }

    @Subject
    def UserDetailsServiceImpl detailService=new UserDetailsServiceImpl(repository)

    def "loadUserByUsername: return ok"() {
        given:
        when:
        def user = detailService.loadUserByUsername(username)
        then:
        user != null;
        user instanceof UserDetails;
        user.username.length() > 0
        user.accountNonExpired
        user.credentialsNonExpired
        user.accountNonLocked
        user.enabled
        user.authorities[0].getAuthority() == role;
        where:
        username     | role
        "user"       | "ROLE_USER"
        "admin"      | "ROLE_ADMIN"
        "accountant" | "ROLE_ACCOUNTANT"
    }


    def "loadUserByUsername: throw UsernameNotFoundException"(){
        given:
        when:
        def user = detailService.loadUserByUsername(username)
        then:
        def e=thrown(UsernameNotFoundException)
        e.message=="User not found"
        where:
        username     | role
        "failUser"       | "ROLE_USER"
    }

}
