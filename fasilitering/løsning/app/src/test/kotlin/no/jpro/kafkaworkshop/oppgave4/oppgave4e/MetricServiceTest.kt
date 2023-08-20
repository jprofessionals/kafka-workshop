import no.jpro.kafkaworkshop.oppgave4.oppgave4e.MetricService
import org.apache.kafka.streams.kstream.ValueMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MetricServiceTest {

    @Test
    fun `test vector counter with two participating systems`() {
        val metricService = MetricService()
        val mapper: ValueMapper<String, Iterable<String>> = metricService.vectorCounter

        val input = """{
            "participatingSystems": [
                {"applicationName": "app1"},
                {"applicationName": "app2"}
            ]
        }"""

        val vectors = mapper.apply(input).toList()

        assertThat(vectors).containsExactly("app1->app2")
    }

    @Test
    fun `test vector counter with no participating systems`() {
        val metricService = MetricService()
        val mapper: ValueMapper<String, Iterable<String>> = metricService.vectorCounter

        val input = "{}"

        val vectors = mapper.apply(input).toList()

        assertThat(vectors).isEmpty()
    }

    @Test
    fun `test vector counter with malformed JSON`() {
        val metricService = MetricService()
        val mapper: ValueMapper<String, Iterable<String>> = metricService.vectorCounter

        val input = "malformed-json"

        val vectors = mapper.apply(input).toList()

        assertThat(vectors).isEmpty()
    }
}
