package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import java.nio.file.Path

class LogGroup(
        private val emit: (String) -> Unit,
        private val files: FilesContract,
        private val baseDir: Path
) {
    fun create(name: String, ext: String = "log"): Logger {
        val initialize: () -> Unit = {
            files.createDirectories(baseDir)
        }
        return LineEmittingAndFileLogger(initialize, emit, files, baseDir.resolve("$name.$ext"))
    }
}
