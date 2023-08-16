package no.jpro.kafkaworkshop.oppgave2

import no.jpro.kafkaworkshop.Common.Message
import no.jpro.kafkaworkshop.Common.objectMapper
import no.jpro.kafkaworkshop.Common.topic
import no.jpro.kafkaworkshop.Common.logger

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

fun main() {
    val producerProperties = mapOf<String, Any>(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "PLAINTEXT"
    )

    val producer = KafkaProducer<String, String>(producerProperties)

    val message = Message(id = "1", value = "a value")

    val jsonMessage: String = objectMapper.writeValueAsString(message)
    val record = ProducerRecord(topic, "key", jsonMessage)

    logger.info("Sending message $jsonMessage")
    producer.send(record)
    logger.info("Message has been sent")

    producer.close()
}