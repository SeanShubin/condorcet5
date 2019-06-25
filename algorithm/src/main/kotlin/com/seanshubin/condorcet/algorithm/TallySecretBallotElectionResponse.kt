package com.seanshubin.condorcet.algorithm

data class TallySecretBallotElectionResponse(val election: String,
                                             val candidates: List<String>,
                                             val voted: List<String>,
                                             val didNotVote: List<String>,
                                             val rankings: List<Ranking>,
                                             val ballots: List<SecretBallot>)
