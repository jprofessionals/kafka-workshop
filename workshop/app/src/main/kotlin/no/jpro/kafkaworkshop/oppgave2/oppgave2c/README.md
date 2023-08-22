# Oppgave 2c Kafka Streams

## Åpne Streams.kt
Filen inneholder skjelettet for summering av antall ganger en melding tidligere er sendt i et topic.
Vi skal benytte Kafka Streams for summeringen, og for å lagre tilstand mellom oppstarter, selv om meldingene ikke lenger finnes på topicet.

## Vi bruker topic fra oppgave 1, first_topic
Topicet inneholder allerede noe data. Det er også enkelt å legge til nye meldinger via CLI.
```kotlin
    val inputTopic = "first_topic"
```

## Sett opp konfigurasjonen
Denne gangen skal vi bruke Kafka Streams-biblioteket for konfigurering.

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
Kafka Streams bruker "Serdes" for serialisering/deserialisering, som gir en enhetlig metode for typesikker håndtering.
[StreamsConfig.STATE_DIR_CONFIG] peker til et område for rask henting av meldinger ved oppstart. Dette plasseres i roten av workshop-mappen, og viser data for en RocksDB database.

##  Opprett en kafka Stream for topicet
```kotlin
val builder = StreamsBuilder()
val textLines: KStream<String, String> = builder.stream(inputTopic)
```

## Lag 

## Tell antall forekomster av et ord og logg antallet
Hvis et ord forekommer flere ganger i en melding, skal det telles hver gang.
 ```kotlin
 val pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS)

val wordCounts: KTable<String, Long> = textLines.flatMapValues(splitWords)
    .groupBy { _, word -> word }
    .count()

wordCounts.toStream().foreach { word, count -> logger.info("word: $word -> $count") }
```
KTable representerer databaselignende tabeller som lagres i en "state store". I vårt tilfelle benyttes RocksDb.

## Velg topologi og start kafka Streams operasjonene
"Topologi" refererer til strukturen og flyten av streamingdata gjennom ulike prosesseringsoperasjoner. Den beskriver hvordan dataene blir aggregert.
```kotlin
val topology = builder.build()
val streams = KafkaStreams(topology, streamsConfiguration)
logger.info("start")
streams.start()
```
## Stopp Kafka Streams på en trygg måte når applikasjonen avsluttes
```kotlin
Runtime.getRuntime().addShutdownHook(Thread {
    streams.close()
    logger.info("stop")
})
```

## Lag en funksjon for å gjøre tellingen av ord.
Legg den utenfor start() metoden.
```kotlin
    val splitWords: (String) -> Iterable<String> = { value ->
        val pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS)
        pattern.split(value.lowercase(Locale.getDefault())).asIterable()
    }
```