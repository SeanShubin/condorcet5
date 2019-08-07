package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import com.seanshubin.condorcet.contract.FilesDelegate
import java.nio.file.Path
import java.time.Clock
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object LoggerFactory {
    fun createLogger(path: Path, name: String): Logger {
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

    fun createLogGroup(baseDir: Path): LogGroup {
        val files: FilesContract = FilesDelegate
        val clock = Clock.systemDefaultZone()
        val now = clock.instant()
        val zone = clock.zone
        val zonedDateTime = ZonedDateTime.ofInstant(now, zone)
        val formattedDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedDateTime)
        val logDir = baseDir.resolve(formattedDateTime.replace(':', '-').replace('.', '-'))
        val emit: (String) -> Unit = ::println
        return LogGroup(emit, files, logDir)
    }
}
