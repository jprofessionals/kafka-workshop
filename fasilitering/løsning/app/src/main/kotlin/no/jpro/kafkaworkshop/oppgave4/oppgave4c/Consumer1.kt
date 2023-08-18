import com.fasterxml.jackson.databind.node.JsonNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
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
        ConsumerConfig.GROUP_ID_CONFIG to "rapidConsumer1-1",
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to "6000",
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to "6000",
        ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to "1000",

        )


    val consumerId1Mapping = mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")

    KafkaConsumer<String, String>(consumerProps).use { consumer ->
        consumer.subscribe(listOf(Rapid.topic))

        while (true) {
            val records = consumer.poll(Duration.ofMillis(100))
            records.forEach { record ->
                logger.info("Consumed record with key ${record.key()} and value ${record.value()}")
                val message: Rapid.RapidMessage? = Rapid.RapidMessage.MessageConverter().convertFromJson(record.value())
                if (message != null) {
                    logger.info("Consumer 1 Deserialized Message: $message")
                    val incomingMessage = message.messageData

                    if (shouldProcessMessage(incomingMessage)) {
                        val id = incomingMessage["key1"]

                        logger.info("Consumer 1 Processing message: $message")

                        val consumerId1Key = consumerId1Mapping.get(id?.get("producer1ItemId")?.asText())
                        val value = incomingMessage["key2"]

                        logger.info("Producer 1 message received: $id: id value: $value Mapping to Consumer1Id: $consumerId1Key")

                        val messageNodeFactory = JsonNodeFactory.instance

                        val newMessage = message.copy2(
                            Rapid.RapidMessage.ParticipatingSystem("consumer1"), mapOf(
                                "consumer1Id" to messageNodeFactory.objectNode().put("id", consumerId1Key)
                            )
                        )

                        logger.info("newMessage: ${newMessage.toJsonText()}")

                        val willNextMessageBeConsumedAgain = shouldProcessMessage(newMessage.messageData)
                        if(willNextMessageBeConsumedAgain) {
                            logger.error("Can not create new message, it will be consumed again and create a loop")
                        }
                    }
                } else {
                    logger.error("Error deserializing record value: ${record.value()}")
                }
            }
        }
    }


}

fun shouldProcessMessage(incomingMessage: MessageData): Boolean {
    val producer1Id = incomingMessage["key1"]
    val harProducer1Id = producer1Id != null && !producer1Id.isNull
    val consumer1Id = incomingMessage["consumer1Id"]
    val manglerConsumer1Id = consumer1Id != null && !consumer1Id.isNull

    return harProducer1Id && manglerConsumer1Id
}
