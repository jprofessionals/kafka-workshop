import no.jpro.kafkaworkshop.oppgave4.oppgave4e.GraphService
import org.apache.kafka.streams.kstream.ValueMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GraphServiceTest {

    @Test
    fun `test vector counter with two participating systems`() {
        val graphService = GraphService()
        val mapper: ValueMapper<String, Iterable<String>> = graphService.vectorCounter

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
        val graphService = GraphService()
        val mapper: ValueMapper<String, Iterable<String>> = graphService.vectorCounter

        val input = "{}"

        val vectors = mapper.apply(input).toList()

        assertThat(vectors).isEmpty()
    }

    @Test
    fun `test vector counter with malformed JSON`() {
        val graphService = GraphService()
        val mapper: ValueMapper<String, Iterable<String>> = graphService.vectorCounter

        val input = "malformed-json"

        val vectors = mapper.apply(input).toList()

        assertThat(vectors).isEmpty()
    }
}
