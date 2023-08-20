package no.jpro.kafkaworkshop.oppgave3

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import no.jpro.kafkaworkshop.oppgave3.Common.topic
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

object Common {
    const val topic = "avro-messages"
}

class Producer

fun main() {
    val logger = LoggerFactory.getLogger(Producer::class.qualifiedName)

    val producerProperties = mapOf<String, Any>(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,

        // ny konfigurasjon
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaAvroSerializer::class.java.name,
        AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to "http://localhost:8081",
        KafkaAvroSerializerConfig.AVRO_REMOVE_JAVA_PROPS_CONFIG to true
    )

    KafkaProducer<String, AvroMessage>(producerProperties).use { producer ->
        val message = AvroMessage("1", "a value")
        val record = ProducerRecord(topic, "key", message)

        try {
            logger.info("Sending message $message")
            producer.send(record)
            logger.info("Message has been sent")
        } catch (e: Exception) {
            logger.error("Error sending message $message", e)
        }
    }
}
