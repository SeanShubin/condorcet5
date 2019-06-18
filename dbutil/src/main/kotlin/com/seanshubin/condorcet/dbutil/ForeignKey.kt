package com.seanshubin.condorcet.dbutil

data class ForeignKey(val name: String, val table: Table) : Column {
    override fun toSql(): List<String> =
            listOf(
                    "${sqlName()} int not null,",
                    "foreign key fk_$name(${sqlName()}) references ${table.name}(id),")

    override fun sqlName(): String = "${name}_id"
}
