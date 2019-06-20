package com.seanshubin.condorcet.dbutil

import com.seanshubin.condorcet.dbutil.FieldType.*

object Schema {
    val userName = Field("name", STRING, unique = true)
    val userEmail = Field("email", STRING, unique = true)
    val userSalt = Field("salt", STRING)
    val userHash = Field("hash", STRING)
    val user = Table("user", userName, userEmail, userSalt, userHash)
    val statusName = Field("name", STRING)
    val status = Table("status", statusName)
    val electionOwner = ForeignKey("owner", user)
    val electionName = Field("name", STRING, unique = true)
    val electionEnd = Field("end", DATE, allowNull = true)
    val electionSecret = Field("secret", BOOLEAN)
    val electionStatus = ForeignKey("status", status)
    val election = Table(
            "election",
            electionOwner,
            electionName,
            electionEnd,
            electionSecret,
            electionStatus)
    val candidateElection = ForeignKey("election", election)
    val candidateName = Field("name", STRING)
    val candidate = Table(
            "candidate",
            columns = listOf(candidateElection, candidateName),
            unique = listOf(candidateElection, candidateName))
    val voterElection = ForeignKey("election", election)
    val voterUser = ForeignKey("user", user)
    val voter = Table(
            "voter",
            columns = listOf(voterElection, voterUser),
            unique = listOf(voterElection, voterUser))
    val ballotUser = ForeignKey("user", user)
    val ballotElection = ForeignKey("election", election)
    val whenCast = Field("when_cast", DATE)
    val ballot = Table(
            "ballot",
            columns = listOf(ballotUser, ballotElection, whenCast),
            unique = listOf(ballotUser, ballotElection))
    val rankingBallot = ForeignKey("ballot", ballot)
    val rankingCandidate = ForeignKey("candidate", candidate)
    val rankingRank = Field("rank", INT, allowNull = true)
    val ranking = Table("ranking",
            columns = listOf(rankingBallot, rankingCandidate, rankingRank),
            unique = listOf(rankingBallot, rankingCandidate))
    val tallyElection = ForeignKey("election", election)
    val tallyCandidate = ForeignKey("candidate", candidate)
    val tallyRank = Field("rank", INT)
    val tally = Table(
            "tally",
            columns = listOf(tallyElection, tallyCandidate, tallyRank),
            unique = listOf(tallyElection, tallyCandidate))
    val eventWhen = Field("when", STRING)
    val eventText = Field("text", TEXT)
    val event = Table("event", eventWhen, eventText)
    val tables = listOf(event, user, status, election, candidate, voter, ballot, ranking, tally)
}
