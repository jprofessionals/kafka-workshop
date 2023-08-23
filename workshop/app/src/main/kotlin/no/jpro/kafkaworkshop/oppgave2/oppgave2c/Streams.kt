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
    }

}
