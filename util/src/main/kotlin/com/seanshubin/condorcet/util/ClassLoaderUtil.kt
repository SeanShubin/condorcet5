package com.seanshubin.condorcet.util

import com.seanshubin.condorcet.util.IoUtil.toString
import java.nio.charset.StandardCharsets

object ClassLoaderUtil {
    fun loadResourceAsString(name: String): String {
        val charset = StandardCharsets.UTF_8
        val classLoader = this.javaClass.classLoader
        val inputStream = classLoader.getResourceAsStream(name)
        if (inputStream == null) {
            throw RuntimeException("Resource named '$name' not found")
        } else {
            return inputStream.toString(charset)
        }
    }
}
