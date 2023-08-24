import no.jpro.kafkaworkshop.oppgave4.needdemo.TravelService
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class TravelServiceTest {

    private lateinit var mockMessageProducer: MessageProducer
    private lateinit var travelService: TestableTravelService

    val expectedMessage = RapidMessage.fromData(
        TestableTravelService::class.simpleName.toString(), "ticketEvent", mapOf(
            "need" to RapidConfiguration.messageNodeFactory.textNode("ticketOffer")
        )
    )

    class TestableTravelService(messageProducer: MessageProducer) : TravelService(messageProducer) {
        public override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
            return super.shouldProcessMessage(incomingMessage)
        }

        public override fun processMessage(originalMessage: RapidMessage): RapidMessage? {
            return super.processMessage(originalMessage)
        }
    }

    @BeforeEach
    fun setUp() {
        mockMessageProducer = Mockito.mock(MessageProducer::class.java)
        travelService = TestableTravelService(mockMessageProducer)
    }

    @Test
    fun `should start and send a message with a need for ticketOffer`() {
        travelService.start()

        verify(mockMessageProducer).send(expectedMessage)
    }

    @Test
    fun `should process message with a ticket offer and not already processed`() {
        val testData = mapOf(
            "ticketOffer" to RapidConfiguration.messageNodeFactory.textNode("VIP"),
            "processed" to RapidConfiguration.messageNodeFactory.booleanNode(false)
        )
        assertThat(travelService.shouldProcessMessage(testData)).isTrue
    }

    @Test
    fun `should not process message without a ticket offer`() {
        val testData = mapOf(
            "processed" to RapidConfiguration.messageNodeFactory.booleanNode(false)
        )
        assertThat(travelService.shouldProcessMessage(testData)).isFalse
    }

    @Test
    fun `should not process message that is already processed`() {
        val testData = mapOf(
            "ticketOffer" to RapidConfiguration.messageNodeFactory.textNode("VIP"),
            "processed" to RapidConfiguration.messageNodeFactory.booleanNode(true)
        )
        assertThat(travelService.shouldProcessMessage(testData)).isFalse
    }

    @Test
    fun `should process message and mark it as processed`() {
        val originalMessage = RapidMessage.fromData(
            "TestApplication", "ticketEvent", mapOf(
                "ticketOffer" to RapidConfiguration.messageNodeFactory.textNode("VIP")
            )
        )
        val processedMessage = travelService.processMessage(originalMessage)
        assertThat(processedMessage!!.payload["processed"]?.booleanValue()).isTrue
    }
}
