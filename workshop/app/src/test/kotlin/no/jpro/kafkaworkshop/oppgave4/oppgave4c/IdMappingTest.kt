/*import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Payload
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class IdMappingServiceTest {

    @Mock
    lateinit var messageProducer: MessageProducer

    class TestableIdMappingService(messageProducer: MessageProducer = MessageProducer()) :
        IdMappingService(messageProducer) {
        public override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
            return super.shouldProcessMessage(incomingMessage)
        }

        public override fun processMessage(originalMessage: RapidMessage): RapidMessage {
            return super.processMessage(originalMessage)
        }
    }

    lateinit var idMappingService: TestableIdMappingService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        idMappingService = TestableIdMappingService(messageProducer)
    }

    @Test
    fun `should not process message without external ID`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "productInternalId" to messageNodeFactory.textNode("A14"),
                "product" to messageNodeFactory.textNode("car")
            )
        )

        assertThat(idMappingService.shouldProcessMessage(testData.payload)).isFalse
    }

    @Test
    fun `should process message with external ID and without internal ID`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "productExternalId" to messageNodeFactory.textNode("10")
            )
        )

        assertThat(idMappingService.shouldProcessMessage(testData.payload)).isTrue
    }

    @Test
    fun `should not process message with both internal and external ID`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "productExternalId" to messageNodeFactory.textNode("10"),
                "productInternalId" to messageNodeFactory.textNode("A14")
            )
        )

        assertThat(idMappingService.shouldProcessMessage(testData.payload)).isFalse
    }

    @Test
    fun `should correctly map external to internal ID`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "productExternalId" to messageNodeFactory.textNode("10")
            )
        )

        val processedMessage = idMappingService.processMessage(testData)
        assertThat(processedMessage.payload["productInternalId"]?.asText()).isEqualTo("A14")
    }

    @Test
    fun `should leave message unchanged if external ID not in mapping`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "productExternalId" to messageNodeFactory.textNode("999")
            )
        )

        val processedMessage = idMappingService.processMessage(testData)
        assertThat(processedMessage.payload["productInternalId"]).isNull()
    }


}
 */
