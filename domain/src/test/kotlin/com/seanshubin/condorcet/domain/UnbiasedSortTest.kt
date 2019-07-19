package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.domain.db.Ranking
import com.seanshubin.condorcet.domain.db.Ranking.Companion.unbiasedSort
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class UnbiasedSortTest {
    @Test
    fun seed1() {
        // given
        val rankings = listOf(
                Ranking(3, "Alpha"),
                Ranking(1, "Bravo"),
                Ranking(2, "Charlie"),
                Ranking(2, "Delta"),
                Ranking(null, "Echo"),
                Ranking(1, "Foxtrot"),
                Ranking(null, "Golf"),
                Ranking(3, "Hotel"),
                Ranking(2, "India"),
                Ranking(null, "Juliet"),
                Ranking(3, "Kilo"),
                Ranking(1, "Lima"))

        val expectedForSeed1 = listOf(
                Ranking(1, "Foxtrot"),
                Ranking(1, "Lima"),
                Ranking(1, "Bravo"),
                Ranking(2, "India"),
                Ranking(2, "Charlie"),
                Ranking(2, "Delta"),
                Ranking(3, "Hotel"),
                Ranking(3, "Alpha"),
                Ranking(3, "Kilo"),
                Ranking(null, "Echo"),
                Ranking(null, "Golf"),
                Ranking(null, "Juliet")
        )

        val seed = 1L
        val random = Random(seed)

        // when
        val actual = rankings.unbiasedSort(random)

        // then
        assertEquals(expectedForSeed1, actual)
    }

    @Test
    fun seed2() {
        // given
        val rankings = listOf(
                Ranking(3, "Alpha"),
                Ranking(1, "Bravo"),
                Ranking(2, "Charlie"),
                Ranking(2, "Delta"),
                Ranking(null, "Echo"),
                Ranking(1, "Foxtrot"),
                Ranking(null, "Golf"),
                Ranking(3, "Hotel"),
                Ranking(2, "India"),
                Ranking(null, "Juliet"),
                Ranking(3, "Kilo"),
                Ranking(1, "Lima"))

        val expectedForSeed2 = listOf(
                Ranking(1, "Lima"),
                Ranking(1, "Bravo"),
                Ranking(1, "Foxtrot"),
                Ranking(2, "Delta"),
                Ranking(2, "Charlie"),
                Ranking(2, "India"),
                Ranking(3, "Kilo"),
                Ranking(3, "Hotel"),
                Ranking(3, "Alpha"),
                Ranking(null, "Golf"),
                Ranking(null, "Juliet"),
                Ranking(null, "Echo")
        )

        val seed = 2L
        val random = Random(seed)

        // when
        val actual = rankings.unbiasedSort(random)

        // then
        assertEquals(expectedForSeed2, actual)
    }
}
