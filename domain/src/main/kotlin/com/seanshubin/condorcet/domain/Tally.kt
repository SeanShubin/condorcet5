package com.seanshubin.condorcet.domain

data class Tally(val electionName: String,
                 val electionOwner: String,
                 val candidates: List<String>,
                 val voted: List<String>,
                 val didNotVote: List<String>,
                 val ballots: List<Ballot>,
                 val preferences: List<List<Int>>,
                 val strongestPaths: List<List<Int>>,
                 val places: List<Place>)
