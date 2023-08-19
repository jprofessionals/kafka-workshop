package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import MessageListener
import no.jpro.kafkaworkshop.logger
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage

fun main() {
    MessageLoggerService().listen("MessageLoggerService-listener-1")
}

class MessageLoggerService : MessageListener() {

    override fun processMessage(originalMessage: RapidMessage): RapidMessage? {
        logger().info(originalMessage.toJsonText() ?: "Unable to convert message to JSON.")
        return null
    }

    override fun shouldProcessMessage(incomingMessage: MessageData): Boolean {
        return true
    }
}