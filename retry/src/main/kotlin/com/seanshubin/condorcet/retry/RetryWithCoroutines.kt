package com.seanshubin.condorcet.retry

import com.seanshubin.condorcet.contract.SystemContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.Duration

class RetryWithCoroutines(private val system: SystemContract) : Retry {
    override fun waitUntil(howOftenToCheck: Duration, howLongToWait: Duration, predicate: (Int) -> Boolean): Long {
        val started = system.currentTimeMillis()
        var tryIndex = 0
        runBlocking {
            while (true) {
                tryIndex++
                val result = predicate(tryIndex)
                if (result) {
                    break
                } else {
                    val timePassed = Duration.ofMillis(System.currentTimeMillis() - started)
                    if (timePassed > howLongToWait) {
                        throw RuntimeException(
                                "Timed out after $tryIndex trys, " +
                                        "check every $howOftenToCheck, " +
                                        "give up after $howLongToWait")
                    } else {
                        delay(howOftenToCheck.toMillis())
                    }
                }
            }
        }
        val finished = system.currentTimeMillis()
        return finished - started
    }
}