/*package no.jpro.kafkaworkshop.oppgave4.oppgave4b


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
    // TODO: Insert override methods from MessageListener and implement the methods.
    // This service should pick all messages from the rapid
    // Make sure no confirmation message is sent to the rapid topic, do not return a message from processMessage.
    // Use logger().info(...) to write the message to the log. Use .toJsonText to write it as json.
}*/