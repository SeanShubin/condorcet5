package com.seanshubin.condorcet.algorithm

data class Computed(val rankings: Map<Int, List<Int>>,
                    val preferenceMatrix: List<List<Int>>,
                    val strongestPathMatrix: List<List<Int>>)
