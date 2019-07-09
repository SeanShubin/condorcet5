package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import java.nio.file.Path

class ConsoleAndFileLogger(emit: (String) -> Unit,
                           files: FilesContract,
                           logFile: Path) :
        Logger {
    private val delegate = CompositeLogger(
            LineEmittingLogger(emit),
            FileLogger(files, logFile)
    )

    override fun log(lines: List<String>) {
        delegate.log(lines)
    }
}
