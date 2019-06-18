package com.seanshubin.condorcet.dbutil

enum class FieldType(val sql: String) {
    STRING("varchar(255)"),
    DATE("datetime"),
    BOOLEAN("boolean"),
    INT("int")
}
