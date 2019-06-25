package com.seanshubin.condorcet.algorithm

data class TallyElectionRequest(val election: String,
                                val candidates: List<String>,
                                val eligibleVoters: List<String>,
                                val ballots: List<Ballot>)
