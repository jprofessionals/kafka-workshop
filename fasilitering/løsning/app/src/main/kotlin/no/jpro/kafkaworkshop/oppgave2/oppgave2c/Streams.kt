package no.jpro.kafkaworkshop.oppgave2.oppgave2c

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.slf4j.LoggerFactory
import java.util.*
import java.util.regex.Pattern

fun main() {
    WordCountService().start()
}

class WordCountService {

    fun start() {
        val logger = LoggerFactory.getLogger("no.jpro.kafkaworkshop.streams")
        val streamsConfiguration = Properties().apply {
            this[StreamsConfig.APPLICATION_ID_CONFIG] = "stream-1"
            this[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
            this[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
            this[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
            this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
            this[StreamsConfig.COMMIT_INTERVAL_MS_CONFIG] = "100"
            this[StreamsConfig.STATE_DIR_CONFIG] = "ktables"
            this[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "6000"
            this[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = "6000"
            this[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = "1000"
        }

        val builder = StreamsBuilder()
        val textLines: KStream<String, String> = builder.stream("first_topic")

        val wordCounts: KTable<String, Long> = textLines.flatMapValues(splitWords)
            .groupBy { _, word -> word }
            .count()

        wordCounts.toStream().foreach { word, count -> logger.info("word: $word -> $count") }

        val topology = builder.build()
        val streams = KafkaStreams(topology, streamsConfiguration)

        logger.info("start")
        streams.start()

        Runtime.getRuntime().addShutdownHook(Thread {
            streams.close()
            logger.info("stop")
        })
    }

    val splitWords: (String) -> Iterable<String> = { value ->
        val pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS)
        pattern.split(value.lowercase(Locale.getDefault())).asIterable()
    }
}
