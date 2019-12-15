package com.seanshubin.condorcet.logger

import kotlin.test.Test
import kotlin.test.assertEquals

class CompositeLoggerTest {
    @Test
    fun composeLoggers() {
        // given
        val fooLogger = StubLogger()
        val barLogger = StubLogger()
        val compositeLogger = CompositeLogger(fooLogger, barLogger)

        // when
        compositeLogger.log("hello")

        // then
        assertEquals(listOf("hello"), fooLogger.lines)
        assertEquals(listOf("hello"), barLogger.lines)
    }

    class StubLogger : Logger {
        val lines = mutableListOf<String>()
        override fun log(lines: List<String>) {
            this.lines.addAll(lines)
        }
    }
}
