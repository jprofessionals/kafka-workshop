package no.jpro.kafkaworkshop.oppgave4.oppgave4a

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

typealias MessageData = Map<String, JsonNode>

class Rapid {

    companion object {
        private val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.Rapid")
        private const val topic: String = "rapid-1"
        val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
        private val messageConverter = RapidMessage.MessageConverter()
        val messageNodeFactory = JsonNodeFactory.instance

        private fun producerProperties() = mapOf<String, Any>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "PLAINTEXT"
        )

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

        fun send(jsonMessage: String) {
            val record = ProducerRecord(topic, "", jsonMessage)
            KafkaProducer<String, String>(producerProperties()).use { producer ->
                try {
                    logger.info("Sending message $jsonMessage")
                    producer.send(record)
                    logger.info("Message has been sent")
                } catch (e: Exception) {
                    logger.error("Error sending message $jsonMessage", e)
                }
            }
        }

        fun consumeMessages(
            consumerGroupId: String,
            shouldProcess: (MessageData) -> Boolean,
            processRecord: (ConsumerRecord<String, String>) -> Unit
        ) {
            logger.info("consumeMessages")
            KafkaConsumer<String, String>(consumerProperties(consumerGroupId)).use { consumer ->
                consumer.subscribe(listOf(topic))

                while (true) {
                    val records = consumer.poll(Duration.ofMillis(100))
                    records.forEach { record ->
                        val message: RapidMessage? = messageConverter.convertFromJson(record.value())
                        if (message != null && shouldProcess(message.messageData)) {
                            processRecord(record)
                        }
                    }
                }
            }
        }
    }

    data class RapidMessage(
        val eventName: String,
        val messageData: MessageData,
        val participatingSystems: List<ParticipatingSystem>
    ) {
        data class ParticipatingSystem(
            val applikasjonsnavn: String,
            val timestamp: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())
        )

        class MessageConverter {
            fun convertFromJson(json: String): RapidMessage? {
                return try {
                    objectMapper.readValue(json, RapidMessage::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        }

        fun toJsonText() = objectMapper.writeValueAsString(this)

        fun copyWithAdditionalData(
            participatingSystem: ParticipatingSystem,
            addMessageData: MessageData
        ): RapidMessage {
            return this.copy(
                participatingSystems = participatingSystems + participatingSystem,
                messageData = messageData + addMessageData
            )
        }
    }
}
