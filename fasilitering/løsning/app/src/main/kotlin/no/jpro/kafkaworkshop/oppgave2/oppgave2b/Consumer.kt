package no.jpro.kafkaworkshop.oppgave2.oppgave2b

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.jpro.kafkaworkshop.oppgave2.oppgave2a.Common.Message
import no.jpro.kafkaworkshop.oppgave2.oppgave2a.Common.topic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration

fun main() {
    val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.consumer")

    val jacksonObjectMapper = jacksonObjectMapper()

    val consumerProps = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.GROUP_ID_CONFIG to "kotlinConsumer-1",
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to "6000",
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to "6000",
        ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to "1000"
    )

    KafkaConsumer<String, String>(consumerProps).use { consumer ->
        consumer.subscribe(listOf(topic))

        while (true) {
            val records = consumer.poll(Duration.ofMillis(100))
            records.forEach { record ->
                logger.info("Consumed record with key ${record.key()} and value ${record.value()}")
                try {
                    val message: Message = jacksonObjectMapper.readValue(record.value(), Message::class.java)
                    logger.info("Deserialized Message: $message")
                } catch (e: Exception) {
                    logger.error("Error deserializing record value: ${record.value()}", e)
                }
            }
        }
    }
}