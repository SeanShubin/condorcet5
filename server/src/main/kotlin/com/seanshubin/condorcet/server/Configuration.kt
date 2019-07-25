package com.seanshubin.condorcet.server

import java.nio.file.Path

data class Configuration(val port: Int,
                         val logFileBaseDir: Path)
