package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

typealias MessageData = Map<String, JsonNode>

class RapidConfiguration {

    companion object {
        const val topic: String = "rapid-1"
        val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
        val messageNodeFactory = JsonNodeFactory.instance
    }
}

/**
 * Extension function to check if a JSON node is not null.
 */
fun JsonNode?.isNotNull(): Boolean {
    return this?.let { !it.isNull } ?: false
}