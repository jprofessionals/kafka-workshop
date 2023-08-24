package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import no.jpro.kafkaworkshop.logger
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Payload
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.isAutoCommitEnabled
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration

/**
 * Abstract class for Kafka message listeners.
 *
 * Implementations of this class can define specific message processing behaviors by
 * overriding the `shouldProcessMessage` and `processMessage` methods.
 */
abstract class MessageListener(private val messageProducer: MessageProducer = MessageProducer()) {

    /**
     * Sets the properties for Kafka consumer.
     *
     * @param consumerGroupId The consumer group ID for the consumer.
     * @return A map of Kafka consumer properties.
     */
    private fun consumerProperties(consumerGroupId: String) = mapOf(
        ConsumerConfig.GROUP_ID_CONFIG to consumerGroupId,
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to isAutoCommitEnabled,
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to "6000",
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to "6000",
        ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to "1000"
    )

    /**
     * Begins listening to Kafka for messages. Messages are consumed, checked if they should be processed,
     * and then processed.
     *
     * @param consumerGroupId The consumer group ID for the consumer.
     */
    fun listen(consumerGroupId: String) {
        consumeMessages(consumerGroupId, ::shouldProcessMessage, ::consumeRecord)
    }

    /**
     * Consumes messages from Kafka based on provided processing checks and record processing function.
     *
     * @param consumerGroupId Consumer group ID.
     * @param shouldProcess A lambda to determine if the message should be processed.
     * @param processRecord A lambda to process a consumed Kafka record.
     */
    private fun consumeMessages(
        consumerGroupId: String,
        shouldProcess: (Payload) -> Boolean,
        processRecord: (ConsumerRecord<String, String>, KafkaConsumer<String, String>) -> Unit
    ) {
        KafkaConsumer<String, String>(consumerProperties(consumerGroupId)).use { consumer ->
            consumer.subscribe(listOf(RapidConfiguration.topic))
            while (true) {
                val records = consumer.poll(Duration.ofMillis(100))
                for (record in records) {
                    val message = RapidMessage.convertFromJson(record.value())
                    if (message != null && shouldProcess(message.payload)) {
                        processRecord(record, consumer)
                    }
                }
            }
        }
    }

    /**
     * Consumes a record from Kafka. If the message from the record can be deserialized and should be processed,
     * it's then passed to `processMessage` for further actions. Offset commit is also managed manually.
     *
     * @param record The Kafka consumer record to be consumed.
     * @param consumer The Kafka consumer instance for committing offsets manually.
     */
    private fun consumeRecord(record: ConsumerRecord<String, String>, consumer: KafkaConsumer<String, String>) {
        try {
            val message = RapidMessage.convertFromJson(record.value())
            message?.let {
                if (shouldProcessMessage(it.payload)) {
                    val newMessage = processMessage(it)
                    if (newMessage != null) {
                        if (!shouldProcessMessage(newMessage.payload)) {
                            messageProducer.send(newMessage)
                            if (!isAutoCommitEnabled) {
                                consumer.commitSync()
                            }
                        } else {
                            logger().error("Cannot create new message; it will be consumed again and create a loop")
                        }
                    }
                }
            } ?: logger().error("Error deserializing record value: ${record.value()}")
        } catch (e: Exception) {
            logger().error("Exception while processing message: ${e.stackTraceToString()}")
            // throw e // Should throw and stop the application in a production environment
            // It is bettter to stop the application, fix the problem, and continue on the same message to ensure that no messages are lost
        }
    }

    /**
     * Checks whether the provided [incomingMessage] should be processed.
     *
     * @param incomingMessage The message data to check.
     * @return `true` if the message should be processed, `false` otherwise.
     */
    protected abstract fun shouldProcessMessage(incomingMessage: Payload): Boolean

    /**
     * Processes the [originalMessage] and returns a new message or null if no new message is created.
     *
     * Implementations should override this method to define specific processing behaviors.
     *
     * @param originalMessage The original message to be processed.
     * @return A new message or `null` if no new message is created.
     */
    protected abstract fun processMessage(originalMessage: RapidMessage): RapidMessage?
}
