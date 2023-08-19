import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration

abstract class MessageListener {

    private val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.MessageListener")


    protected abstract fun messageWillBeProcessed(incomingMessage: MessageData): Boolean

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

        consumeMessages(consumerGroupId = consumerGroupId,
            shouldProcess = ::messageWillBeProcessed,
            processRecord = { record ->
                val message = Rapid.messageConverter.convertFromJson(record.value())
                message?.let {
                    val incomingMessage = it.messageData

                    if (messageWillBeProcessed(incomingMessage)) {
                        processIncomingMessage(record)
                    }
                } ?: logger.error("Error deserializing record value: ${record.value()}")
            })


    }

    fun consumeMessages(
        consumerGroupId: String,
        shouldProcess: (MessageData) -> Boolean,
        processRecord: (ConsumerRecord<String, String>) -> Unit
    ) {
        logger.info("consumeMessages")
        KafkaConsumer<String, String>(consumerProperties(consumerGroupId)).use { consumer ->
            consumer.subscribe(listOf(Rapid.topic))

            while (true) {
                val records = consumer.poll(Duration.ofMillis(100))
                records.forEach { record ->
                    val message: RapidMessage? = Rapid.messageConverter.convertFromJson(record.value())
                    if (message != null && shouldProcess(message.messageData)) {
                        processRecord(record)
                    }
                }
            }
        }
    }

    protected abstract fun processIncomingMessage(
        record: ConsumerRecord<String, String>
    )
}

