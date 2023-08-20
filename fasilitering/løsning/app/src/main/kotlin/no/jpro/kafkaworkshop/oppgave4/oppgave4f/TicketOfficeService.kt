package no.jpro.kafkaworkshop.oppgave4.oppgave4f

import MessageListener
import no.jpro.kafkaworkshop.logger
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.*

fun main() {
    TicketOfficeService().start()
    TicketOfficeService().listen("TicketOfficeService-listener-1")
}

open class TicketOfficeService(private val messageProducer: MessageProducer = MessageProducer()) :
    MessageListener(messageProducer) {
    fun start() {
        val applicationName = this::class.simpleName.toString()

        val payload: Payload = mapOf(
            "need" to RapidConfiguration.messageNodeFactory.textNode("ticketOffer"),
        )

        val message = RapidMessage.fromData(applicationName, "ticketEvent", payload)
        messageProducer.send(message)
    }

    override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
        val ticketOffer = incomingMessage["ticketOffer"]?.isNotNull() ?: false
        val alreadyProcessed = incomingMessage["processed"]?.booleanValue() == true

        return ticketOffer && !alreadyProcessed
    }

    override fun processMessage(originalMessage: RapidMessage): RapidMessage {
        val offer = originalMessage.payload["ticketOffer"]?.asText()
        logger().info("ticket offer received, offer: $offer")
        val additionalData = mapOf("processed" to RapidConfiguration.messageNodeFactory.booleanNode(true))
        return originalMessage.copyWithAdditionalData(this::class.simpleName!!, additionalData)
    }
}