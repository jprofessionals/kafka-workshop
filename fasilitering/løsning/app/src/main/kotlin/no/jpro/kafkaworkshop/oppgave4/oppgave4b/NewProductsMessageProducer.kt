package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Payload
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.objectMapper
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.slf4j.LoggerFactory

/**
 * Main entry point for the NewProductsMessageProducer application.
 */
fun main() {
    NewProductsMessageProducer().produceMessage()
}

/**
 * The `NewProductsMessageProducer` class is responsible for producing sample product messages.
 *
 * The class demonstrates how to create a sample product message and send it to the Kafka topic.
 */
class NewProductsMessageProducer {
    private val logger = LoggerFactory.getLogger(NewProductsMessageProducer::class.java)

    /**
     * Produces a sample product message and sends it.
     */
    fun produceMessage() {
        val sampleMessage = createSampleProductMessage()
        MessageProducer.send(sampleMessage)
    }

    /**
     * Creates a sample product message for demonstration purposes.
     *
     * This function constructs a sample message that contains information about a product.
     * The message contains the product's external ID and other details.
     *
     * @return A sample `RapidMessage` object with product information.
     */
    private fun createSampleProductMessage(): RapidMessage {
        val applicationName = this::class.simpleName.toString()

        val product = Product("car", "red")
        val payload: Payload = mapOf(
            "productExternalId" to messageNodeFactory.textNode("12"),
            "product" to objectMapper.valueToTree(product)
        )

        return RapidMessage.fromData(applicationName, "SampleEvent", payload)
    }

    /**
     * Data class representing a product.
     *
     * Contains simple attributes like the name and color of the product.
     *
     * @property name The name of the product.
     * @property color The color of the product.
     */
    data class Product(val name: String, val color: String)
}
