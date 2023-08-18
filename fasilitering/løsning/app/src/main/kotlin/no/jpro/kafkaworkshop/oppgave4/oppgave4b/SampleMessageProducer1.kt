package no.jpro.kafkaworkshop.oppgave4.oppgave4b

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.MessageData
import no.jpro.kafkaworkshop.oppgave4.oppgave4a.Rapid

private const val applikasjonsnavn = "Producer1"
private val messageNodeFactory = JsonNodeFactory.instance
fun sampleProducerMessage(): Rapid.RapidMessage {

    val messageData: MessageData = mapOf(
        "key1" to messageNodeFactory.objectNode().put("producer1ItemId", "12"),
        "key2" to messageNodeFactory.objectNode().put("producer1ItemValue", "144")
    )

    val participatingSystems = listOf(
        Rapid.RapidMessage.ParticipatingSystem(applikasjonsnavn),
    )

    return Rapid.RapidMessage("SampleEvent", messageData, participatingSystems)
}
