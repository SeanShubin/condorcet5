package com.seanshubin.condorcet.crypto

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PasswordUtilTest {
    @Test
    fun validPassword() {
        // given
        val password = "foo"
        val uniqueIdGenerator = Uuid4()
        val oneWayHash = Sha256Hash()
        val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
        val saltAndHash = passwordUtil.createSaltAndHash(password)

        // when
        val result = passwordUtil.validatePassword("foo", saltAndHash)

        // then
        assertTrue(result)
    }

    @Test
    fun invalidPassword() {
        // given
        val password = "foo"
        val uniqueIdGenerator = Uuid4()
        val oneWayHash = Sha256Hash()
        val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
        val saltAndHash = passwordUtil.createSaltAndHash(password)

        // when
        val result = passwordUtil.validatePassword("bar", saltAndHash)

        // then
        assertFalse(result)
    }
}