import com.fasterxml.jackson.databind.JsonNode
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.isNotNull
import org.slf4j.LoggerFactory

fun main() {
    CustomerService().listen("CustomerService-listener-1")
}


class CustomerService : MessageListener() {
    override fun processMessage(originalMessage: RapidMessage): RapidMessage {
        val additionalData = mapOf("processed" to messageNodeFactory.booleanNode(true))
        return originalMessage.copyWithAdditionalData(this::class.simpleName!!, additionalData)
    }

    override fun shouldProcessMessage(incomingMessage: MessageData): Boolean {
        val productExists = incomingMessage["product"]?.isNotNull() ?: false
        val internalIdExists = incomingMessage["productInternalId"]?.isNotNull() ?: false
        val alreadyProcessed = incomingMessage["processed"]?.booleanValue() == true

        return productExists && internalIdExists && !alreadyProcessed
    }
}


