package com.seanshubin.condorcet.logger

import kotlin.test.Test
import kotlin.test.assertEquals

class LineEmittingLoggerTest {
    @Test
    fun emitLines() {
        // given
        val emit = EmitStub()
        val lineEmittingLogger = LineEmittingLogger(emit)

        // when
        lineEmittingLogger.log("hello")
        lineEmittingLogger.log("world")

        // then
        assertEquals(listOf("hello", "world"), emit.invocations)
    }
}