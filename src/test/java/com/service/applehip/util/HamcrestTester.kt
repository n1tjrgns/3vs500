package com.service.applehip.util

import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

/**
 * HamcrestTest 를 더 원활히 하고자 만든 Class
 */
open class HamcrestTester {
    fun <T> assertThat(actual : T ,  matcher : Matcher<in T>) =
            MatcherAssert.assertThat(actual, matcher)
    fun <T> assertThatEqual(actual : T, prediction : T) =
            MatcherAssert.assertThat(actual, Matchers.equalTo(prediction))
    fun assertThatIsNotNullValue(actual : Any?) =
            MatcherAssert.assertThat(actual, Matchers.notNullValue())
}