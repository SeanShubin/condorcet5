package com.seanshubin.condorcet.memory.db

interface TableRow<T> {
    val primaryKey: T
    val cells: List<Any?>
}
