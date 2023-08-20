import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Payload
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration.Companion.messageNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CustomerServiceTest {

    @Mock
    lateinit var messageProducer: MessageProducer

    class TestableCustomerService(messageProducer: MessageProducer = MessageProducer()) :
        CustomerService(messageProducer) {
        public override fun shouldProcessMessage(incomingMessage: Payload): Boolean {
            return super.shouldProcessMessage(incomingMessage)
        }

        public override fun processMessage(originalMessage: RapidMessage): RapidMessage {
            return super.processMessage(originalMessage)
        }
    }

    lateinit var customerService: TestableCustomerService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        customerService = TestableCustomerService(messageProducer)
    }

    @Test
    fun `should not process message without product`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "productInternalId" to messageNodeFactory.textNode("A14")
            )
        )

        assertThat(customerService.shouldProcessMessage(testData.payload)).isFalse
    }

    @Test
    fun `should not process message without internal ID`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "product" to messageNodeFactory.textNode("car")
            )
        )

        assertThat(customerService.shouldProcessMessage(testData.payload)).isFalse
    }

    @Test
    fun `should not process message already processed`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "product" to messageNodeFactory.textNode("car"),
                "productInternalId" to messageNodeFactory.textNode("A14"),
                "processed" to messageNodeFactory.booleanNode(true)
            )
        )

        assertThat(customerService.shouldProcessMessage(testData.payload)).isFalse
    }

    @Test
    fun `should process valid message`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "product" to messageNodeFactory.textNode("car"),
                "productInternalId" to messageNodeFactory.textNode("A14")
            )
        )

        assertThat(customerService.shouldProcessMessage(testData.payload)).isTrue
    }

    @Test
    fun `should mark message as processed`() {
        val testData = RapidMessage.fromData(
            "TestApplication", "SampleEvent", mapOf(
                "product" to messageNodeFactory.textNode("car"),
                "productInternalId" to messageNodeFactory.textNode("A14")
            )
        )

        val processedMessage = customerService.processMessage(testData)
        assertThat(processedMessage.payload["processed"]?.booleanValue()).isTrue
    }
}
