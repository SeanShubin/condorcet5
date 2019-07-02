package com.seanshubin.condorcet.matrix

data class Size(val row: Int, val col: Int) {
    fun squareSize(): Int =
            if (row == col) row
            else throw RuntimeException("size is not a square, row is $row and col is $col")
}
