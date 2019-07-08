package com.seanshubin.condorcet.table.formatter

object ListUtil {
    fun <T> List<List<T>>.transpose(): List<List<T>> {
        return if (this.isEmpty()) {
            emptyList()
        } else {
            val mutableList = mutableListOf<List<T>>()
            for (i in 0..this[0].lastIndex) {
                val newMutableRow = mutableListOf<T>()
                for (j in 0..this.lastIndex) {
                    newMutableRow.add(this[j][i])
                }
                mutableList.add(newMutableRow)
            }
            mutableList
        }
    }

    fun <T> List<T>.exactlyOne(): T =
            when (size) {
                0 -> throw RuntimeException("exactly 1 element expected, got 0")
                1 -> get(0)
                else -> throw  RuntimeException("exactly 1 element expected, got $size")
            }
}
