import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.messageNodeFactory
import org.slf4j.LoggerFactory

fun main() {
    val customerService = CustomerService()
    customerService.listen()
}

class CustomerService {
    fun listen() {
        val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.CustomerService")

        Rapid.consumeMessages(
            consumerGroupId = "rapidConsumer1-1",
            shouldProcess = ::shouldProcessMessage,
            processRecord = { record ->
                val message = Rapid.RapidMessage.MessageConverter().convertFromJson(record.value())
                message?.let {
                    val incomingMessage = it.messageData

                    if (shouldProcessMessage(incomingMessage)) {
                        val productInternalId = incomingMessage["productInternalId"]?.asText()
                        val product = incomingMessage["product"]
                        logger.info("message received with productExternalId: $productInternalId: $productInternalId value: $product")

                        val newMessage = message.copyWithAdditionalData(
                            this::class.simpleName!!, mapOf(
                                "processed" to messageNodeFactory.booleanNode(true)
                            )
                        )

                        if (!shouldProcessMessage(newMessage.messageData)) {
                            logger.info("Sending newMessage: ${newMessage.toJsonText()}")
                            Rapid.send(newMessage.toJsonText())
                        } else {
                            logger.error("Can not create new message, it will be consumed again and create a loop")
                        }
                    }
                } ?: logger.error("Error deserializing record value: ${record.value()}")
            }
        )
    }

    fun shouldProcessMessage(incomingMessage: MessageData): Boolean {
        val product = incomingMessage["product"]
        val hasProduct = product != null && !product.isNull
        val productInternalId = incomingMessage["productInternalId"]
        val hasProductInternalId = productInternalId != null && !productInternalId.isNull

        return hasProductInternalId && hasProduct
    }
}


