# Oppgave 2b Kafka Consumer

## Åpne Consumer.kt
Den inneholder skjelettet for å kjøre en consumer mot kafka topic.
Det er der en main metode som kan starte produceren. 

## Hent inn topicnavn og meldingsobject fra oppgave 1 i imports
import no.jpro.kafkaworkshop.oppgave2.oppgave2a.Common.Message
import no.jpro.kafkaworkshop.oppgave2.oppgave2a.Common.topic

## Definerer konfigurasjonen for vår Kafka consumer
Inkluder hvor Kafka-serveren kjører og hvordan meldingsnøklene og verdiene skal deserialiseres.
Legg inn en groupid som identifiserer denne consumergruppen. Om denne applikasjonen kjørte i et clulster sammen med andre noder med samme groupid, ville meldingene blitt fordelt mellom consumernodene.
Som i oppgave 1, så velger vi "earlierst" som konfigurasjon, slik at vi starter på starten av topic første gangen denne klienten kjører mot topic.
```kotlin
val consumerProps = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name, // Note: Changed this from StringSerializer to StringDeserializer
        ConsumerConfig.GROUP_ID_CONFIG to "kotlinConsumer-1"
    )
```

## Lag en KafkaConsumer med konfigurasjonen over, og bruk en 'kotlin use block' som rydder opp ressurser fordi den blokken blir autoCloseable
```kotlin
KafkaConsumer<String, String>(consumerProps).use { consumer -> 
        // TODO: koden for resten av oppgaven legges i use-kode-blokken, for å sikre at den er autocloseable
        
}
        
```

## Lytt på meldingene på topicet
```kotlin
consumer.subscribe(listOf(topic))
```

## Lag en løkke for å ha kontinuerlig lytting på topic
while(true) gir ikke optimal prosessorutnyuttelse, og kan ha en del andre utfordringer, men det er tilstrekkelig for en workshop.
```kotlin
while (true) {
 // resten av koden går inn her
}
```

## Hent ut en melding, sett timeout til 1/10 sekund
```kotlin
val records = consumer.poll(Duration.ofMillis(100))
```

## Default oppsett som vi har, gjør at vi får 500 medlinger om gangen.
Gå gjennom hver melding, og konverter den til producers Message format
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

## Start consumer
Du skal se følgende i loggen:
```Consumed record with key key and value {"id":"1","value":"a value"}```
    

