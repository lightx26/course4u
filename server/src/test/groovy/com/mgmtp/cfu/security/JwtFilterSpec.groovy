package com.mgmtp.cfu.security

import com.mgmtp.cfu.entity.User
import com.mgmtp.cfu.enums.Role
import com.mgmtp.cfu.repository.UserRepository
import com.mgmtp.cfu.service.impl.JwtServiceImpl
import jakarta.servlet.FilterChain
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification
import spock.lang.Subject

class JwtFilterSpec extends Specification {
    JwtServiceImpl jwtService = Mock()
    UserRepository userRepository = Mock()
    @Subject
    JwtFilter jwtFilter = new JwtFilter(jwtService, userRepository);

    def "doFilterInternal: should  authenticate user when JWT token is valid"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        MockHttpServletResponse response = new MockHttpServletResponse()
        FilterChain filterChain = new MockFilterChain()
        request.addHeader("Authorization", "BEARER this_is_jwt_token")
        jwtService.extractUsername(_) >> "username"
        userRepository.existsByUsername(_) >> true
        userRepository.findByUsername(_) >> Optional.of(User.builder().username("user").role(Role.USER).build())
        when:
        jwtFilter.doFilterInternal(request, response, filterChain)
        then:
        jwtFilter.getCurrentUser().username == "user"


    }

    def'doFilterInternal: Request don\'t have authorization header'(){
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        MockHttpServletResponse response = new MockHttpServletResponse()
        FilterChain filterChain = new MockFilterChain()
        jwtService.extractUsername(_) >> "username"
        userRepository.existsByUsername(_) >> true
        userRepository.findByUsername(_) >> Optional.of(User.builder().username("user").role(Role.USER).build())
        when:
        jwtFilter.doFilterInternal(request, response, filterChain)
        then:
        jwtFilter.getCurrentUser() == null
    }

    def'doFilterInternal: Request don\'t have authorization header'(){
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        MockHttpServletResponse response = new MockHttpServletResponse()
        FilterChain filterChain = new MockFilterChain()
        jwtService.extractUsername(_) >> "username"
        request.addHeader("Authorization", "this_is_only_jwt_token")
        userRepository.existsByUsername(_) >> true
        userRepository.findByUsername(_) >> Optional.of(User.builder().username("user").role(Role.USER).build())
        when:
        jwtFilter.doFilterInternal(request, response, filterChain)
        then:
        jwtFilter.getCurrentUser() == null
    }
}