package com.seanshubin.condorcet.domain

data class ElectionSummary(
    val ownerName: String,
    val name: String,
    val endIsoString: String? = null,
    val secretBallot: Boolean = true,
    val status: ElectionStatus = ElectionStatus.EDITING,
    val candidateCount: Int = 0,
    val voterCount: Int = 0
)
