/*package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import com.fasterxml.jackson.databind.JsonNode
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Payload
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MessageLoggerServiceTest {

    class TestableMessageLoggerService : MessageLoggerService() {
        public override fun processMessage(originalMessage: RapidMessage): RapidMessage? {
            return super.processMessage(originalMessage)
        }

        public override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
            return super.shouldProcessMessage(incomingMessage)
        }
    }

    private lateinit var messageLoggerService: TestableMessageLoggerService

    val testMessage = RapidMessage.fromData(
        "TestApplication", "SampleEvent", mapOf(
            "productExternalId" to RapidConfiguration.messageNodeFactory.textNode("12"),
            "product" to RapidConfiguration.objectMapper.valueToTree(NewProductsMessageProducer.Product("car", "red"))
        )
    )

    @BeforeEach
    fun setUp() {
        messageLoggerService = TestableMessageLoggerService()
    }

    @Test
    fun `processMessage should return null`() {

        val result = messageLoggerService.processMessage(testMessage)

        assertThat(result).isNull()
    }

    @Test
    fun `shouldProcessMessage should return true`() {
        val payload = (mapOf<String, JsonNode>())

        val result = messageLoggerService.shouldProcessMessage(payload)

        assertThat(result).isTrue()
    }

}
 */
