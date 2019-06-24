package com.seanshubin.condorcet.util.db

data class Field(val name: String,
                 val type: FieldType,
                 val allowNull: Boolean = false,
                 val unique: Boolean = false) : Column {
    override fun toSql(): List<String> {
        val parts = mutableListOf<String>()
        parts.add(handleKeyword(name))
        parts.add(type.sql)
        if (!allowNull) {
            parts.add("not null")
        }
        if (unique) {
            parts.add("unique")
        }
        return listOf(parts.joinToString(" ") + ",")
    }

    private fun handleKeyword(s: String): String =
            if (MysqlConstants.reservedWords.contains(s.toLowerCase())) {
                """`$s`"""
            } else {
                s
            }

    override fun sqlName(): String = handleKeyword(name)
}
