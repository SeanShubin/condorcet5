package com.seanshubin.condorcet.domain

import java.time.Instant

data class Ballot(
        val user: String,
        val election: String,
        val confirmation: String,
        val whenCast: Instant,
        val isActive: Boolean,
        val rankings: List<Ranking>)
