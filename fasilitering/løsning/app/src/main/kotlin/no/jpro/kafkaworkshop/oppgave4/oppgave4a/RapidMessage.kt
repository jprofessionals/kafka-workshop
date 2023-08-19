package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import no.jpro.kafkaworkshop.logger
import java.time.ZoneId
import java.time.ZonedDateTime

data class RapidMessage(
    val eventName: String,
    val messageData: MessageData,
    val participatingSystems: List<ParticipatingSystem>
) {
    data class ParticipatingSystem(
        val applicationName: String, // renamed for clarity
        val timestamp: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())
    )

    companion object {
        fun convertFromJson(json: String): RapidMessage? {
            return try {
                RapidConfiguration.objectMapper.readValue(json, RapidMessage::class.java)
            } catch (e: Exception) {
                logger().error("Error converting JSON to RapidMessage", e)
                null
            }
        }

        fun fromData(callerClass: String, eventName: String, additionalMessageData: MessageData): RapidMessage {
            val participatingSystem = ParticipatingSystem(callerClass)
            return RapidMessage(
                eventName = eventName,
                messageData = additionalMessageData,
                participatingSystems = listOf(participatingSystem)
            )
        }
    }

    fun toJsonText(): String? {
        return try {
            RapidConfiguration.objectMapper.writeValueAsString(this)
        } catch (e: Exception) {
            logger().error("Error converting RapidMessage to JSON", e)
            null
        }
    }

    fun copyWithAdditionalData(callerClass: String, additionalMessageData: MessageData): RapidMessage {
        val newParticipatingSystem = ParticipatingSystem(callerClass)
        return this.copy(
            participatingSystems = participatingSystems + newParticipatingSystem,
            messageData = messageData + additionalMessageData
        )
    }
}
