/*import no.jpro.kafkaworkshop.oppgave4.oppgave4a.*
import no.jpro.kafkaworkshop.oppgave4.oppgave4f.TicketOfficeService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class TicketOfficeServiceTest {

    private lateinit var mockMessageProducer: MessageProducer
    private lateinit var ticketOfficeService: TestableTicketOfficeService

    val expectedMessage = RapidMessage.fromData(
        TestableTicketOfficeService::class.simpleName.toString(), "ticketEvent", mapOf(
            "need" to RapidConfiguration.messageNodeFactory.textNode("ticketOffer")
        )
    )

    class TestableTicketOfficeService(messageProducer: MessageProducer) : TicketOfficeService(messageProducer) {
        public override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
            return super.shouldProcessMessage(incomingMessage)
        }

        public override fun processMessage(originalMessage: RapidMessage): RapidMessage {
            return super.processMessage(originalMessage)
        }
    }

    @BeforeEach
    fun setUp() {
        mockMessageProducer = Mockito.mock(MessageProducer::class.java)
        ticketOfficeService = TestableTicketOfficeService(mockMessageProducer)
    }

    @Test
    fun `should start and send a message with a need for ticketOffer`() {
        ticketOfficeService.start()

        verify(mockMessageProducer).send(expectedMessage)
    }

    @Test
    fun `should process message with a ticket offer and not already processed`() {
        val testData = mapOf(
            "ticketOffer" to RapidConfiguration.messageNodeFactory.textNode("VIP"),
            "processed" to RapidConfiguration.messageNodeFactory.booleanNode(false)
        )
        assertThat(ticketOfficeService.shouldProcessMessage(testData)).isTrue
    }

    @Test
    fun `should not process message without a ticket offer`() {
        val testData = mapOf(
            "processed" to RapidConfiguration.messageNodeFactory.booleanNode(false)
        )
        assertThat(ticketOfficeService.shouldProcessMessage(testData)).isFalse
    }

    @Test
    fun `should not process message that is already processed`() {
        val testData = mapOf(
            "ticketOffer" to RapidConfiguration.messageNodeFactory.textNode("VIP"),
            "processed" to RapidConfiguration.messageNodeFactory.booleanNode(true)
        )
        assertThat(ticketOfficeService.shouldProcessMessage(testData)).isFalse
    }

    @Test
    fun `should process message and mark it as processed`() {
        val originalMessage = RapidMessage.fromData(
            "TestApplication", "ticketEvent", mapOf(
                "ticketOffer" to RapidConfiguration.messageNodeFactory.textNode("VIP")
            )
        )
        val processedMessage = ticketOfficeService.processMessage(originalMessage)
        assertThat(processedMessage.payload["processed"]?.booleanValue()).isTrue
    }
}
 */
