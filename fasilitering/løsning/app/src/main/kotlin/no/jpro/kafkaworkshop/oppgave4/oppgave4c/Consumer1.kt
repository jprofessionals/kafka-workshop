import com.fasterxml.jackson.databind.node.JsonNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import no.jpro.kafkaworkshop.oppgave4.oppgave4b.sampleProducerMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.consumer")
private val consumerId1Mapping = mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")
private val messageConverter = Rapid.RapidMessage.MessageConverter()

fun main() {
    logger.info("Start Consumer1")
    Rapid.consumeMessages(
        consumerGroupId = "rapidConsumer1-1",
        shouldProcess = ::shouldProcessMessage,
        processRecord = ::processRecord
    )
}

fun processRecord(record: ConsumerRecord<String, String>) {
    val message = messageConverter.convertFromJson(record.value())
    val incomingMessage = message?.messageData ?: return logger.error("Error deserializing record value: ${record.value()}")

    if (!shouldProcessMessage(incomingMessage)) return

    val id = incomingMessage["key1"]?.get("producer1ItemId")?.asText()
    val consumerId1Key = consumerId1Mapping[id]
    val value = incomingMessage["key2"]

    logger.info("Producer 1 message received: $id: id value: $value Mapping to Consumer1Id: $consumerId1Key")

    val newMessage = message.copy(
        Rapid.RapidMessage.ParticipatingSystem("consumer1"),
        mapOf("consumer1Id" to JsonNodeFactory.instance.objectNode().put("id", consumerId1Key))
    )

    if (shouldProcessMessage(newMessage.messageData)) {
        logger.error("Can not create new message, it will be consumed again and create a loop")
    } else {
        logger.info("Sending newMessage: ${newMessage.toJsonText()}")
        Rapid.send(newMessage.toJsonText())
    }
}

fun shouldProcessMessage(incomingMessage: MessageData): Boolean {
    val producer1IdExists = incomingMessage["key1"]?.takeIf { !it.isNull } != null
    val consumer1IdMissing = incomingMessage["consumer1Id"]?.takeIf { !it.isNull } == null

    return producer1IdExists && consumer1IdMissing
}
