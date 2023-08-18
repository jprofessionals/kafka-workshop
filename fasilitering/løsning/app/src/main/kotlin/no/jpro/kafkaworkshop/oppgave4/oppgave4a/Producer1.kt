package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.jpro.kafkaworkshop.oppgave2.oppgave2a.Common.topic

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.producer")

    val producerProperties = mapOf<String, Any>(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "PLAINTEXT"
    )

    KafkaProducer<String, String>(producerProperties).use { producer ->
        val jsonMessage = sampleProducerMessage().toJsonText()
        val record = ProducerRecord(Rapid.rapidtopic, "", jsonMessage)

        try {
            logger.info("Sending message $jsonMessage")
            producer.send(record)
            logger.info("Message has been sent")
        } catch (e: Exception) {
            logger.error("Error sending message $jsonMessage", e)
        }
    }
}