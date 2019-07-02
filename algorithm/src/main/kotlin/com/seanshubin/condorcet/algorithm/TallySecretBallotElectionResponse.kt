package com.seanshubin.condorcet.algorithm

data class TallySecretBallotElectionResponse(val election: String,
                                             val candidates: List<String>,
                                             val voted: List<String>,
                                             val didNotVote: List<String>,
                                             val placings: List<Placing>,
                                             val ballots: List<SecretBallot>)
