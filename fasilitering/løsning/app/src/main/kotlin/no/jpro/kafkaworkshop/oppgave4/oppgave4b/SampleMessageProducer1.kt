package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Message
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid
import java.time.ZonedDateTime

fun sampleProducerMessage(): Rapid.RapidMessage {

    val applikasjonsnavn = "Producer1"

    val messageNodeFactory = JsonNodeFactory.instance
    val messageData: Message = mapOf(
        "key1" to messageNodeFactory.objectNode().put("producer1ItemId", "12"),
        "key2" to messageNodeFactory.objectNode().put("producer1ItemValue", "144")
    )

    val participatingSystems = listOf(
        Rapid.RapidMessage.ParticipatingSystem(applikasjonsnavn, ZonedDateTime.now()),
    )

    return Rapid.RapidMessage("SampleEvent", messageData, participatingSystems)
}
