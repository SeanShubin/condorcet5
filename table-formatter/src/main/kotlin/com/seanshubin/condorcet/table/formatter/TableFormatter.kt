package com.seanshubin.condorcet.table.formatter

import com.seanshubin.condorcet.table.formatter.StringUtil.escape
import com.seanshubin.condorcet.table.formatter.StringUtil.truncate

interface TableFormatter {
    interface Justify {
        data class Left(val x: Any?) : Justify

        data class Right(val x: Any?) : Justify
    }

    fun format(originalRows: List<List<Any?>>): List<String>

    companion object {
        val escapeString: (Any?) -> String = { cell ->
            when (cell) {
                null -> "null"
                else -> cell.toString().escape()
            }
        }

        fun escapeAndTruncateString(max: Int): (Any?) -> String = { cell ->
            escapeString(cell).truncate(max)
        }
    }
}
