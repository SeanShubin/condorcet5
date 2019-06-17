package com.seanshubin.condorcet.util

fun <T> List<T>.exactlyOne(nameOfThingLookingFor: String): T = when (size) {
    1 -> get(0)
    else -> throw RuntimeException("Exactly one $nameOfThingLookingFor expected, got $size")
}
