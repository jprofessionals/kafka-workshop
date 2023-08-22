package no.jpro.kafkaworkshop.oppgave4.oppgave4b


import no.jpro.kafkaworkshop.logger
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Payload
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageListener
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage

fun main() {
    MessageLoggerService().listen("MessageLoggerService-listener-1")
}

/**
 * The `MessageLoggerService` listens to messages and logs their content.
 */
    open class MessageLoggerService : MessageListener() {

    /**
     * Logs the content of the original message.
     *
     * This implementation of the `processMessage` function logs the content of the original message
     * and does not return any new message (returns null).
     *
     * @param originalMessage The original message to process.
     * @return Always returns null as this service is just logging the message without producing any new message.
     */
    override fun processMessage(originalMessage: RapidMessage): RapidMessage? {
        logger().info(originalMessage.toJsonText() ?: "Unable to convert message to JSON.")
        return null
    }

    /**
     * Specifies that all incoming messages should be processed.
     *
     * This function always returns true, indicating that every incoming message should be processed
     * and logged by the service.
     *
     * @param incomingMessage The incoming message to check.
     * @return Always true.
     */
    override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
        return true
    }
}
