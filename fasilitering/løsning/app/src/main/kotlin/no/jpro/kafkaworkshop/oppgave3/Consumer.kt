package no.jpro.kafkaworkshop.oppgave3

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import no.jpro.kafkaworkshop.oppgave3.Common.topic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration

class Consumer

fun main() {
    val logger = LoggerFactory.getLogger(Consumer::class.qualifiedName)

    val consumerProps = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.GROUP_ID_CONFIG to "kotlinConsumer-1",

        // ny konfigurasjon
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaAvroDeserializer::class.java.name,
        AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to "http://localhost:8081",
        KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG to true
    )

    KafkaConsumer<String, AvroMessage>(consumerProps).use { consumer ->
        consumer.subscribe(listOf(topic))

        while (true) {
            val records = consumer.poll(Duration.ofMillis(100))
            records.forEach { record ->
                logger.info("Consumed record with key ${record.key()} and value ${record.value()}")
                try {
                    val message: AvroMessage = record.value()
                    logger.info("Deserialized Message: $message")
                } catch (e: Exception) {
                    logger.error("Error deserializing record value: ${record.value()}", e)
                }
            }
        }
    }

}
