import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageProducer
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidMessage
import no.jpro.kafkaworkshop.oppgave4.oppgave4b.NewProductsMessageProducer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class NewProductsMessageProducerTest {

    private lateinit var mockMessageProducer: MessageProducer
    private lateinit var newProductsMessageProducer: NewProductsMessageProducer

    val testMessage = RapidMessage.fromData(
        NewProductsMessageProducer::class.simpleName.toString(), "SampleEvent", mapOf(
            "productExternalId" to RapidConfiguration.messageNodeFactory.textNode("12"),
            "product" to RapidConfiguration.objectMapper.valueToTree(NewProductsMessageProducer.Product("car", "red"))
        )
    )

    @BeforeEach
    fun setUp() {
        mockMessageProducer = Mockito.mock(MessageProducer::class.java)
        newProductsMessageProducer = NewProductsMessageProducer(mockMessageProducer)
    }

    @Test
    fun `produceMessage should send message via MessageProducer`() {
        newProductsMessageProducer.produceMessage()

        // Assert
        verify(mockMessageProducer).send(testMessage)
    }
}
