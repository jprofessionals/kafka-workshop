package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid

fun main() {
    Rapid.send(sampleProducerMessage().toJsonText())
}