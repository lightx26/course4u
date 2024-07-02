package com.mgmtp.cfu.security

import com.mgmtp.cfu.enums.Role
import org.springframework.security.authentication.InternalAuthenticationServiceException
import spock.lang.Specification
import spock.lang.Subject

class MyUserDetailsSpec extends Specification {
    @Subject
    MyUserDetails userDetails=new MyUserDetails("user", "password", "abc@mgm-tp.com",Role.USER);

    def 'getAuthorities: thrown InternalAuthenticationServiceException(\'Role not set\')'(){
        given:
        userDetails.setRole(null)
        when:
        userDetails.getAuthorities()
        then:
        def e= thrown(InternalAuthenticationServiceException)
        e.message=='Role not set'
    }

    def'getAuthorities: return SimpleGrantedAuthority list'(){
        given:
        userDetails.role >> Role.USER
        when:
        var authorities=userDetails.getAuthorities()
        then:
        authorities.size()==1
        authorities.getAt(0).authority.equals("ROLE_"+Role.USER.name())

    }

    def'isAccountNonExpired: true'(){
        given:
        when:
        var isAccountNonExpired=userDetails.isAccountNonExpired();
        then:
        isAccountNonExpired==true
    }
    def'isAccountNonLocked: true'(){
        given:
        when:
        var isAccountNonLocked=userDetails.isAccountNonLocked();
        then:
        isAccountNonLocked==true
    }
    def'isCredentialsNonExpired: true'(){
        given:
        when:
        var isCredentialsNonExpired=userDetails.isCredentialsNonExpired();
        then:
        isCredentialsNonExpired==true
    }
    def'isEnabled: true'(){
        given:
        when:
        var isEnabled=userDetails.isEnabled();
        then:
        isEnabled==true
    }


}
