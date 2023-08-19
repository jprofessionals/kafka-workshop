package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import no.jpro.kafkaworkshop.logger
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Represents a message used in the Rapid communication protocol.
 *
 * @property eventName The name of the event this message relates to.
 * @property payload The actual data of the message represented as a map.
 * @property participatingSystems A list of systems that have interacted with or processed this message.
 */
data class RapidMessage(
    val eventName: String,
    val payload: Payload,
    val participatingSystems: List<ParticipatingSystem>
) {
    /**
     * Represents a system that has participated in the lifecycle of a RapidMessage.
     *
     * @property applicationName The name of the application or system.
     * @property eventTime The time when the system interacted with or processed the message.
     */
    data class ParticipatingSystem(
        val applicationName: String,
        val eventTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Oslo"))
    )

    companion object {
        /**
         * Converts a JSON string into a `RapidMessage` object.
         *
         * @param json The JSON representation of a RapidMessage.
         * @return A RapidMessage object or null if conversion fails.
         */
        fun convertFromJson(json: String): RapidMessage? {
            return try {
                RapidConfiguration.objectMapper.readValue(json, RapidMessage::class.java)
            } catch (e: Exception) {
                logger().error("Error converting JSON to RapidMessage", e)
                null
            }
        }

        /**
         * Creates a `RapidMessage` instance from provided data.
         *
         * @param callerClass The class or system creating this message.
         * @param eventName The name of the event.
         * @param additionalPayload Additional message data.
         * @return A new RapidMessage object.
         */
        fun fromData(callerClass: String, eventName: String, additionalPayload: Payload): RapidMessage {
            val participatingSystem = ParticipatingSystem(callerClass)
            return RapidMessage(
                eventName = eventName,
                payload = additionalPayload,
                participatingSystems = listOf(participatingSystem)
            )
        }
    }

    /**
     * Converts this `RapidMessage` object into a JSON string.
     *
     * @return A JSON string or null if conversion fails.
     */
    fun toJsonText(): String? {
        return try {
            RapidConfiguration.objectMapper.writeValueAsString(this)
        } catch (e: Exception) {
            logger().error("Error converting RapidMessage to JSON", e)
            null
        }
    }

    /**
     * Returns a copy of this `RapidMessage` with additional data.
     *
     * This method can be used when a new system processes or interacts with the message
     * and needs to append its information and some additional data to the message.
     *
     * @param callerClass The class or system interacting with the message.
     * @param additionalPayload Additional message data.
     * @return A new RapidMessage object.
     */
    fun copyWithAdditionalData(callerClass: String, additionalPayload: Payload): RapidMessage {
        val newParticipatingSystem = ParticipatingSystem(callerClass)
        return this.copy(
            participatingSystems = participatingSystems + newParticipatingSystem,
            payload = payload + additionalPayload
        )
    }
}
