package com.seanshubin.condorcet.domain

data class ElectionDetail(
    val ownerName: String,
    val name: String,
    val endIsoString: String? = null,
    val secretBallot: Boolean = true,
    val status: ElectionStatus = ElectionStatus.EDITING,
    val candidateNames: List<String>,
    val voterNames: List<String>
)
