package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import com.seanshubin.condorcet.contract.FilesDelegate
import java.nio.file.Path
import java.time.Clock

object LoggerFactory {
    fun create(path: Path, name: String): Logger {
        val clock = Clock.systemDefaultZone()
        val files: FilesContract = FilesDelegate
        val emit: (String) -> Unit = ::println
        return ConsoleAndFileLogger(emit, files, path, name, clock)
    }
}
