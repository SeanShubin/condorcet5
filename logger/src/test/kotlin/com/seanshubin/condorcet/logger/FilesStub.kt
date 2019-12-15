package com.seanshubin.condorcet.logger

import com.seanshubin.condorcet.contract.FilesContractNotImplemented
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.attribute.FileAttribute

class FilesStub : FilesContractNotImplemented {
    val whatHappened = mutableListOf<String>()
    override fun createDirectories(dir: Path, vararg attrs: FileAttribute<*>): Path {
        whatHappened.add("createDirectories(dir = $dir, attrs = ${attrs.toList()})")
        return dir
    }

    override fun write(path: Path, lines: Iterable<CharSequence>, vararg options: OpenOption): Path {
        whatHappened.add("write(path = $path, lines = $lines, options = ${options.toList()})")
        return path
    }
}