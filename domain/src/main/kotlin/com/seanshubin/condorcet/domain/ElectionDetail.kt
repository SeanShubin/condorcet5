package com.seanshubin.condorcet.domain

import java.time.Instant

data class ElectionDetail(
        val ownerName: String,
        val name: String,
        val end: Instant? = null,
        val secretBallot: Boolean = true,
        val status: ElectionStatus = ElectionStatus.EDITING,
        val candidateNames: List<String>,
        val voterNames: List<String>,
        val isAllVoters: Boolean
)
