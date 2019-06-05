package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.db.DbApi
import com.seanshubin.condorcet.db.DbElection
import com.seanshubin.condorcet.db.DbStatus
import com.seanshubin.condorcet.db.DbUser

class ApiBackedByDb(private val db: DbApi) : Api {
    override fun login(userNameOrUserEmail: String, userPassword: String): Credentials {
        val dbUser =
                db.searchUserByName(userNameOrUserEmail) ?: db.searchUserByEmail(userNameOrUserEmail)
                ?: throw RuntimeException("User with name or email '$userNameOrUserEmail' does not exist")
        return dbUser.toApiCredentials()
    }

    override fun register(userName: String, userEmail: String, userPassword: String): Credentials {
        assertUserNameDoesNotExist(userName)
        assertUserEmailDoesNotExist(userEmail)
        val dbUser = db.createUser(userName, userEmail, userPassword)
        return dbUser.toApiCredentials()
    }

    override fun createElection(credentials: Credentials, electionName: String): ElectionDetail {
        assertCredentialsValid(credentials)
        assertElectionNameDoesNotExist(electionName)
        val dbElection = db.createElection(credentials.userName, electionName)
        return dbElection.toApiElectionDetail()
    }

    override fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun listElections(credentials: Credentials): List<ElectionSummary> {
        TODO("not implemented")
    }

    override fun getElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun endElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun listCandidates(credentials: Credentials, electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun updateCandidateNames(credentials: Credentials, electionName: String, candidateNames: List<String>): List<String> {
        TODO("not implemented")
    }

    override fun listEligibleVoters(credentials: Credentials, electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun areAllVotersEligible(credentials: Credentials, electionName: String): Boolean {
        TODO("not implemented")
    }

    override fun listAllVoters(credentials: Credentials): List<String> {
        TODO("not implemented")
    }

    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoterNames: List<String>): List<String> {
        TODO("not implemented")
    }

    override fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun listBallots(credentials: Credentials, voterName: String): List<Ballot> {
        TODO("not implemented")
    }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot {
        TODO("not implemented")
    }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: List<Ranking>): Ballot {
        TODO("not implemented")
    }

    override fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): ElectionDetail {
        TODO("not implemented")
    }

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail {
        TODO("not implemented")

    }

    private fun assertUserNameDoesNotExist(userName: String) {
        if (db.searchUserByName(userName) != null) throw RuntimeException("User named '$userName' already exists")
    }

    private fun assertUserEmailDoesNotExist(userEmail: String) {
        if (db.searchUserByEmail(userEmail) != null) throw RuntimeException("User named '$userEmail' already exists")
    }

    private fun assertCredentialsValid(credentials: Credentials) {
        val user = db.searchUserByName(credentials.userName) ?: authError(credentials)
        if (user.password != credentials.userPassword) authError(credentials)
    }

    private fun assertElectionNameDoesNotExist(electionName: String) {
        if (db.searchElectionByName(electionName) != null) throw RuntimeException("Election named '$electionName' already exists")
    }

    private fun authError(credentials: Credentials): Nothing =
            throw RuntimeException("Invalid user/password combination for '${credentials.userName}'")

    private fun DbUser.toApiCredentials(): Credentials = Credentials(userName = name, userPassword = password)

    private fun DbElection.toApiElectionDetail(): ElectionDetail {
        val candidateNames = db.listCandidateNames(name)
        val voterNames = db.listVoterNames(name)
        return ElectionDetail(owner, name, end, secret, status.toApiStatus(), candidateNames, voterNames)
    }

    private fun DbStatus.toApiStatus(): ElectionStatus = when (this) {
        DbStatus.EDITING -> ElectionStatus.EDITING
        DbStatus.LIVE -> ElectionStatus.LIVE
        DbStatus.COMPLETE -> ElectionStatus.COMPLETE
    }
}
