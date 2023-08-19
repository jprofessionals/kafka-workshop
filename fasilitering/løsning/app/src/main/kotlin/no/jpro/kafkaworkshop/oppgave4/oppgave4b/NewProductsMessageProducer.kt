package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.objectMapper

fun main() {
    val newProductsMessageProducer = NewProductsMessageProducer()
    newProductsMessageProducer.produceMessage()
}

class NewProductsMessageProducer {
    fun produceMessage() {
        Rapid.send(sampleProductMessage(this::class.simpleName.toString()).toJsonText())
    }

    fun sampleProductMessage(applicationName: String): Rapid.RapidMessage {

        data class Product(val name: String, val color: String)

        val messageData: MessageData = mapOf(
            "productExternalId" to messageNodeFactory.textNode("12"),
            "product" to objectMapper.valueToTree(Product("car", "red"))
        )

        return Rapid.RapidMessage(
            eventName = "SampleEvent",
            messageData = messageData,
            participatingSystems = listOf(Rapid.RapidMessage.ParticipatingSystem(applicationName))
        )
    }
}

