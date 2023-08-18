package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid.Companion.messageNodeFactory

private const val applikasjonsnavn = "Producer1"

fun sampleProducerMessage(): Rapid.RapidMessage {

    val messageData: MessageData = mapOf(
        "producer1Id" to messageNodeFactory.textNode("12"),
        "producer1Item" to messageNodeFactory.objectNode().put("144", "Car") // ObjectNode here
    )

    return Rapid.RapidMessage(
        eventName = "SampleEvent",
        messageData = messageData,
        participatingSystems = listOf(Rapid.RapidMessage.ParticipatingSystem(applikasjonsnavn))
    )
}