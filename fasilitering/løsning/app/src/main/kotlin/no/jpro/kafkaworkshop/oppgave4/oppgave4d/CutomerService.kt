import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory

fun main() {
    CustomerService().listen("CustomerService-listener-1")
}

class CustomerService : MessageListener() {
    val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.IdMapping")

    override fun processIncomingMessage(
        record: ConsumerRecord<String, String>
    ) {
        val message = RapidMessage.MessageConverter().convertFromJson(record.value())
        message?.let {
            val incomingMessage = it.messageData

            if (messageWillBeProcessed(incomingMessage)) {
                logger.info("Processing message")
                val productInternalId = incomingMessage["productInternalId"]?.asText()
                val product = incomingMessage["product"]
                logger.info("message received with productExternalId: $productInternalId: $productInternalId value: $product")

                val newMessage = message.copyWithAdditionalData(
                    this::class.simpleName!!, mapOf(
                        "processed" to messageNodeFactory.booleanNode(true)
                    )
                )

                val nextWillBeProcessedAgain = messageWillBeProcessed(newMessage.messageData)
                logger.info("nextWillBeProcessedAgain: $nextWillBeProcessedAgain")

                if (!nextWillBeProcessedAgain) {
                    logger.info("Sending newMessage: ${newMessage.toJsonText()}")
                    MessageProducer.send(newMessage)
                } else {
                    logger.error("Can not create new message, it will be consumed again and create a loop")
                }
            }
        } ?: logger.error("Error deserializing record value: ${record.value()}")
    }

    override fun messageWillBeProcessed(incomingMessage: MessageData): Boolean {
        val product = incomingMessage["product"]
        val hasProduct = product != null && !product.isNull
        val productInternalId = incomingMessage["productInternalId"]
        val hasProductInternalId = productInternalId != null && !productInternalId.isNull
        val isProcessed = incomingMessage["processed"]?.takeIf { !it.isNull }?.booleanValue() == true
        val willBeProcessed = hasProductInternalId && hasProduct && !isProcessed

        return willBeProcessed
    }
}


