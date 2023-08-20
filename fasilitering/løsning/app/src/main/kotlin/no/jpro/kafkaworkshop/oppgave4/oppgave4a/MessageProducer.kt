package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import no.jpro.kafkaworkshop.logger
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

/**
 * A utility class for producing Kafka messages to a specified topic.
 *
 * This class provides methods to send `RapidMessage` objects to a Kafka topic.
 * It leverages a companion object to ensure shared producer properties across all instances.
 */
class MessageProducer {

    companion object {

        /**
         * Creates and returns the properties for Kafka producer.
         *
         * @return A map containing Kafka producer configurations.
         */
        private fun producerProperties() = mapOf<String, Any>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "PLAINTEXT"
        )

    }
    /**
     * Sends the provided [rapidMessage] to a Kafka topic as defined by `RapidConfiguration.topic`.
     *
     * This method serializes the `RapidMessage` object to JSON and then sends it to the Kafka topic.
     * If any errors are encountered during the sending process, they are logged.
     *
     * @param rapidMessage The `RapidMessage` object to be sent to Kafka.
     */
    fun send(rapidMessage: RapidMessage) {
        KafkaProducer<String, String>(producerProperties()).use { producer ->
            try {
                val jsonMessage = rapidMessage.toJsonText()
                val record = ProducerRecord(RapidConfiguration.topic, "", jsonMessage)

                logger().info("Sending message $jsonMessage")
                producer.send(record)
                logger().info("Message has been sent")

            } catch (e: Exception) {
                logger().error("Error sending message $rapidMessage", e)
            }
        }
    }
}
