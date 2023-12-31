import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Payload
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.isNotNull
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageListener

fun main() {
    IdMappingService().listen("IdMapping-listener-1")
}

/**
 * The `IdMapping` class is responsible for listening to messages and mapping external product IDs
 * to internal ones.
 *
 * If a message has an external product ID and lacks an internal ID, it maps the external ID
 * using a predefined mapping and appends the internal ID to the message data.
 */
open class IdMappingService(messageProducer: MessageProducer = MessageProducer()) : MessageListener(messageProducer) {

    companion object {
        /**
         * Predefined mapping from external product IDs to internal ones.
         */
        private val ID_MAP = mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")
    }

    /**
     * Determines whether a message should be processed.
     *
     * @param incomingMessage The incoming message data.
     * @return `true` if the message has an external product ID and lacks an internal one, `false` otherwise.
     */
    override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
        val hasExternalId = incomingMessage["productExternalId"]?.isTextual ?: false
        val lacksInternalId = !incomingMessage["productInternalId"].isNotNull()

        return hasExternalId && lacksInternalId
    }

    /**
     * Processes the original message, appends the internal product ID if needed, and returns the new message.
     *
     * @param originalMessage The original message to be processed.
     * @return The processed message with additional data if an internal product ID is added.
     */
    override fun processMessage(originalMessage: RapidMessage): RapidMessage? {
        val externalId = originalMessage.payload["productExternalId"]?.asText()
        val internalId = ID_MAP[externalId]

        return originalMessage.copyWithAdditionalData(
            this::class.simpleName!!,
            mapOf("productInternalId" to messageNodeFactory.textNode(internalId))
        )
    }
}
