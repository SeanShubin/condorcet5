package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.db.DbApi
import com.seanshubin.condorcet.db.DbElection
import com.seanshubin.condorcet.db.DbStatus
import com.seanshubin.condorcet.db.DbUser

class ApiBackedByDb(private val db: DbApi) : Api {
    override fun login(userNameOrUserEmail: String, userPassword: String): Credentials {
        val trimmedUserNameOrUserEmail = trim(userNameOrUserEmail)
        val dbUser =
                db.searchUserByName(trimmedUserNameOrUserEmail) ?: db.searchUserByEmail(trimmedUserNameOrUserEmail)
                ?: throw RuntimeException("User with name or email '$trimmedUserNameOrUserEmail' does not exist")
        val givenCredentials = Credentials(dbUser.name, userPassword)
        assertCredentialsValid(givenCredentials)
        val actualCredentials = dbUser.toApiCredentials()
        return actualCredentials
    }

    override fun register(userName: String, userEmail: String, userPassword: String): Credentials {
        val trimmedUserName = trim(userName)
        val trimmedUserEmail = trim(userEmail)
        assertUserNameDoesNotExist(trimmedUserName)
        assertUserEmailDoesNotExist(trimmedUserEmail)
        val dbUser = db.createUser(trimmedUserName, trimmedUserEmail, userPassword)
        return dbUser.toApiCredentials()
    }

    override fun createElection(credentials: Credentials, electionName: String): ElectionDetail {
        val trimmedElectionName = trim(electionName)
        assertCredentialsValid(credentials)
        assertElectionNameDoesNotExist(trimmedElectionName)
        val dbElection = db.createElection(credentials.userName, trimmedElectionName)
        return dbElection.toApiElectionDetail()
    }

    override fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail {
//        assertCredentialsValid(credentials)
//        assertElectionNameDoesNotExist(newElectionName)
//        val electionToCopy = getElectionDetail(electionToCopyName)
//        val newElection = electionToCopy.copy(
//                ownerName = credentials.userName,
//                name = newElectionName,
//                endIsoString = null,
//                status = ElectionStatus.EDITING)
//        createElection(newElection)
//        return newElection
        TODO("not implemented")
    }

    override fun listElections(credentials: Credentials): List<ElectionSummary> {
        TODO("not implemented")
    }

    override fun getElection(credentials: Credentials, electionName: String): ElectionDetail =
            db.findElectionByName(electionName).toApiElectionDetail()

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
        if (db.searchUserByName(userName) != null) throw RuntimeException("User with name '$userName' already exists")
    }

    private fun assertUserEmailDoesNotExist(userEmail: String) {
        if (db.searchUserByEmail(userEmail) != null) throw RuntimeException("User with email '$userEmail' already exists")
    }

    private fun assertCredentialsValid(credentials: Credentials) {
        val user = db.searchUserByName(credentials.userName) ?: authError(credentials)
        if (user.password != credentials.userPassword) authError(credentials)
    }

    private fun assertElectionNameDoesNotExist(electionName: String) {
        if (db.searchElectionByName(electionName) != null) throw RuntimeException("Election with name '$electionName' already exists")
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

    private fun getElectionDetail(electionName: String): ElectionDetail {
        TODO()
    }

    private fun createElection(electionDetail: ElectionDetail) {

    }

    private fun trim(s: String): String = s.trim().replace(whitespaceBlock, " ")

    companion object {
        private val whitespaceBlock = Regex("""\s+""")
    }
}
