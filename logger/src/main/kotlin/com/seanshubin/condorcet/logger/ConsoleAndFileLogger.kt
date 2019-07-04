package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContract
import java.nio.file.Path
import java.time.Clock

class ConsoleAndFileLogger(emit: (String) -> Unit, files: FilesContract, baseDir: Path, name: String, clock: Clock) :
        Logger {
    private val delegate = CompositeLogger(
            LineEmittingLogger(emit),
            FileLogger(files, baseDir, name, clock)
    )

    override fun log(lines: List<String>) {
        delegate.log(lines)
    }
}
