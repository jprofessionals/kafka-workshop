import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.messageNodeFactory
import org.slf4j.LoggerFactory

fun main() {
    val idMapping = IdMapping()
    idMapping.listen()
}

class IdMapping {
    fun listen() {
        val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.IdMapping")

        Rapid.consumeMessages(
            consumerGroupId = "rapidConsumer1-1",
            shouldProcess = ::shouldProcessMessage,
            processRecord = { record ->
                val message = Rapid.RapidMessage.MessageConverter().convertFromJson(record.value())
                message?.let {
                    val incomingMessage = it.messageData

                    if (shouldProcessMessage(incomingMessage)) {
                        val productExternalId = incomingMessage["productExternalId"]?.asText()
                        val productInternalId = tilProductInternalId(productExternalId)
                        val value = incomingMessage["product"]
                        logger.info("message received with productExternalId: $productExternalId value: $value Mapping to Consumer1Id: $productInternalId")

                        val newMessage = message.copyWithAdditionalData(
                            this::class.simpleName!!,
                            mapOf(
                                "productInternalId" to messageNodeFactory.textNode(productInternalId)
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
        val productExternalId = incomingMessage["productExternalId"]
        val harProductExternalId = productExternalId != null && !productExternalId.isNull
        val productInternalId = incomingMessage["productInternalId"]
        val harProductInternalId = productInternalId != null && !productInternalId.isNull

        return harProductExternalId && !harProductInternalId
    }

    fun tilProductInternalId(productExternalId: String?) =
        mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")[productExternalId]
}


