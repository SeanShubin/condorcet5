package com.seanshubin.condorcet.util.db.memory

interface TableRow<T> {
    val primaryKey: T
    val cells: List<Any?>
}
