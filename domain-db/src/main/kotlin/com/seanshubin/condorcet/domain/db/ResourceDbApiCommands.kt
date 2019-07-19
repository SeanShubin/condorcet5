package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.json.JsonUtil
import java.time.Instant

class ResourceDbApiCommands(private val dbFromResource: DbFromResource) :
        DbApiCommands,
        DbFromResource by dbFromResource {
    override fun createUser(initiator: Initiator,
                            name: String,
                            email: String,
                            salt: String,
                            hash: String) {
        update("create-user.sql", name, email, salt, hash)
    }

    override fun createElection(initiator: Initiator,
                                ownerUserName: String,
                                electionName: String) {
        update("create-election.sql", ownerUserName, electionName, null, false, DbStatus.EDITING.name)
    }

    override fun setElectionEndDate(initiator: Initiator,
                                    electionName: String,
                                    end: Instant?) {
        update("set-election-end-date.sql", end, electionName)
    }

    override fun setElectionSecretBallot(initiator: Initiator,
                                         electionName: String,
                                         secretBallot: Boolean) {
        update("set-election-secret-ballot.sql", secretBallot, electionName)
    }

    override fun setElectionStatus(initiator: Initiator,
                                   electionName: String,
                                   status: DbStatus) {
        update("set-election-status.sql", status.name, electionName)
    }

    override fun setCandidates(initiator: Initiator,
                               electionName: String,
                               candidateNames: List<String>) {
        update("remove-candidates-from-election.sql", electionName)
        candidateNames.forEach {
            update("add-candidate-to-election.sql", electionName, it)
        }
    }

    override fun setVoters(initiator: Initiator,
                           electionName: String,
                           voterNames: List<String>) {
        update("remove-voters-from-election.sql", electionName)
        voterNames.forEach {
            update("add-voter-to-election.sql", electionName, it)
        }
    }

    override fun createBallot(initiator: Initiator,
                              electionName: String,
                              userName: String,
                              confirmation: String,
                              whenCast: Instant,
                              rankings: Map<String, Int>) {
        createDbBallot(electionName, userName, confirmation, whenCast)
        val ballotId = queryInt("ballot-id-by-user-election.sql", userName, electionName)
        createRankings(ballotId, userName, electionName, rankings)
    }


    override fun updateBallot(initiator: Initiator,
                              electionName: String,
                              userName: String,
                              whenCast: Instant,
                              rankings: Map<String, Int>) {
        val ballotId = queryInt("ballot-id-by-user-election.sql", userName, electionName)
        removeRankings(ballotId)
        createRankings(ballotId, userName, electionName, rankings)
    }

    override fun setReport(initiator: Initiator,
                           electionName: String,
                           report: Report) {
        update("create-tally.sql", electionName, JsonUtil.compact.writeValueAsString(report))
    }

    override fun setVotersToAll(initiator: Initiator,
                                electionName: String) {
        update("set-voters-to-all.sql", electionName)
    }

    private fun removeRankings(ballotId: Int) {
        update("remove-rankings-by-ballot.sql", ballotId)
    }

    private fun createRankings(ballotId: Int, userName: String, electionName: String, rankings: Map<String, Int>) {
        fun createRanking(ranking: Pair<String, Int>) {
            val (candidateName, rank) = ranking
            val candidateId = queryInt("candidate-id-by-election-candidate.sql", electionName, candidateName)
            createRanking(ballotId, candidateId, rank)
        }
        rankings.toList().sortedBy { it.second }.forEach(::createRanking)
    }

    private fun createRanking(ballotId: Int, candidateId: Int, rank: Int) {
        update("create-ranking.sql", ballotId, candidateId, rank)
    }

    private fun createDbBallot(electionName: String, userName: String, confirmation: String, whenCast: Instant) {
        update(
                "create-ballot.sql",
                electionName, userName, confirmation, whenCast
        )
    }
}
