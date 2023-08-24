# Oppgave 2b Kafka Consumer

## Åpne Consumer.kt
Filen inneholder skjelettet for å kjøre en consumer mot en Kafka topic.

## Hent inn topicnavn og meldingsobjekt fra oppgave 2a i imports
import no.jpro.kafkaworkshop.oppgave2.oppgave2a.Common.Message
import no.jpro.kafkaworkshop.oppgave2.oppgave2a.Common.topic

## Opprett en instans av jacksonObjektmapper som kan brukes til å opprettte Message objekter av meldinger
```kotlin
val jacksonObjectMapper = jacksonObjectMapper()
```

## Definer konfigurasjonen for vår Kafka consumer
I main funksjonen, inkluder informasjon om hvor Kafka-serveren kjører og hvordan meldingsnøklene og verdiene skal deserialiseres.
Legg til en groupid som identifiserer consumergruppen. Hvis denne applikasjonen kjørte i et cluster sammen med andre noder med samme groupid, ville meldingene blitt fordelt mellom consumernodene.
Som i oppgave 1, velger vi "earliest" som konfigurasjon, slik at vi starter fra begynnelsen av topicen første gang denne klienten kjører.
```kotlin
val consumerProps = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name, // Note: Changed this from StringSerializer to StringDeserializer
        ConsumerConfig.GROUP_ID_CONFIG to "kotlinConsumer-1",
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to "6000",
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to "6000",
        ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to "1000"
    )
```

## Opprett en KafkaConsumer med den angitte konfigurasjonen og bruk en 'kotlin use block'. Dette sørger for at ressursene ryddes opp etter bruk, ettersom denne blokken er autocloseable.
```kotlin
KafkaConsumer<String, String>(consumerProps).use { consumer ->
    // TODO: Legg inn resten av koden i denne use-kode-blokken for å sikre at ressurser lukkes automatisk
}
```

## Lytt på meldingene fra topicet
```kotlin
consumer.subscribe(listOf(topic))
```

## Opprett en løkke for kontinuerlig overvåkning av topicet
Selv om while(true) ikke er optimal for prosessorutnyttelse og kan medføre utfordringer i produksjonsmiljøer, fungerer det fint for denne workshopen.
```kotlin
while (true) {
    // resten av koden legges her
}
```

## Hent ut meldinger og bruk timeout på 1/10 sekund per melding
```kotlin
val records = consumer.poll(Duration.ofMillis(100))
```


## Gå gjennom hver melding og konverter den til producerens Message-format
Med vårt standard oppsett kan vi få opp til 500 meldinger om gangen.
```kotlin
 records.forEach { record ->
                logger.info("Consumed record with key ${record.key()} and value ${record.value()}")

                try {
                    val message: Message = jacksonObjectMapper().readValue(record.value(), Message::class.java)
                    logger.info("Deserialized Message: $message")
                } catch (e: Exception) {
                    logger.error("Error deserializing record value: ${record.value()}", e)
                }
            }
```

## Start consumeren
Du skal se følgende i loggen:
```Consumed record with key key and value {"id":"1","value":"a value"}```

## Start producer en gang til 
Verifiser at melding kommer en gang til.
    

