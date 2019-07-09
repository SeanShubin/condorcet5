package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import java.nio.file.Path

class LogDirectory(
        private val files: FilesContract,
        private val baseDir: Path) {
    fun create(name: String): Logger =
            FileLogger(files, baseDir.resolve(name))
}