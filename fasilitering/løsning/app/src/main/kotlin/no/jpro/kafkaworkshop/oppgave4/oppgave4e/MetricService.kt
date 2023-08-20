package no.jpro.kafkaworkshop.oppgave4.oppgave4e

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.jpro.kafkaworkshop.logger
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.RapidConfiguration
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.ValueMapper
import java.util.*

fun main() {
    MetricService().start()
}

/**
 * MetricService: A Kafka Streams Application
 *
 * This class defines a Kafka Streams application named 'MetricService'.
 * The purpose of the application is to consume records from a Kafka topic
 * configured in `RapidConfiguration`, and calculate vector counts based
 * on the `participatingSystems` field in each JSON message.
 *
 * Vector counts are then logged for further use or analysis.
 */
class MetricService {

    fun start() {
        val applicationId = "metricService-1"
        val bootstrapServers = "localhost:9092"
        val stateDir = "ktables"

        val streamsConfiguration = Properties().apply {
            this[StreamsConfig.APPLICATION_ID_CONFIG] = applicationId
            this[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
            this[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
            this[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
            this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
            this[StreamsConfig.COMMIT_INTERVAL_MS_CONFIG] = "100"
            this[StreamsConfig.STATE_DIR_CONFIG] = stateDir
            this[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "6000"
            this[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = "6000"
            this[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = "1000"
        }

        val builder = StreamsBuilder()
        val textLines: KStream<String, String> = builder.stream(RapidConfiguration.topic)


         // Use the vectorCounter function to transform the Kafka stream, group them, and count the vectors. The result is stored in a KTable.
        val vectorCounts: KTable<String, Long> = textLines.flatMapValues(vectorCounter)
            .groupBy { _, vector -> vector }.count()

        // Log the vector counts
        vectorCounts.toStream().foreach { vector, count -> logger().info("vector: $vector -> $count") }

        val topology = builder.build()
        val streams = KafkaStreams(topology, streamsConfiguration)

        logger().info("start")
        streams.start()

        // Ensure graceful shutdown on application termination
        Runtime.getRuntime().addShutdownHook(Thread {
            streams.close()
            logger().info("stop")
        })
    }

    /**
     * A ValueMapper function that reads a JSON string, extracts a
     * 'participatingSystems' list, and creates vectors for the last two
     * systems. These vectors are then counted in the stream.
     */
    val vectorCounter: ValueMapper<String, Iterable<String>> = ValueMapper { value ->
        val objectMapper = jacksonObjectMapper()
        try {
            val jsonNode = objectMapper.readTree(value)
            val participatingSystems = jsonNode["participatingSystems"]?.mapNotNull {
                it["applicationName"]?.asText()
            } ?: emptyList()

            if (participatingSystems.size >= 2) {
                val lastTwo = participatingSystems.takeLast(2)
                listOf("${lastTwo[0]}->${lastTwo[1]}")
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            logger().error("Failed to process JSON: $value", e)
            emptyList()
        }
    }
}
