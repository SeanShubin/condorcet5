package com.seanshubin.condorcet.domain

data class Ballot(val ballotId: String,
                  val confirmation: String,
                  val electionName: String,
                  val voterName: String,
                  val whenCastIso: String?,
                  val isActive: Boolean,
                  val rankings: List<Ranking>)
