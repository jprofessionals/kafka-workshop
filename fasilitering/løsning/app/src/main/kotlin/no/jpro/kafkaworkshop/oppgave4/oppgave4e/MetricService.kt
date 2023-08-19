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
import java.util.*

fun main() {
    MetricService().start()
}

/**
 * The `MetricService` class is responsible for processing and tracking metrics
 * related to messages being passed through the Kafka streams. It counts the
 * vectors of participating systems from the incoming messages and logs their occurrences.
 */
class MetricService {
    private val log = logger()

    /**
     * Starts the Kafka stream processing to capture and log metric data.
     */
    fun start() {
        val APPLICATION_ID = "metricService-1"
        val BOOTSTRAP_SERVERS = "localhost:9092"
        val STATE_DIR = "ktables"

        val streamsConfiguration = Properties().apply {
            this[StreamsConfig.APPLICATION_ID_CONFIG] = APPLICATION_ID
            this[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
            this[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
            this[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
            this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
            this[StreamsConfig.COMMIT_INTERVAL_MS_CONFIG] = "100"
            this[StreamsConfig.STATE_DIR_CONFIG] = STATE_DIR
            this[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "6000"
            this[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = "6000"
            this[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = "1000"
        }

        val builder = StreamsBuilder()
        val textLines: KStream<String, String> = builder.stream(RapidConfiguration.topic)

        val objectMapper = jacksonObjectMapper()

        // Process the incoming messages, extracting vectors of participating systems
        // and count occurrences of each vector
        val vectorCounts: KTable<String, Long> = textLines.flatMapValues { value ->
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
                log.error("Failed to process JSON: $value", e)
                emptyList<String>()
            }
        }
            .groupBy { _, vector -> vector }.count()

        // Log the vector counts
        vectorCounts.toStream().foreach { vector, count -> log.info("vector: $vector -> $count") }

        val topology = builder.build()
        val streams = KafkaStreams(topology, streamsConfiguration)

        log.info("start")
        streams.start()

        // Ensure graceful shutdown on application termination
        Runtime.getRuntime().addShutdownHook(Thread {
            streams.close()
            log.info("stop")
        })
    }
}
