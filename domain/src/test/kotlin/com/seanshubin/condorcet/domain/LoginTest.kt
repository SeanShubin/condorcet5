package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.addWhitespaceNoise
import com.seanshubin.condorcet.domain.Tester.invertCapitalization
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginTest {
    @Test
    fun loginWithName() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alice@email.com", "password")

        // when
        val credentials = api.login("Alice", "password")

        // then
        assertEquals(Credentials("Alice", "password"), credentials)
    }

    @Test
    fun loginWithNameDifferentCapitalization() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alice@email.com", "password")

        // when
        val credentials = api.login("Alice".invertCapitalization(), "password")

        // then
        assertEquals(Credentials("Alice", "password"), credentials)
    }

    @Test
    fun loginWithEmail() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alice@email.com", "password")

        // when
        val credentials = api.login("alice@email.com", "password")

        // then
        assertEquals(Credentials("Alice", "password"), credentials)
    }

    @Test
    fun loginWithEmailDifferentCapitalization() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alice@email.com".invertCapitalization(), "password")

        // when
        val credentials = api.login("alice@email.com", "password")

        // then
        assertEquals(Credentials("Alice", "password"), credentials)
    }

    @Test
    fun wrongNameOrEmail() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alice@email.com", "password")

        // when
        val loginResult = Try { api.login("Bob", "password") }

        // then
        assertEquals(
                "User with name or email 'Bob' does not exist",
                (loginResult as Failure).exception.message)
    }

    @Test
    fun wrongPassword() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alice@email.com", "password")

        // when
        val loginResult = Try { api.login("Alice", "wrong-password") }

        // then
        assertEquals(
                "Invalid user/password combination for 'Alice'",
                (loginResult as Failure).exception.message)
    }

    @Test
    fun loginTrimAndCollapseWhitespaceOnUser() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice Smith", "alice@email.com", "password")

        // when
        val loginResult = api.login("alice smith".addWhitespaceNoise(), "password")

        // then
        assertEquals(Credentials("Alice Smith", "password"), loginResult)
    }

    @Test
    fun loginErrorShowsTrimmedName() {
        // given
        val api = Tester.createEmpty()

        // when
        val loginResult = Try { api.login("alice smith".addWhitespaceNoise(), "password") }

        // then
        assertEquals(
                "User with name or email 'alice smith' does not exist",
                (loginResult as Failure).exception.message)
    }

    @Test
    fun loginTrimAndCollapseWhitespaceOnEmail() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alicesmith@email.com", "password")

        // when
        val loginResult = api.login("alicesmith@email.com".addWhitespaceNoise(), "password")

        // then
        assertEquals(Credentials("Alice", "password"), loginResult)
    }
}
