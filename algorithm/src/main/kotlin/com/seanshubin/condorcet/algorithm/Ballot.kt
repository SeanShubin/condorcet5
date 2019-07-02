package com.seanshubin.condorcet.algorithm

import java.util.function.Function

data class Ballot(val voterName: String,
                  val confirmation: String,
                  val rankings: Map<String, Int>) {
    companion object {
        val sortByConfirmation = Comparator.comparing(Function<Ballot, String> { ballot -> ballot.confirmation })
    }
}
