package com.seanshubin.condorcet.domain.db

data class Place(val name: String, val candidates: List<String>) {
    constructor(placeValue: Int, candidates: List<String>) :
            this(placeValue.toPlaceName(), candidates)

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
