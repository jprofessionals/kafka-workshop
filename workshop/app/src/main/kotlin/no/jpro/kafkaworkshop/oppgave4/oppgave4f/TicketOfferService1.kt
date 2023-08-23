package no.jpro.kafkaworkshop.oppgave4.oppgave4f

import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageListener
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.*

fun main() {
    TicketOfferService1().listen("TicketOfferService1-listener-1")
}

open class TicketOfferService1(private val messageProducer: MessageProducer = MessageProducer()) :
    MessageListener(messageProducer) {

    override fun processMessage(originalMessage: RapidMessage): RapidMessage? {
        return originalMessage.copyWithAdditionalData(
            this::class.simpleName!!,
            mapOf("ticketOffer" to RapidConfiguration.messageNodeFactory.numberNode(22))
        )
    }

    override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
        val need = incomingMessage["need"]?.asText()
        val ticketOfferNeed =  need.equals("ticketOffer")
        val ticketOffer = incomingMessage["ticketOffer"]?.isNotNull() ?: false
        return ticketOfferNeed && !ticketOffer
    }
}