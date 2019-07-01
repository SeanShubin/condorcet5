package com.seanshubin.condorcet.util

import java.io.InputStream

interface ClassLoaderContract {
    fun getResourceAsStream(name: String): InputStream?
}
