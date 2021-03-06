package com.seanshubin.condorcet.prototype

import com.amazonaws.services.rds.AmazonRDSAsync
import com.amazonaws.services.rds.AmazonRDSAsyncClientBuilder
import com.seanshubin.condorcet.util.DurationFormat
import com.seanshubin.condorcet.util.rds.RdsDatabaseApi
import com.seanshubin.condorcet.util.rds.RdsDatabaseApiImpl
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun withApi(caption: String, f: (RdsDatabaseApi) -> Unit) {
    val duration = timer {
        val api = createApi()
        f(api)
    }
    val durationString = DurationFormat.milliseconds.format(duration)
    println("$caption: $durationString")
}

fun timer(f: () -> Unit): Long {
    val start = System.currentTimeMillis()
    f()
    val end = System.currentTimeMillis()
    val duration = end - start
    return duration
}

fun createApi(): RdsDatabaseApi {
    val properties = Properties()
    val path = Paths.get(awsCredentialsPath)
    val inputStream = Files.newInputStream(path)
    properties.load(inputStream)
    val credentialsProvider = PropertiesCredentialsProvider(properties)
    val rdsClient: AmazonRDSAsync =
            AmazonRDSAsyncClientBuilder
                    .standard()
                    .withRegion(awsRegion)
                    .withCredentials(credentialsProvider).build()
    val api: RdsDatabaseApi = RdsDatabaseApiImpl(rdsClient, dbUser, dbPassword, dbName)
    return api
}
