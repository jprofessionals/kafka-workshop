# Oppgave 2a Kafka Producer

## Åpne Producer.kt
Filen inneholder skjelettet for å kjøre en producer mot et Kafka topic. Det er to kodeblokker der: en for felleskode med neste oppgave, som er å lage en consumer, og en main-metode som kan starte produceren.

## Over main-metoden, legg inn verdiene for Common objektet.
Dette er et objekt som inneholder topicnavn og et objekt som representerer meldingene som skal produseres. 
Det skal senere kunne gjenbrukes av consumer i neste oppgave, så det plasseres utenfor main-metoden. 
Vi bruker her samme topic som ble satt opp i oppgave 1.
Pass på at consumer fra oppgave 1 fortsatt kjører.
```kotlin
object Common {
    const val topic = "first_topic"
    data class Message(val id: String, val value: String)
}
```

## Definer konfigurasjonen for vår Kafka producer
Inkluder hvor Kafka-serveren kjører, og hvordan meldingsnøklene og verdiene skal serialiseres.
```kotlin
val producerProperties = mapOf<String, Any>(
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,
    CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "PLAINTEXT"
)
```

## Lag en KafkaPrducer med konfigurasjonen over, og bruk en 'kotlin use block' som rydder opp ressurser fordi den blokken blir autoCloseable
```kotlin
KafkaProducer<String, String>(producerProperties).use { producer ->
    // TODO: Legg inn resten av koden for oppgaven her
}
```

## Lag en melding som skal produseres, konverter den til en json-streng, og opprett en Kafka record som er klargjort for sending via Kafka.
```kotlin
val message = Common.Message(id = "1", value = "a value")
val jsonMessage: String = jacksonObjectMapper().writeValueAsString(message)
val record = ProducerRecord(topic, "key", jsonMessage)
```

## Send meldingen
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

## Klargjør for neste oppgave ved å henvise til et nytt topic
Vi vil ha et tomt topic som kun skal ha riktig format på meldingene. Endre topic til:
```kotlin
const val topic = "kotlin_topic"
```

## Til slutt, send en melding på det nye topicet.
Start main-metoden på nytt, og verifiser at kallet ikke feiler. Det ligger nå klart en melding på det nye topicet for neste oppgave.
