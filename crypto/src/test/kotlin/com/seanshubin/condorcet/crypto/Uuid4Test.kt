package com.seanshubin.condorcet.crypto

import org.junit.Test
import kotlin.test.assertNotEquals

class Uuid4Test {
    @Test
    fun unique() {
        // given
        val uniqueIdGenerator = Uuid4()

        // when
        val id1 = uniqueIdGenerator.uniqueId()
        val id2 = uniqueIdGenerator.uniqueId()

        // then
        assertNotEquals(id1, id2)
    }
}
