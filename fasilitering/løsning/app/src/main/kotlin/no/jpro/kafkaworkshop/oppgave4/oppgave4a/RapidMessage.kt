package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import java.time.ZoneId
import java.time.ZonedDateTime

data class RapidMessage(
    val eventName: String,
    val messageData: MessageData,
    val participatingSystems: List<ParticipatingSystem>
) {
    data class ParticipatingSystem(
        val applikasjonsnavn: String,
        val timestamp: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())
    )

    class MessageConverter {
        fun convertFromJson(json: String): RapidMessage? {
            return try {
                Rapid.objectMapper.readValue(json, RapidMessage::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun toJsonText() = Rapid.objectMapper.writeValueAsString(this)

    fun copyWithAdditionalData(callerClass: String, addMessageData: MessageData): RapidMessage {
        val participatingSystem = ParticipatingSystem(callerClass)
        return this.copy(
            participatingSystems = participatingSystems + participatingSystem,
            messageData = messageData + addMessageData
        )
    }
}
