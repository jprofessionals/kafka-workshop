package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.ZonedDateTime


typealias Message = Map<String, ObjectNode>

class Rapid {
    companion object {
        val topic: String = "rapid-1"
        val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
    }

    data class RapidMessage(
        val eventName: String,
        val message: Message,
        val participatingSystems: List<ParticipatingSystem>
    ) {
        data class ParticipatingSystem(val applikasjonsnavn: String, val timestamp: ZonedDateTime)

        class MessageConverter {

            fun convertFromJson(json: String): RapidMessage? {
                return try {
                    objectMapper.readValue(json, RapidMessage::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        }

        fun toJsonText() = objectMapper.writeValueAsString(this)
    }

}

