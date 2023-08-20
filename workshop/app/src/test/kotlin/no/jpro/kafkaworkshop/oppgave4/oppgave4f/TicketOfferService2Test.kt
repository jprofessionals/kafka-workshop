/*import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Payload
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import no.jpro.kafkaworkshop.oppgave4.oppgave4f.TicketOfferService2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class TicketOfferService2Test {

    private lateinit var mockMessageProducer: MessageProducer
    private lateinit var ticketOfferService2: TestableTicketOfferService2

    class TestableTicketOfferService2(messageProducer: MessageProducer) : TicketOfferService2(messageProducer) {
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
        ticketOfferService2 = TestableTicketOfferService2(mockMessageProducer)
    }

    @Test
    fun `should process message with a need for ticketOffer and no existing ticket offer`() {
        val testData = mapOf(
            "need" to RapidConfiguration.messageNodeFactory.textNode("ticketOffer")
        )
        assertThat(ticketOfferService2.shouldProcessMessage(testData)).isTrue
    }

    @Test
    fun `should not process message with existing ticket offer`() {
        val testData = mapOf(
            "need" to RapidConfiguration.messageNodeFactory.textNode("ticketOffer"),
            "ticketOffer" to RapidConfiguration.messageNodeFactory.textNode("VIP")
        )
        assertThat(ticketOfferService2.shouldProcessMessage(testData)).isFalse
    }

    @Test
    fun `should process message and add ticketOffer`() {
        val originalMessage = RapidMessage.fromData(
            "TestApplication", "ticketEvent", mapOf(
                "need" to RapidConfiguration.messageNodeFactory.textNode("ticketOffer")
            )
        )
        val processedMessage = ticketOfferService2.processMessage(originalMessage)
        assertThat(processedMessage.payload["ticketOffer"]?.numberValue()).isEqualTo(25)
    }
}
*/

