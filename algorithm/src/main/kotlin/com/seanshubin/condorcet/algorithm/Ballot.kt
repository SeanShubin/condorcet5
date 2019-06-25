package com.seanshubin.condorcet.algorithm

data class Ballot(val voterName: String,
                  val confirmation: String,
                  val rankings: Map<String, Int>)
