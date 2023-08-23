import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Payload
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.isNotNull
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageListener

fun main() {
    CustomerService().listen("CustomerService-listener-1")
}

/**
 * The `CustomerService` class processes messages related to customer products.
 * It checks if a product exists, if it has an internal ID, and if it has not been previously processed.
 * If all conditions are met, it marks the message as processed.
 */
open class CustomerService(messageProducer: MessageProducer = MessageProducer()) : MessageListener(messageProducer) {

    /**
     * Determines whether a message should be processed based on certain criteria.
     *
     * @param incomingMessage The incoming message data.
     * @return `true` if the message has a product, has an internal product ID,
     *         and hasn't been processed before. Returns `false` otherwise.
     */
    override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
        val productExists = incomingMessage["product"]?.isNotNull() ?: false
        val internalIdExists = incomingMessage["productInternalId"]?.isNotNull() ?: false
        val alreadyProcessed = incomingMessage["processed"]?.booleanValue() == true

        return productExists && internalIdExists && !alreadyProcessed
    }

    /**
     * Processes the given original message by adding a "processed" flag.
     *
     * @param originalMessage The original message to be processed.
     * @return A new message with additional data indicating it has been processed.
     */
    override fun processMessage(originalMessage: RapidMessage): RapidMessage? {
        val additionalData = mapOf("processed" to messageNodeFactory.booleanNode(true))
        return originalMessage.copyWithAdditionalData(this::class.simpleName!!, additionalData)
    }
}
