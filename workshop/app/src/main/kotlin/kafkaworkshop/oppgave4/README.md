# Oppgave 4 Kafka Streams

## Åpne Streams.kt
Den inneholder skjelettet for å aggregere antall ganger en melding tidligere er sendt på topic. 
Vi skal bruke Kafka Streams for å lagre tilstanden mellom oppstarter, uavhengig av om meldingene fortsatt ligger på topicet.

## Vi bruker topic fra oppgave 1. 
Den har allerede litt data, og det er enkelt å legge inn nye meldinger via cli
```kotlin
    val inputTopic = "first_topic"
```

## Sett opp konfigurasjonen
Denne gangen bruker vi streams biblioteket for å sette opp konfigurasjonen. 

```kotlin
    val streamsConfiguration = Properties()
    streamsConfiguration[StreamsConfig.APPLICATION_ID_CONFIG] = "stream-1"
    streamsConfiguration[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
    streamsConfiguration[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
    streamsConfiguration[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
    streamsConfiguration[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
    streamsConfiguration[StreamsConfig.COMMIT_INTERVAL_MS_CONFIG] = "100"
    streamsConfiguration[StreamsConfig.STATE_DIR_CONFIG] = "ktables"
    streamsConfiguration[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "6000"
    streamsConfiguration[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = "6000"
    streamsConfiguration[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = "1000"
 ```

Kafka streams bruker serdes som serialisering/deserialisering, for å gi en ensartet mekanisme for å håndtere det på en typetrygg måte, og for å lette integrasjonen med Kafka's innebygde dataformater.
```[StreamsConfig.STATE_DIR_CONFIG] ``` Refererer til område for hurtig henting av medlinger ved oppstart. Dette legges på roten i workshop mappen, og viser data på RocksDB format.

##  Opprett en kafka stream mot topic
```kotlin
val builder = StreamsBuilder()
val textLines: KStream<String, String> = builder.stream(inputTopic)
```

## Tell antall forekomster av et ord, og skriv antallet til loggen
Om et ord kommer flere ganger i en melding, telles det flere ganger.
 ```kotlin
 val pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS)

val wordCounts: KTable<String, Long> = textLines
    .flatMapValues { value -> pattern.split(value.lowercase(Locale.getDefault())).asIterable() }
    .groupBy { _, word -> word }
    .count()

wordCounts.toStream().foreach { word, count -> logger.info("word: $word -> $count") }
```
KTable er databaselignende tabeller som lagres i en statestore, i vårt tiilfelle RocksDb


## Velg topologi og start kafka streams operasjonene
"Topologi" er strukturen og flyten av streamingdata gjennom ulike prosesseringsoperasjoner. Den beskriver hvordan ordene aggregeres sammen
```kotlin
val topology = builder.build()
val streams = KafkaStreams(topology, streamsConfiguration)
logger.info("start")
streams.start()
```
## Stans kafka streams på en god måte når applikasjonen stopppes
```kotlin
Runtime.getRuntime().addShutdownHook(Thread {
    streams.close()
    logger.info("stop")
})
```