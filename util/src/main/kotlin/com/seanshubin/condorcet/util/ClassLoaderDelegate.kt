package com.seanshubin.condorcet.util

import java.io.InputStream

class ClassLoaderDelegate(private val delegate: ClassLoader) : ClassLoaderContract {
    override fun getResourceAsStream(name: String): InputStream? =
            delegate.getResourceAsStream(name)
}
