package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.memory.api.InMemoryDb
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginTest {
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
    fun wrongName() {
        // given
        val api = createTestApi()
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
    fun loginErrorShowsTrimmedName() {
        // given
        val api = createTestApi()

        // when
        val loginResult = Try { api.login("    alice    smith    ", "password") }

        // then
        assertEquals(
                "User with name or email 'alice smith' does not exist",
                (loginResult as Failure).exception.message)
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
