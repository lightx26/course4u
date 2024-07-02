package com.mgmtp.cfu.service.impl


import spock.lang.Specification
import spock.lang.Subject
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

class JwtServiceImplSpec extends Specification {
    @Subject
    JwtServiceImpl jwtService = new JwtServiceImpl();
    def setup() {
        jwtService = new JwtServiceImpl()
        jwtService.secretKey=UUID.randomUUID().toString()+UUID.randomUUID()
        jwtService.init()
    }

    def "should extract all claims from token"() {
        given:
        def token = Jwts.builder()
                .subject("testUser")
                .signWith(jwtService.secret)
                .compact()

        when:
        Claims claims = jwtService.extractAllClaims(token)

        then:
        claims.get("sub") == "testUser"
    }

    def "should extract specific claim from token"() {
        given:
        def token = Jwts.builder()
                .subject("testUser")
                .claim("customClaim", "customValue")
                .signWith(jwtService.secret)
                .compact()

        when:
        def customClaim = jwtService.extractClaim(token) { claims -> claims.get("customClaim", String) }

        then:
        customClaim == "customValue"
    }

    def "should extract username from token"() {
        given:
        def token = Jwts.builder()
                .subject("testUser")
                .signWith(jwtService.secret)
                .compact()

        when:
        def username = jwtService.extractUsername(token)
        then:
        username == "testUser"
    }

    def "should generate token with claims and username"() {
        given:
        def username = "testUser"
        def claims = ["ROLE_USER", "ROLE_ADMIN"]

        when:
        def token = jwtService.generatedClaim(username, claims)
        def parsedClaims = jwtService.extractAllClaims(token)
        then:
        parsedClaims.get("sub") == username
        parsedClaims.get("Authorization") == claims
        parsedClaims.getIssuer() == "course-for-you-backend-app"
        parsedClaims.getAudience().contains("course-for-you-client-app")
        parsedClaims.getExpiration().time > new Date().time
    }
}
