package no.jpro.kafkaworkshop.oppgave3

import no.jpro.kafkaworkshop.Common.Message
import no.jpro.kafkaworkshop.Common.logger
import no.jpro.kafkaworkshop.Common.objectMapper
import no.jpro.kafkaworkshop.Common.topic
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration

fun main() {
    val consumerProps = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name, // Note: Changed this from StringSerializer to StringDeserializer
        ConsumerConfig.GROUP_ID_CONFIG to "kotlinConsumer-1"
    )

    val consumer: Consumer<String, String> = KafkaConsumer(consumerProps)
    consumer.subscribe(listOf(topic))

    while (true) {
        val records = consumer.poll(Duration.ofMillis(100))
        records.forEach { record ->
            logger.info("Consumed record with key ${record.key()} and value ${record.value()}")

            try {
                val message: Message = objectMapper.readValue(record.value(), Message::class.java)
                logger.info("Deserialized Melding: $message")
            } catch (e: Exception) {
                logger.error("Error deserializing record value: ${record.value()}", e)
            }
        }
    }
}
