package com.seanshubin.condorcet.domain.db

import java.time.Instant

data class Ballot(
        val user: String,
        val election: String,
        val confirmation: String,
        val whenCast: Instant,
        val active: Boolean,
        val rankings: List<Ranking>)
