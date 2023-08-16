package no.jpro.kafkaworkshop

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory

object Common {
    val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop")
    val objectMapper = jacksonObjectMapper()
    const val topic = "kotlin_topic"

    data class Message(val id: String, val value: String)
}