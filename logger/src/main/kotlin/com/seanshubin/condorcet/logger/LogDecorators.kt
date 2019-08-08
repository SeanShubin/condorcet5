package com.seanshubin.condorcet.logger

object LogDecorators {
    fun logSql(logger: Logger): (String) -> Unit = { logger.log("${it.trim()};") }
}
