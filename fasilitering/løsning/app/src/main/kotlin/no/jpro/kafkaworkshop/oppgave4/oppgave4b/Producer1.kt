package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory

fun main() {
    Rapid.send(sampleProducerMessage().toJsonText())
}