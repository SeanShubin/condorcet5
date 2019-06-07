package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.createWithElection
import com.seanshubin.condorcet.domain.Tester.createWithUser
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.invalidCredentials
import com.seanshubin.condorcet.domain.Tester.validCredentials
import kotlin.test.Test
import kotlin.test.assertEquals


class ElectionEndDateTest {
    @Test
    fun defaultsToNull() {
        // given
        val api = createWithUser()

        // when
        val election = api.createElection(validCredentials, electionName)

        // then
        assertEquals(null, election.endIsoString)
    }

    @Test
    fun setEndDate() {
        // given
        val api = createWithElection()
        val isoEndDate = "2019-06-07T16:05:41.325574Z"

        // when
        val election = api.setEndDate(validCredentials, electionName, isoEndDate)

        // then
        assertEquals(isoEndDate, election.endIsoString)
    }

    @Test
    fun setEndDateAuth() {
        // given
        val api = createWithElection()
        val isoEndDate = "2019-06-07T16:05:41.325574Z"

        // when
        val result = Try { api.setEndDate(invalidCredentials, electionName, isoEndDate) }

        // then
        assertEquals("Invalid user/password combination for 'Alice'", (result as Failure).exception.message)
    }
}
