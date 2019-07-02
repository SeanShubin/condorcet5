package com.seanshubin.condorcet.algorithm

data class TallyElectionRequest(val election: String,
                                val candidates: Set<String>,
                                val eligibleVoters: Set<String>,
                                val ballots: List<Ballot>)
