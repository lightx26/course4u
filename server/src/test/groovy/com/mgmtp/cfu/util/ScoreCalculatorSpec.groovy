package com.mgmtp.cfu.util

import com.mgmtp.cfu.enums.CourseLevel
import com.mgmtp.cfu.enums.DurationUnit
import spock.lang.Specification

import java.time.ZoneId
import java.time.ZonedDateTime

class ScoreCalculatorSpec extends Specification {

        def "test calculate score"() {
            given:
            def level = CourseLevel.INTERMEDIATE
            def startTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault())
            def endTime = ZonedDateTime.of(2021, 1, 2, 12, 0, 0, 0, ZoneId.systemDefault())
            def estimatedTime = 3
            def unit = DurationUnit.DAY

            when:
            def result = ScoreCalculator.calculateScore(level, startTime, endTime, estimatedTime, unit)

            then:
            result == 1000
        }
}
