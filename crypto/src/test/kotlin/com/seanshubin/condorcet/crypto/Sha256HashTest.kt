package com.seanshubin.condorcet.crypto

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Sha256HashTest {
    @Test
    fun sameThingsHashSame() {
        // given
        val oneWayHash: OneWayHash =
                Sha256Hash()

        // when
        val hash1 = oneWayHash.hash("same thing")
        val hash2 = oneWayHash.hash("same thing")

        // then
        assertEquals(hash1, hash2)
    }

    @Test
    fun differentThingsHashDifferent() {
        // given
        val oneWayHash: OneWayHash =
                Sha256Hash()

        // when
        val hash1 = oneWayHash.hash("one thing")
        val hash2 = oneWayHash.hash("another thing")

        // then
        assertNotEquals(hash1, hash2)
    }
}
