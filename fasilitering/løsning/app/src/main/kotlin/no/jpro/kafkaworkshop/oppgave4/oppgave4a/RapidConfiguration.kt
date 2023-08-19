package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.TimeZone

/**
 * A type alias for a map where keys are strings and values are `JsonNode` objects.
 */
typealias Payload = Map<String, JsonNode>

/**
 * A configuration class for handling Rapid message processing.
 *
 * This class provides centralized configurations for handling messages in the Kafka workshop.
 * It sets up a global Jackson object mapper and a Json node factory.
 */
class RapidConfiguration {

    companion object {

        /** The topic name used for Rapid messages. */
        const val topic: String = "rapid-1"

        /**
         * Determines whether or not auto-commit of offsets is enabled.
         *
         * When set to `true`, the consumer's offset will be periodically committed
         * in the background. This is useful for most at-least-once delivery
         * guarantees.
         *
         * When set to `false`, the consumer application should manually commit
         * offsets using `commitSync` or `commitAsync` methods. This provides
         * better control over when a record is considered as consumed but also
         * means the application has to handle offset management.
         */
        var isAutoCommitEnabled: Boolean = true

        /**
         * A global Jackson object mapper configuration.
         *
         * This mapper is pre-configured with modules and settings suitable for the workshop.
         */
        val objectMapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setTimeZone(TimeZone.getTimeZone("Europe/Oslo"))

        /** A global instance of `JsonNodeFactory` for creating JSON nodes. */
        val messageNodeFactory = JsonNodeFactory.instance
    }
}

/**
 * An extension function to check if a `JsonNode` is not null.
 *
 * @return `true` if the node is not null, otherwise `false`.
 */
fun JsonNode?.isNotNull(): Boolean {
    return this?.let { !it.isNull } ?: false
}
