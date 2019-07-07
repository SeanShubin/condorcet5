package com.seanshubin.condorcet.domain

import java.time.Instant

data class ElectionSummary(
        val ownerName: String,
        val name: String,
        val end: Instant? = null,
        val secretBallot: Boolean = true,
        val status: ElectionStatus = ElectionStatus.EDITING,
        val candidateCount: Int = 0,
        val voterCount: Int = 0
)
