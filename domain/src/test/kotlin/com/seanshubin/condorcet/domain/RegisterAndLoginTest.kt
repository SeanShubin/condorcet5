package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.memory.api.InMemoryDb
import kotlin.test.Test
import kotlin.test.assertEquals

class RegisterAndLoginTest {
    @Test
    fun register() {
        // given
        val api = createTestApi()

        // when
        val credentials = api.register("Alice", "alice@email.com", "password")

        // then
        assertEquals(Credentials("Alice", "password"), credentials)
    }

    @Test
    fun loginWithName() {
        // given
        val api = createTestApi()
        api.register("Alice", "alice@email.com", "password")

        // when
        val credentials = api.login("Alice", "password")

        // then
        assertEquals(Credentials("Alice", "password"), credentials)
    }

    @Test
    fun loginWithEmail() {
        // given
        val api = createTestApi()
        api.register("Alice", "alice@email.com", "password")

        // when
        val credentials = api.login("alice@email.com", "password")

        // then
        assertEquals(Credentials("Alice", "password"), credentials)
    }

    @Test
    fun wrongPassword() {
        // given
        val api = createTestApi()
        api.register("Alice", "alice@email.com", "password")

        // when
        val loginResult = Try { api.login("Alice", "wrong-password") }

        // then
        assertEquals(
                "Invalid user/password combination for 'Alice'",
                (loginResult as Failure).exception.message)
    }

    @Test
    fun disallowNamesDifferentOnlyInCapitalization() {
        // given
        val api = createTestApi()
        api.register("Alice", "alice@email.com", "password")

        // when
        val registerResult = Try { api.register("alice", "alice2@email.com", "password") }

        // then
        assertEquals(
                "User named 'alice' already exists",
                (registerResult as Failure).exception.message)
    }

    @Test
    fun disallowEmailsDifferentOnlyInCapitalization() {
        // given
        val api = createTestApi()
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
        val api = createTestApi()
        api.register("  \t\r\n  Alice\t\r\n  Smith\t\r\n  ", "alice@email.com", "password")
        // when

        val loginResult = api.login("Alice Smith", "password")

        // then
        assertEquals(Credentials("Alice Smith", "password"), loginResult)
    }

    @Test
    fun registerTrimAndCollapseWhitespaceOnEmail() {
        // given
        val api = createTestApi()
        api.register("Alice", "  \t\r\n  alice  \t\r\n  smith@email.com  \t\r\n  ", "password")

        // when
        val loginResult = api.login("alice smith@email.com", "password")

        // then
        assertEquals(Credentials("Alice", "password"), loginResult)
    }

    @Test
    fun loginTrimAndCollapseWhitespaceOnUser() {
        // given
        val api = createTestApi()
        api.register("Alice Smith", "alice@email.com", "password")

        // when
        val loginResult = api.login("    alice    smith    ", "password")

        // then
        assertEquals(Credentials("Alice Smith", "password"), loginResult)
    }

    @Test
    fun loginTrimAndCollapseWhitespaceOnEmail() {
        // given
        val api = createTestApi()
        api.register("Alice", "alice smith@email.com", "password")

        // when
        val loginResult = api.login("  \t\r\n  alice  \t\r\n  smith@email.com  \t\r\n  ", "password")

        // then
        assertEquals(Credentials("Alice", "password"), loginResult)
    }

    private fun createTestApi(): Api {
        val db = InMemoryDb()
        val api = ApiBackedByDb(db)
        return api
    }
}
