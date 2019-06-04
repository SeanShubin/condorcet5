package com.seanshubin.condorcet.memory.db

class InMemoryTable<PkType, T : TableRow<PkType>>(private val name: String) : Table<PkType, T> {
    private val rows = mutableListOf<T>()
    private val pkIndex = mutableMapOf<PkType, T>()

    override fun add(value: T) {
        if (pkIndex.containsKey(value.primaryKey))
            throw RuntimeException("Primary key $value already exists for table $name")
        rows.add(value)
        pkIndex[value.primaryKey] = value
    }

    override fun listAll(): List<T> = rows
    override fun remove(key: PkType) {
        rows.removeAll { it.primaryKey == key }
        pkIndex.remove(key)
    }

    override fun update(value: T) {
        val index = rows.indexOfFirst { it.primaryKey == value.primaryKey }
        if (index < 0)
            throw RuntimeException("Primary key $value does not exist for table $name")
        rows[index] = value
        pkIndex[value.primaryKey] = value
    }
}
