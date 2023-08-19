package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.objectMapper
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.slf4j.LoggerFactory

fun main() {
    NewProductsMessageProducer().produceMessage()
}

class NewProductsMessageProducer {
    private val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.NewProductsMessageProducer")
    fun produceMessage() {
        MessageProducer.send(sampleProductMessage(this::class.simpleName.toString()))
    }

    fun sampleProductMessage(applicationName: String): RapidMessage {

        data class Product(val name: String, val color: String)

        val messageData: MessageData = mapOf(
            "productExternalId" to messageNodeFactory.textNode("12"),
            "product" to objectMapper.valueToTree(Product("car", "red"))
        )

        return RapidMessage(
            eventName = "SampleEvent",
            messageData = messageData,
            participatingSystems = listOf(RapidMessage.ParticipatingSystem(applicationName))
        )
    }
}

