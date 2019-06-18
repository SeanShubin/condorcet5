package com.seanshubin.condorcet.dbutil

fun main() {
    Schema.tables.flatMap { it.toSql() }.forEach(::println)
}