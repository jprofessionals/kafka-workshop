
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
fun main() {
    val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.consumer")

    val consumerProps = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.GROUP_ID_CONFIG to "rapidConsumer1-1"
    )

    KafkaConsumer<String, String>(consumerProps).use { consumer ->
        consumer.subscribe(listOf(Rapid.topic ))

        while (true) {
            val records = consumer.poll(Duration.ofMillis(100))
            records.forEach { record ->
                logger.info("Consumed record with key ${record.key()} and value ${record.value()}")
                val message: Rapid.RapidMessage? = Rapid.RapidMessage.MessageConverter().convertFromJson(record.value())
                if (message != null) {
                    logger.info("Deserialized Message: $message")
                    val producer1Message  = message.message
                    val id = producer1Message["key1"]
                    val value = producer1Message["key2"]
                    logger.info(" Producer 1 message received: ${id}: id value: ${value}")
                } else {
                    logger.error("Error deserializing record value: ${record.value()}")
                }
            }
        }
    }
}