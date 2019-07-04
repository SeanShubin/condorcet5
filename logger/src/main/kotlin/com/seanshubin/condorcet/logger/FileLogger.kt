package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.Clock
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class FileLogger(
        private val files: FilesContract,
        baseDir: Path,
        name: String,
        clock: Clock
) : Logger {
    private val logFile: Path

    init {
        files.createDirectories(baseDir)
        val now = clock.instant()
        val zone = clock.zone
        val zonedDateTime = ZonedDateTime.ofInstant(now, zone)
        val formattedDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedDateTime)
        val baseFileName = "$formattedDateTime-$name"
        val safeFileName = baseFileName.replace(':', '-').replace('.', '-')
        val fileName = "$safeFileName.log"
        logFile = baseDir.resolve(fileName)
    }

    override fun log(lines: List<String>) {
        files.write(logFile, lines, StandardOpenOption.APPEND, StandardOpenOption.CREATE)
    }
}
