package com.seanshubin.condorcet.dbutil

import com.seanshubin.condorcet.util.ClassLoaderUtil

object MysqlConstants {
    val keywords: Set<String>
    val reservedWords: Set<String>

    init {
        val keywordsString = ClassLoaderUtil.loadResourceAsString("mysql-keywords.txt")
        keywords = keywordsString.split("\n").map { it.toLowerCase() }.toSet()
        val reservedWordsString = ClassLoaderUtil.loadResourceAsString("mysql-reserved-words.txt")
        reservedWords = reservedWordsString.split("\n").map { it.toLowerCase() }.toSet()
    }
}
