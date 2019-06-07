package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.addWhitespaceNoise
import kotlin.test.Test
import kotlin.test.assertEquals

class RegisterTest {
    @Test
    fun register() {
        // given
        val api = Tester.createEmpty()

        // when
        val credentials = api.register("Alice", "alice@email.com", "password")

        // then
        assertEquals(Credentials("Alice", "password"), credentials)
    }

    @Test
    fun disallowNamesDifferentOnlyInCapitalization() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alice@email.com", "password")

        // when
        val registerResult = Try { api.register("alice", "alice2@email.com", "password") }

        // then
        assertEquals(
                "User with name 'alice' already exists",
                (registerResult as Failure).exception.message)
    }

    @Test
    fun disallowEmailsDifferentOnlyInCapitalization() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alice@email.com", "password")

        // when
        val registerResult = Try { api.register("Alice2", "Alice@email.com", "password") }

        // then
        assertEquals(
                "User with email 'Alice@email.com' already exists",
                (registerResult as Failure).exception.message)
    }

    @Test
    fun registerTrimAndCollapseWhitespaceOnUserName() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice Smith".addWhitespaceNoise(), "alice@email.com", "password")
        // when

        val loginResult = api.login("Alice Smith", "password")

        // then
        assertEquals(Credentials("Alice Smith", "password"), loginResult)
    }

    @Test
    fun registerTrimAndCollapseWhitespaceOnEmail() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alicesmith@email.com".addWhitespaceNoise(), "password")

        // when
        val loginResult = api.login("alicesmith@email.com", "password")

        // then
        assertEquals(Credentials("Alice", "password"), loginResult)
    }

    @Test
    fun registerChecksForExistingEmailUsingTrimmed() {
        // given
        val api = Tester.createEmpty()
        api.register("Alice", "alice@email.com".addWhitespaceNoise(), "password")

        // when
        val registerResult = Try { api.register("Alice Smith", "alice@email.com".addWhitespaceNoise(), "password") }

        // then
        assertEquals(
                "User with email 'alice@email.com' already exists",
                (registerResult as Failure).exception.message)
    }

    @Test
    fun registerChecksForExistingNameUsingTrimmed() {
        // given
        val api = Tester.createEmpty()
        api.register("  \t\r\n  Alice\t\r\n  Smith\t\r\n  ", "alice@email.com", "password")
        // when

        val registerResult = Try { api.register("Alice Smith", "alice@email.com", "password") }

        // then
        assertEquals(
                "User with name 'Alice Smith' already exists",
                (registerResult as Failure).exception.message)
    }
}
