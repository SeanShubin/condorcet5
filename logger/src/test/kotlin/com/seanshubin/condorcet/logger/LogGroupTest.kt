package com.seanshubin.condorcet.logger

import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class LogGroupTest {
    @Test
    fun createLogger() {
        // given
        val emit = EmitStub()
        val files = FilesStub()
        val baseDir = Paths.get("base-dir")
        val logGroup = LogGroup(emit, files, baseDir)

        // when
        val logger = logGroup.create("log-name")
        logger.log("hello")

        // then
        assertEquals(listOf("hello"), emit.invocations)
        assertEquals(2, files.whatHappened.size)
        assertEquals("createDirectories(dir = base-dir, attrs = [])", files.whatHappened[0])
        assertEquals("write(path = base-dir/log-name.log, lines = [hello], options = [APPEND, CREATE])", files.whatHappened[1])
    }
}