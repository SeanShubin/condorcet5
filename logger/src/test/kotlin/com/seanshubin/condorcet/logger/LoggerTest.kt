package com.seanshubin.condorcet.logger

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoggerTest {
    @Test
    fun logOneLine() {
        // given
        val logger = TestLogger()

        // when
        logger.log("hello")

        // then
        assertEquals(listOf("hello"), logger.lines)
    }

    @Test
    fun logException() {
        // given
        val logger = TestLogger()
        val exception = RuntimeException("oops")

        // when
        logger.log(exception)

        // then
        assertTrue(logger.lines.size >= 1)
        assertTrue(logger.lines[0].startsWith("java.lang.RuntimeException: oops"))

    }

    class TestLogger : Logger {
        val lines = mutableListOf<String>()
        override fun log(lines: List<String>) {
            this.lines.addAll(lines)
        }
    }
}