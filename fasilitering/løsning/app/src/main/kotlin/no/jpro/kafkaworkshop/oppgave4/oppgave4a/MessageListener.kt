import no.jpro.kafkaworkshop.logger
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration

abstract class MessageListener {

    protected abstract fun shouldProcessMessage(incomingMessage: MessageData): Boolean

    private fun consumerProperties(consumerGroupId: String) = mapOf(
        ConsumerConfig.GROUP_ID_CONFIG to consumerGroupId,
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to "6000",
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to "6000",
        ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to "1000"
    )

    fun listen(consumerGroupId: String) {
        consumeMessages(consumerGroupId, ::shouldProcessMessage, ::consumeRecord)
    }

    private fun consumeMessages(
        consumerGroupId: String,
        shouldProcess: (MessageData) -> Boolean,
        processRecord: (ConsumerRecord<String, String>) -> Unit
    ) {
        logger().info("consumeMessages")
        KafkaConsumer<String, String>(consumerProperties(consumerGroupId)).use { consumer ->
            consumer.subscribe(listOf(RapidConfiguration.topic))
            while (true) {
                val records = consumer.poll(Duration.ofMillis(100))
                records.forEach { record ->
                    val message = RapidMessage.convertFromJson(record.value())
                    if (message != null && shouldProcess(message.messageData)) {
                        processRecord(record)
                    }
                }
            }
        }
    }

    private fun consumeRecord(record: ConsumerRecord<String, String>) {
        val message = RapidMessage.convertFromJson(record.value())
        message?.let {
            if (shouldProcessMessage(it.messageData)) {
                val newMessage = processMessage(it)
                if (newMessage != null && !shouldProcessMessage(newMessage.messageData)) {
                    MessageProducer.send(newMessage)
                } else {
                    logger().error("Cannot create new message; it will be consumed again and create a loop")
                }
            }
        } ?: logger().error("Error deserializing record value: ${record.value()}")
    }

    protected abstract fun processMessage(originalMessage: RapidMessage): RapidMessage?
}
