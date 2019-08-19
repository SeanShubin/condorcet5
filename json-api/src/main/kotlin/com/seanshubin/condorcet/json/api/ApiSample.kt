package com.seanshubin.condorcet.json.api

import com.seanshubin.condorcet.domain.*
import com.seanshubin.condorcet.domain.db.Ballot
import com.seanshubin.condorcet.domain.db.Place
import com.seanshubin.condorcet.domain.db.Ranking
import com.seanshubin.condorcet.domain.db.Report
import java.time.Instant

class ApiSample : Api {
    private val sample = Sample()
    override fun lastSynced(): Int = sample.lastSynced()

    override fun login(nameOrEmail: String, password: String): Credentials = sample.credentials()

    override fun register(name: String, email: String, password: String): Credentials = sample.credentials()

    override fun createElection(credentials: Credentials, electionName: String): ElectionDetail = sample.electionDetail()

    override fun setEndDate(credentials: Credentials, electionName: String, endDate: Instant?): ElectionDetail = sample.electionDetail()

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail = sample.electionDetail()

    override fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail = sample.electionDetail()

    override fun endElection(credentials: Credentials, electionName: String): ElectionDetail = sample.electionDetail()

    override fun setCandidateNames(credentials: Credentials, electionName: String, candidateNames: List<String>): ElectionDetail = sample.electionDetail()

    override fun setVoters(credentials: Credentials, electionName: String, eligibleVoterNames: List<String>): ElectionDetail = sample.electionDetail()

    override fun setVotersToAll(credentials: Credentials, electionName: String): ElectionDetail = sample.electionDetail()

    override fun listElections(credentials: Credentials): List<ElectionSummary> = sample.electionSummaryList()

    override fun getElection(credentials: Credentials, electionName: String): ElectionDetail = sample.electionDetail()

    override fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail = sample.electionDetail()

    override fun listBallots(credentials: Credentials, voterName: String): List<Ballot> = sample.ballotList()

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot = sample.ballot()
    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: Map<String, Int>): Ballot = sample.ballot()
    override fun tally(credentials: Credentials, electionName: String): Report = sample.report()

    private class Sample {
        var index = 0
        fun lastSynced(): Int = ++index
        fun string(caption: String): String = "$caption-${++index}"
        fun userName(): String = string("userName")
        fun password(): String = string("password")
        fun credentials(): Credentials = Credentials(userName(), password())
        fun electionName(): String = string("election")
        fun instant(): Instant = Instant.ofEpochMilli(0).plusMillis(++index * 1000L)
        fun electionEnd(): Instant? = instant()
        fun boolean(): Boolean = (++index) % 2 == 0
        fun electionSecretBallot(): Boolean = boolean()
        inline fun <reified T : Enum<T>> chooseEnum(): T = enumValues<T>()[(++index) % enumValues<T>().size]
        fun electionStatus(): ElectionStatus = chooseEnum()
        fun stringList(quantity: Int, caption: String): List<String> = (1..quantity).map { string(caption) }
        fun candidateNames(): List<String> = stringList(3, "candidate")
        fun voterNames(): List<String> = stringList(3, "voter")
        fun isAllVoters(): Boolean = boolean()
        fun electionDetail(): ElectionDetail = ElectionDetail(
                userName(),
                electionName(),
                electionEnd(),
                electionSecretBallot(),
                electionStatus(),
                candidateNames(),
                voterNames(),
                isAllVoters())

        fun electionSummary(): ElectionSummary = ElectionSummary(
                userName(),
                electionName(),
                electionEnd(),
                electionSecretBallot(),
                electionStatus())

        fun electionSummaryList(quantity: Int): List<ElectionSummary> = (1..quantity).map { electionSummary() }
        fun electionSummaryList(): List<ElectionSummary> = electionSummaryList(3)
        fun confirmation(): String = string("confirmation")
        fun whenCast(): Instant = instant()
        fun active(): Boolean = boolean()
        fun rank(): Int? = ++index
        fun candidateName(): String = string("candidate")
        fun ranking(): Ranking = Ranking(rank(), candidateName())
        fun rankings(quantity: Int): List<Ranking> = (1..quantity).map { ranking() }
        fun rankings(): List<Ranking> = rankings(3)
        fun ballot(): Ballot = Ballot(userName(), electionName(), confirmation(), whenCast(), active(), rankings())
        fun ballotList(quantity: Int): List<Ballot> = (1..quantity).map { ballot() }
        fun ballotList(): List<Ballot> = ballotList(3)
        fun row(cols: Int): List<Int> = (1..cols).map { ++index }
        fun rows(rows: Int, cols: Int): List<List<Int>> = (1..rows).map { row(cols) }
        fun preferences(): List<List<Int>> = rows(3, 3)
        fun strongestPaths(): List<List<Int>> = rows(3, 3)
        fun place(): Place = Place(++index, candidateNames())
        fun places(quantity: Int): List<Place> = (1..quantity).map { place() }
        fun places(): List<Place> = places(3)
        fun report(): Report = Report(
                electionName(),
                userName(),
                candidateNames(),
                voterNames(),
                voterNames(),
                ballotList(),
                preferences(),
                strongestPaths(),
                places())
    }
}