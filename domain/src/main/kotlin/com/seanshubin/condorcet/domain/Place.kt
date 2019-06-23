package com.seanshubin.condorcet.domain

data class Place(val name: String, val candidates: List<String>) {
    companion object {
        fun Int.toPlaceName(): String {
            val suffix = when (this) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
            return "$this$suffix"
        }
    }
}
