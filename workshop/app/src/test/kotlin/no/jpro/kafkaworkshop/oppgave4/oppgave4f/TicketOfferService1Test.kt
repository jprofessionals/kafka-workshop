/*import no.jpro.kafkaworkshop.oppgave4.oppgave4a.*
import no.jpro.kafkaworkshop.oppgave4.oppgave4f.TicketOfferService1
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class TicketOfferService1Test {

    private lateinit var mockMessageProducer: MessageProducer
    private lateinit var ticketOfferService1: TestableTicketOfferService1

    class TestableTicketOfferService1(messageProducer: MessageProducer) : TicketOfferService1(messageProducer) {
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
        ticketOfferService1 = TestableTicketOfferService1(mockMessageProducer)
    }

    @Test
    fun `should process message with a need for ticketOffer and no existing ticket offer`() {
        val testData = mapOf(
            "need" to RapidConfiguration.messageNodeFactory.textNode("ticketOffer")
        )
        assertThat(ticketOfferService1.shouldProcessMessage(testData)).isTrue
    }

    @Test
    fun `should not process message with existing ticket offer`() {
        val testData = mapOf(
            "need" to RapidConfiguration.messageNodeFactory.textNode("ticketOffer"),
            "ticketOffer" to RapidConfiguration.messageNodeFactory.textNode("VIP")
        )
        assertThat(ticketOfferService1.shouldProcessMessage(testData)).isFalse
    }

    @Test
    fun `should process message and add ticketOffer`() {
        val originalMessage = RapidMessage.fromData(
            "TestApplication", "ticketEvent", mapOf(
                "need" to RapidConfiguration.messageNodeFactory.textNode("ticketOffer")
            )
        )
        val processedMessage = ticketOfferService1.processMessage(originalMessage)
        assertThat(processedMessage.payload["ticketOffer"]?.numberValue()).isEqualTo(22)
    }
}*/
