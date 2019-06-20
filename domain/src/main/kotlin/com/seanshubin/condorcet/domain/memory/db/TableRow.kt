package com.seanshubin.condorcet.domain.memory.db

interface TableRow<T> {
    val primaryKey: T
    val cells: List<Any?>
}
