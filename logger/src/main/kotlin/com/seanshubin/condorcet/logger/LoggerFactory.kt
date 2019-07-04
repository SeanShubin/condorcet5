package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import com.seanshubin.condorcet.contract.FilesDelegate
import java.nio.file.Path
import java.time.Clock

class LoggerFactory(val path: Path) {
    fun create(name: String): Logger {
        val clock = Clock.systemDefaultZone()
        val files: FilesContract = FilesDelegate
        val emit: (String) -> Unit = ::println
        return ConsoleAndFileLogger(emit, files, path, name, clock)
    }
}
