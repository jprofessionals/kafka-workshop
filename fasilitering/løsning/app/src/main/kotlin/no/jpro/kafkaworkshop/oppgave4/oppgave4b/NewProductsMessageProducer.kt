package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.objectMapper
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.slf4j.LoggerFactory

fun main() {
    NewProductsMessageProducer().produceMessage()
}

class NewProductsMessageProducer {
    private val logger = LoggerFactory.getLogger(NewProductsMessageProducer::class.java)

    fun produceMessage() {
        val sampleMessage = createSampleProductMessage()
        MessageProducer.send(sampleMessage)
    }

    private fun createSampleProductMessage(): RapidMessage {
        val applicationName = this::class.simpleName.toString()

        val product = Product("car", "red")
        val messageData: MessageData = mapOf(
            "productExternalId" to messageNodeFactory.textNode("12"),
            "product" to objectMapper.valueToTree(product)
        )

        return RapidMessage.fromData(applicationName, "SampleEvent", messageData)
    }

    private data class Product(val name: String, val color: String)
}
