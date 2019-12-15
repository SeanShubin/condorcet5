package com.seanshubin.condorcet.logger

import java.io.PrintWriter
import java.io.StringWriter

interface Logger {
    fun log(lines: List<String>)

    fun log(line: String) {
        log(listOf(line))
    }

    fun log(exception: Throwable) {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        exception.printStackTrace(printWriter)
        log(stringWriter.buffer.toString())
    }
}
