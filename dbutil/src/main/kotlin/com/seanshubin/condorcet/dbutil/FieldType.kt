package com.seanshubin.condorcet.dbutil

enum class FieldType(val sql: String) {
    STRING("varchar(255)"),
    DATE("datetime(6)"),
    BOOLEAN("boolean"),
    INT("int"),
    TEXT("text")
}
