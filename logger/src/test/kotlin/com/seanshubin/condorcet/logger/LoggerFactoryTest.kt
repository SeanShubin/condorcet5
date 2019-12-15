package com.seanshubin.condorcet.logger

import java.nio.file.Paths
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals

class LoggerFactoryTest {
    @Test
    fun createLogger() {
        // given
        val zoneId = ZoneId.of("America/Los_Angeles")
        val clock = ClockStub(zoneId, 1000)
        val files = FilesStub()
        val emit = EmitStub()
        val path = Paths.get("the-path")
        val name = "the-name"
        val loggerFactory = LoggerFactory(clock, files, emit)

        // when
        val logger = loggerFactory.createLogger(path, name)
        logger.log("hello")

        // then
        assertEquals(listOf("hello"), emit.invocations)
        assertEquals(2, files.whatHappened.size)
        assertEquals(
                "createDirectories(dir = the-path, attrs = [])",
                files.whatHappened[0])
        assertEquals(
                "write(path = the-path/1969-12-31T16-00-01-08-00-the-name, lines = [hello], options = [APPEND, CREATE])",
                files.whatHappened[1])
    }

    @Test
    fun createLogGroup() {
        // given
        val zoneId = ZoneId.of("America/Los_Angeles")
        val clock = ClockStub(zoneId, 1000)
        val files = FilesStub()
        val emit = EmitStub()
        val baseDir = Paths.get("base-dir")
        val name = "the-name"
        val loggerFactory = LoggerFactory(clock, files, emit)

        // when
        val logGroup = loggerFactory.createLogGroup(baseDir)
        val logger = logGroup.create(name)

        logger.log("hello")

        // then
        assertEquals(listOf("hello"), emit.invocations)
        assertEquals(2, files.whatHappened.size)
        assertEquals(
                "createDirectories(dir = base-dir/1969-12-31T16-00-01-08-00, attrs = [])",
                files.whatHappened[0])
        assertEquals(
                "write(path = base-dir/1969-12-31T16-00-01-08-00/the-name.log, lines = [hello], options = [APPEND, CREATE])",
                files.whatHappened[1])
    }
}
