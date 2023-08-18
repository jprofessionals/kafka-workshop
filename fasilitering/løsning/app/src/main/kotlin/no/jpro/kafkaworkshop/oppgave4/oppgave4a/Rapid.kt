package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.ZoneId
import java.time.ZonedDateTime


typealias MessageData = Map<String, ObjectNode>

class Rapid {
    companion object {
        val topic: String = "rapid-1"
        val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
    }

    data class RapidMessage(
        val eventName: String,
        val messageData: MessageData,
        val participatingSystems: List<ParticipatingSystem>
    ) {
        data class ParticipatingSystem private constructor(val applikasjonsnavn: String, val timestamp: ZonedDateTime) {
            constructor(applikasjonsnavn: String) : this(applikasjonsnavn, ZonedDateTime.now(ZoneId.systemDefault()))
        }

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


        fun copy2(participatingSystem: ParticipatingSystem, addMessageData: MessageData): RapidMessage {
            return this.copy(
                participatingSystems = participatingSystems + participatingSystem,
                messageData = messageData + addMessageData
            )
        }

    }

}

