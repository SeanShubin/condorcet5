package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import com.seanshubin.condorcet.contract.FilesDelegate
import java.nio.file.Path
import java.time.Clock
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object LoggerFactory {
    fun create(path: Path, name: String): Logger {
        val clock = Clock.systemDefaultZone()
        val files: FilesContract = FilesDelegate
        val emit: (String) -> Unit = ::println
        val now = clock.instant()
        val zone = clock.zone
        val zonedDateTime = ZonedDateTime.ofInstant(now, zone)
        val formattedDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedDateTime)
        val fileName = formattedDateTime.replace(':', '-').replace('.', '-') + "-$name"
        files.createDirectories(path)
        val logFile = path.resolve(fileName)
        return ConsoleAndFileLogger(emit, files, logFile)
    }

    fun createDirectory(baseDir: Path): LogDirectory {
        val files: FilesContract = FilesDelegate
        val clock = Clock.systemDefaultZone()
        val now = clock.instant()
        val zone = clock.zone
        val zonedDateTime = ZonedDateTime.ofInstant(now, zone)
        val formattedDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedDateTime)
        val baseDir = baseDir.resolve(formattedDateTime.replace(':', '-').replace('.', '-'))
        return LogDirectory(files, baseDir)
    }
}
