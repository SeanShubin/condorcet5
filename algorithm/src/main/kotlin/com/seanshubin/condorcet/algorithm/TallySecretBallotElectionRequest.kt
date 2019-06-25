package com.seanshubin.condorcet.algorithm

data class TallySecretBallotElectionRequest(val election: String,
                                            val candidates: List<String>,
                                            val eligibleVoters: List<String>,
                                            val ballots: List<SecretBallot>)
