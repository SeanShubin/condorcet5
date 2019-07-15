package com.seanshubin.condorcet.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

object JsonUtil {
    val pretty: ObjectMapper = ObjectMapper().registerModule(KotlinModule()).registerModule(JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    val compact: ObjectMapper = ObjectMapper().registerModule(KotlinModule()).registerModule(JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}