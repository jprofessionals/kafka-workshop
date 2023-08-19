package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.text.SimpleDateFormat
import java.util.TimeZone


typealias MessageData = Map<String, JsonNode>

class RapidConfiguration {

    companion object {
        const val topic: String = "rapid-1"

        val objectMapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setTimeZone(TimeZone.getTimeZone("Europe/Oslo"))

        val messageNodeFactory = JsonNodeFactory.instance
    }
}

fun JsonNode?.isNotNull(): Boolean {
    return this?.let { !it.isNull } ?: false
}