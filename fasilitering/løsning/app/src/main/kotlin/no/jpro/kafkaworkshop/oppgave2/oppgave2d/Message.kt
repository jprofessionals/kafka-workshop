package no.jpro.kafkaworkshop.oppgave2.oppgave2d

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class Message(val id: String, val value: String) {
    class MessageConverter {
        private val objectMapper = jacksonObjectMapper()

        fun convertFromJson(json: String): Message? {
            return try {
                objectMapper.readValue(json, Message::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}
