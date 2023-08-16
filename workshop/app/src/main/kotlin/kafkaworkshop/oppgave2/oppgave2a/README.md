# Oppgave 2a Kafka Producer

## Åpne Producer.kt
Den inneholder skjelettet for å kjøre en producer mot kafka topic. Det er to kodebolker der, en for felleskode med neste oppgave som er å lage en consumer.
Og en main metode som kan starte produceren.

## Over main metoden, legg inn verdiene for Common objektet.
Dette er et objekt som inneholder topicnavn og et objekt som representerer meldingene som som skal produseres.
Det skal senere kunne gjenbrukes av consumer i neste oppgave, så det leggges utenfor main metoden.
Vi bruker her samme topic som ble satt opp i oppgave 1, slik at vi i første omgang kan se endringene i samme consumer som ble laget der.
Pass på at consumer i oppgave 1 fortsatt kjører.
```kotlin
object Common {
    const val topic = "first_topic"
    data class Message(val id: String, val value: String)
}
```

## Definerer konfigurasjonen for vår Kafka producer
Inkluder hvor Kafka-serveren kjører og hvordan meldingsnøklene og verdiene skal serialiseres.
```kotlin
val producerProperties = mapOf<String, Any>(
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
    CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "PLAINTEXT"
)
```

## Lag en melding som skal produseres, gjør den om til json string, og opprett en kafka record som er klargjort for sending via kafka.

```kotlin
val message = Common.Message(id = "1", value = "a value")
val jsonMessage: String = jacksonObjectMapper().writeValueAsString(message)
val record = ProducerRecord(topic, "key", jsonMessage)
```

## Sending av kafka meldingen skjer via producer.send
```kotlin 
try {
    logger.info("Sending message $jsonMessage")
    producer.send(record)
    logger.info("Message has been sent")
} catch (e: Exception) {
    logger.error("Error sending message $jsonMessage", e)
}
```

## Start main metoden for å sende en melding til topic
Verifiser at oppgave 1 sin consumer har motatt en melding på følgende format:
```json
{"id":"1","value":"a value"}
```

## KLlargjør for neste oppgave ved å henvise til nytt topic
Vi vil ha et tomt topic som kun skal få riktig format på meldingene
Endre topic til:
```kotlin
const val topic = "kotlin_topic"
```

## Tilslutt send en melding på det nye topicet.
Start main metoden på nytt, og verifiser at kallet ikke feiler. Det ligger nå klart en melding på det nye topicet for neste oppgave.
