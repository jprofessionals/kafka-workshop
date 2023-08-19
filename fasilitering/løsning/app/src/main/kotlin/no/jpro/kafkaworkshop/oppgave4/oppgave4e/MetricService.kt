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

class MetricService {
    private val log = logger()

    fun start() {
        val APPLICATION_ID = "metricService-1"
        val BOOTSTRAP_SERVERS = "localhost:9092"
        val STATE_DIR = "ktables"

        val streamsConfiguration = Properties()
        streamsConfiguration[StreamsConfig.APPLICATION_ID_CONFIG] = APPLICATION_ID
        streamsConfiguration[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
        streamsConfiguration[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
        streamsConfiguration[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
        streamsConfiguration[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        streamsConfiguration[StreamsConfig.COMMIT_INTERVAL_MS_CONFIG] = "100"
        streamsConfiguration[StreamsConfig.STATE_DIR_CONFIG] = STATE_DIR
        streamsConfiguration[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "6000"
        streamsConfiguration[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = "6000"
        streamsConfiguration[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = "1000"

        val builder = StreamsBuilder()
        val textLines: KStream<String, String> = builder.stream(RapidConfiguration.topic)

        val objectMapper = jacksonObjectMapper()
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

        vectorCounts.toStream().foreach { vector, count -> log.info("vector: $vector -> $count") }

        val topology = builder.build()
        val streams = KafkaStreams(topology, streamsConfiguration)
        log.info("start")
        streams.start()

        Runtime.getRuntime().addShutdownHook(Thread {
            streams.close()
            log.info("stop")
        })
    }
}
