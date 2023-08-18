
import no.jpro.kafkaworkshop.oppgave2.oppgave2d.Message
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
fun main() {
    val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.consumer")
    val topic = "kotlin_topic"
    val converter = Message.MessageConverter()

    val consumerProps = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.GROUP_ID_CONFIG to "kotlinConsumer-1"
    )

    KafkaConsumer<String, String>(consumerProps).use { consumer ->
        consumer.subscribe(listOf(topic ))

        while (true) {
            val records = consumer.poll(Duration.ofMillis(100))
            records.forEach { record ->
                logger.info("Consumed record with key ${record.key()} and value ${record.value()}")
                val message: Message? = converter.convertFromJson(record.value())
                if (message != null) {
                    logger.info("Deserialized Message: $message")
                } else {
                    logger.error("Error deserializing record value: ${record.value()}")
                }
            }
        }
    }
}