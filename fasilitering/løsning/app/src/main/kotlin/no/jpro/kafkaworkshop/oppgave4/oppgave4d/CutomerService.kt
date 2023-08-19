import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory

fun main() {
    CustomerService().listen("CustomerService-listener-1")
}

class CustomerService : MessageListener() {
    private val logger = LoggerFactory.getLogger("com.jpro.kafkaworkshop.CustomerService")

    override fun processIncomingMessage(
        record: ConsumerRecord<String, String>,
        incomingMessage: MessageData,
        message: RapidMessage
    ) {
        logger.info("Processing message")
        val productInternalId = incomingMessage["productInternalId"]?.asText()
        val product = incomingMessage["product"]
        logger.info("message received with productInternalId: $productInternalId value: $product")
    }

    override fun createNewMessage(
        incomingMessage: MessageData,
        originalMessage: RapidMessage
    ): RapidMessage? {
        return originalMessage.copyWithAdditionalData(
            this::class.simpleName!!,
            mapOf("processed" to messageNodeFactory.booleanNode(true))
        )
    }

    override fun shouldProcessMessage(incomingMessage: MessageData): Boolean {
        val product = incomingMessage["product"]
        val hasProduct = product != null && !product.isNull
        val productInternalId = incomingMessage["productInternalId"]
        val hasProductInternalId = productInternalId != null && !productInternalId.isNull
        val isProcessed = incomingMessage["processed"]?.takeIf { !it.isNull }?.booleanValue() == true
        return hasProductInternalId && hasProduct && !isProcessed
    }
}
