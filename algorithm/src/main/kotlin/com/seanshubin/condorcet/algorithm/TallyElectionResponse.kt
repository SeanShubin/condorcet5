package com.seanshubin.condorcet.algorithm

data class TallyElectionResponse(val election: String,
                                 val candidates: List<String>,
                                 val voted: List<String>,
                                 val didNotVote: List<String>,
                                 val rankings: List<Ranking>,
                                 val ballots: List<Ballot>,
                                 val preferenceMatrix: List<List<Int>>,
                                 val strongestPathMatrix: List<List<Int>>)
