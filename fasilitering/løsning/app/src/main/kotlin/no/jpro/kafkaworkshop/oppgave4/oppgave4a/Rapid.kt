package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

typealias MessageData = Map<String, JsonNode>

class Rapid {

    companion object {
        const val topic: String = "rapid-1"
        val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
        val messageConverter = RapidMessage.MessageConverter()
        val messageNodeFactory = JsonNodeFactory.instance


    }
}