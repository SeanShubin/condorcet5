package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.algorithm.Placing
import com.seanshubin.condorcet.domain.db.Ballot
import com.seanshubin.condorcet.domain.db.DbBallot
import com.seanshubin.condorcet.domain.db.Place
import com.seanshubin.condorcet.domain.db.Ranking
import java.time.Instant
import com.seanshubin.condorcet.algorithm.Ballot as AlgorithmBallot

object AlgorithmToDomain {
    fun AlgorithmBallot.toDomain(electionName: String,
                                 whenCast: Instant,
                                 isActive: Boolean): Ballot =
            Ballot(
                    user = this.voterName,
                    election = electionName,
                    confirmation = this.confirmation,
                    whenCast = whenCast,
                    active = isActive,
                    rankings = this.rankings.algorithmRankingsToDomain())

    fun List<AlgorithmBallot>.toDomain(
            dbBallotByConfirmation: Map<String, DbBallot>,
            isActive: Boolean): List<Ballot> =
            map {
                val dbBallot = dbBallotByConfirmation.getValue(it.confirmation)
                it.toDomain(dbBallot.election, dbBallot.whenCast, isActive)
            }

    fun Pair<String, Int>.algorithmRankingToDomain(): Ranking {
        val (candidate, rank) = this
        return Ranking(rank, candidate)
    }

    fun Map<String, Int>.algorithmRankingsToDomain(): List<Ranking> {
        return toList().map { it.algorithmRankingToDomain() }
    }

    fun Placing.toDomain(): Place = Place(place, candidates)

    fun List<Placing>.toDomain(): List<Place> = map { it.toDomain() }
}
