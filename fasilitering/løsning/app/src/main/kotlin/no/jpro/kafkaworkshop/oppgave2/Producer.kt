package no.jpro.kafkaworkshop.oppgave2

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.jpro.kafkaworkshop.oppgave2.Common.logger
import no.jpro.kafkaworkshop.oppgave2.Common.objectMapper
import no.jpro.kafkaworkshop.oppgave2.Common.topic

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

object Common {
    val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop")
    val objectMapper = jacksonObjectMapper()
    const val topic = "kotlin_topic"

    data class Message(val id: String, val value: String)
}
fun main() {
    val producerProperties = mapOf<String, Any>(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "PLAINTEXT"
    )

    KafkaProducer<String, String>(producerProperties).use { producer ->
        val message = Common.Message(id = "1", value = "a value")
        val jsonMessage: String = objectMapper.writeValueAsString(message)
        val record = ProducerRecord(topic, "key", jsonMessage)

        try {
            logger.info("Sending message $jsonMessage")
            producer.send(record)
            logger.info("Message has been sent")
        } catch (e: Exception) {
            logger.error("Error sending message $jsonMessage", e)
        }
    }
}