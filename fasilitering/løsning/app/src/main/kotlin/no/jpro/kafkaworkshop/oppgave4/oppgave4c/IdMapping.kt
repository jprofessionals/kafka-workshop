import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory

fun main() {
    IdMapping().listen("IdMapping-listener-1")
}

class IdMapping : MessageListener() {
    private val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.IdMapping")

    override fun processIncomingMessage(
        record: ConsumerRecord<String, String>,
    ) {
        val message = RapidMessage.MessageConverter().convertFromJson(record.value())
        message?.let {
            val incomingMessage = it.messageData

            if (messageWillBeProcessed(incomingMessage)) {
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

                if (!messageWillBeProcessed(newMessage.messageData)) {
                    MessageProducer.send(newMessage)
                } else {
                    logger.error("Can not create new message, it will be consumed again and create a loop")
                }
            }
        } ?: logger.error("Error deserializing record value: ${record.value()}")
    }

    override fun messageWillBeProcessed(incomingMessage: MessageData): Boolean {
        val productExternalId = incomingMessage["productExternalId"]
        val harProductExternalId = productExternalId != null && !productExternalId.isNull
        val productInternalId = incomingMessage["productInternalId"]
        val harProductInternalId = productInternalId != null && !productInternalId.isNull

        return harProductExternalId && !harProductInternalId
    }

    fun tilProductInternalId(productExternalId: String?) =
        mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")[productExternalId]
}


