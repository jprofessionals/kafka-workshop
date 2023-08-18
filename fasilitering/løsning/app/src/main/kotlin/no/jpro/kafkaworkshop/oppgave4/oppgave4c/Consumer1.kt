import com.fasterxml.jackson.databind.node.JsonNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import no.jpro.kafkaworkshop.oppgave4.oppgave4b.sampleProducerMessage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration

fun main() {
    val consumerId1Mapping = mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")
    val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.consumer")
    logger.info("Start Consumer1")
    Rapid.consumeMessages(
        consumerGroupId = "rapidConsumer1-1",
        shouldProcess = ::shouldProcessMessage,
        processRecord = { record ->
            val message: Rapid.RapidMessage? = Rapid.RapidMessage.MessageConverter().convertFromJson(record.value())
            if (message != null) {
                val incomingMessage = message.messageData

                if (shouldProcessMessage(incomingMessage)) {
                    val id = incomingMessage["key1"]
                    val consumerId1Key = consumerId1Mapping.get(id?.get("producer1ItemId")?.asText())
                    val value = incomingMessage["key2"]
                    logger.info("Producer 1 message received: $id: id value: $value Mapping to Consumer1Id: $consumerId1Key")
                    val messageNodeFactory = JsonNodeFactory.instance

                    val newMessage = message.copy(
                        Rapid.RapidMessage.ParticipatingSystem("consumer1"), mapOf(
                            "consumer1Id" to messageNodeFactory.objectNode().put("id", consumerId1Key)
                        )
                    )


                    val willNextMessageBeConsumedAgain = shouldProcessMessage(newMessage.messageData)
                    if (willNextMessageBeConsumedAgain) {
                        logger.error("Can not create new message, it will be consumed again and create a loop")
                    } else {
                        logger.info("Sending newMessage: ${newMessage.toJsonText()}")
                        Rapid.send(newMessage.toJsonText())
                    }
                }
            } else {
                logger.error("Error deserializing record value: ${record.value()}")
            }
        }
    )
}

fun shouldProcessMessage(incomingMessage: MessageData): Boolean {
    val producer1Id = incomingMessage["key1"]
    val harProducer1Id = producer1Id != null && !producer1Id.isNull
    val consumer1Id = incomingMessage["consumer1Id"]
    val manglerConsumer1Id = consumer1Id == null || consumer1Id.isNull

    return harProducer1Id && manglerConsumer1Id
}
