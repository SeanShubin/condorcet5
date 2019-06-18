package com.seanshubin.condorcet.dbutil

data class Field(val name: String,
                 val type: FieldType,
                 val allowNull: Boolean = false,
                 val unique: Boolean = false) : Column {
    override fun toSql(): List<String> {
        val parts = mutableListOf<String>()
        parts.add(name)
        parts.add(type.sql)
        if (!allowNull) {
            parts.add("not null")
        }
        if (unique) {
            parts.add("unique")
        }
        return listOf(parts.joinToString(" ") + ",")
    }

    override fun sqlName(): String = name
}
