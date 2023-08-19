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
        incomingMessage: MessageData,
        message: RapidMessage
    ) {
        val productExternalId = incomingMessage["productExternalId"]?.asText()
        val productInternalId = toProductInternalId(productExternalId)
        val value = incomingMessage["product"]
        logger.info("message received with productExternalId: $productExternalId value: $value Mapping to Consumer1Id: $productInternalId")
    }

    override fun createNewMessage(
        incomingMessage: MessageData,
        originalMessage: RapidMessage
    ): RapidMessage? {
        val productInternalId = toProductInternalId(incomingMessage["productExternalId"]?.asText())
        return originalMessage.copyWithAdditionalData(
            this::class.simpleName!!,
            mapOf("productInternalId" to messageNodeFactory.textNode(productInternalId))
        )
    }

    override fun shouldProcessMessage(incomingMessage: MessageData): Boolean {
        val productExternalId = incomingMessage["productExternalId"]
        val harProductExternalId = productExternalId != null && !productExternalId.isNull
        val productInternalId = incomingMessage["productInternalId"]
        val harProductInternalId = productInternalId != null && !productInternalId.isNull
        return harProductExternalId && !harProductInternalId
    }

    private fun toProductInternalId(productExternalId: String?): String? {
        return mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")[productExternalId]
    }
}