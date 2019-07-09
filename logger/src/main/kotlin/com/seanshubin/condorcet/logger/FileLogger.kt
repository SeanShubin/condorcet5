package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class FileLogger(
        private val files: FilesContract,
        private val logFile: Path
) : Logger {
    override fun log(lines: List<String>) {
        files.write(logFile, lines, StandardOpenOption.APPEND, StandardOpenOption.CREATE)
    }
}
