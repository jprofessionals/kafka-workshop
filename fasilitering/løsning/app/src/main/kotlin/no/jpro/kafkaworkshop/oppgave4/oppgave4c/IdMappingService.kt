import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.isNotNull

fun main() {
    IdMapping().listen("IdMapping-listener-1")
}

class IdMapping : MessageListener() {

    companion object {
        private val ID_MAP = mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")
    }

    override fun shouldProcessMessage(incomingMessage: MessageData): Boolean {
        val hasExternalId = incomingMessage["productExternalId"]?.isTextual ?: false
        val lacksInternalId = !incomingMessage["productInternalId"].isNotNull()

        return hasExternalId && lacksInternalId
    }

    override fun processMessage(originalMessage: RapidMessage): RapidMessage {
        val externalId = originalMessage.messageData["productExternalId"]?.asText()
        val internalId = mapExternalIdToInternal(externalId)

        return originalMessage.copyWithAdditionalData(
            this::class.simpleName!!,
            mapOf("productInternalId" to messageNodeFactory.textNode(internalId))
        )
    }

    private fun mapExternalIdToInternal(externalId: String?): String? {
        return ID_MAP[externalId]
    }
}