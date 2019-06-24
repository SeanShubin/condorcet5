package com.seanshubin.condorcet.util

import java.io.*
import java.nio.charset.Charset

object IoUtil {
    fun OutputStream.consume(inputStream: InputStream) {
        var byte = inputStream.read()
        while (byte != -1) {
            write(byte)
            byte = inputStream.read()
        }
    }

    fun Writer.consume(reader: Reader) {
        var char = reader.read()
        while (char != -1) {
            write(char)
            char = reader.read()
        }
    }

    fun InputStream.toByteArray(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        outputStream.consume(this)
        return outputStream.toByteArray()
    }

    fun Reader.consumeToString(): String {
        val writer = StringWriter()
        writer.consume(this)
        return writer.buffer.toString()
    }

    fun ByteArray.toInputStream(): InputStream = ByteArrayInputStream(this)

    fun String.toInputStream(charset: Charset): InputStream = toByteArray(charset).toInputStream()

    fun InputStream.toString(charset: Charset): String = String(toByteArray(), charset)

    fun String.toReader(): Reader = StringReader(this)

    fun OutputStream.consume(s: String, charset: Charset) {
        write(s.toByteArray(charset))
    }

    fun ByteArray.toString(charset: Charset): String = String(this, charset)

    fun OutputStream.consume(bytes: ByteArray) {
        consume(bytes.toInputStream())
    }
}
