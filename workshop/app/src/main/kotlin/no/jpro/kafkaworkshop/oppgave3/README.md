# Oppgave 3 Apache Avro for serialisering

I denne oppgaven skal vi publisere og konsumere meldinger serialisert på Avro-format.
Avro på Kafka er proprietært, ved at det krever Confluent Schema Registry.

Vi genererer en Java-klasse fra Avro-skjemaet.
Til dette bruker vi et Gradle-plugin med usikker fremtid.
Confluent tilbyr offisielt Maven-plugin, så Maven kan være et tryggere valg.

[Lenke til presentasjon](https://docs.google.com/presentation/d/1bs0tBy_auDlA9_7qU9Lx8UKv8s2jBDOLT-j6664Jfa8/edit?usp=sharing).

## Forberedelser

Det kan ta tid å laste ned images og biblioteker.
Gjør gjerne forberedelsene på forhånd.

### Start Kafka med Avro-støtte

Bruk Docker Compose-oppsettet fra https://github.com/codingharbour/kafka-docker-compose
```shell
cd single-node-avro-kafka
docker-compose up
```

### Installer IntelliJ Kafka plugin

Installer Kafka-plugin i IntelliJ.
Hvis du har IntelliJ-versjon < 2023.2, så installerer du Big Data Tools-plugin.

Konfigurer plugin mot `localhost`:

Kafka Broker:

| Innstilling         | Verdi          |
|---------------------|----------------|
| Broker host         | 127.0.0.1:9092 |
| Configuration source | Custom        |
| Bootstrap servers   | 127.0.0.1:9092 |
| Authentication      | None           |

Schema Registry:

| Innstilling             | Verdi                 |
|-------------------------|-----------------------|
| Type                    | Confluent             |
| URL                     | http://localhost:8081 |
| Configuration source    | Custom                |
| Authentication          | None                  |
| Use broker SSL settings | true                  |

Sjekk at "Test connection" returnerer _"Connected"._

### Installer Apache Avro IDL Schema Support plugin

Apache Avro IDL Schema Support-pluginet krever ingen konfigurasjon,
men gir syntaks-støtte når vi redigerer Avro-skjema.

## Oppgave 3: Send og motta Avro-serialiserte meldinger

Vi skal definere et Avro-skjema for samme meldingsstruktur som vi brukte i oppgave 2.

Opprett Avro-skjema i `./src/main/avro/oppgave3/AvroMessage.avsc`:
```json
{
  "namespace": "no.jpro.kafkaworkshop.oppgave3",
  "type": "record",
  "name": "AvroMessage",
  "fields": [
    {
      "name": "id",
      "type": "string"
    },
    {
      "name": "value",
      "type": "string"
    }
  ]
}
```

Generer modellklassen `no.jpro.kafkaworkshop.oppgave3.AvroMessage` med Gradle:
```shell
./gradlew generateAvroJava
```
(det gjøres også automatisk under `./gradlew build`)

Ta utgangspunkt i Producer og Consumer fra oppgave 2a og 2b,
og kopier klassene dine til `no.jpro.kafkaworkshop.oppgave3`.
Hvis du ikke lyktes med å løse oppgavene, kan du kopiere fra fasit.

Vi kan ikke publisere Avro-meldinger til samme topic som vi tidligere publiserte JSON-tekst,
så vi definerer et eget topic for oppgave 3 i `Common.topic`:
```kotlin
object Common {
    const val topic = "avro-messages"
}
```
Klassen `Common.Message` fra oppgave 2 kan du slette,
siden vi bruker den genererte `AvroMessage` i denne oppgaven.

Konfigurer Producer til å serialisere til Avro-format,
ved å bruke en annen _value serializer_.
Vi må også oppgi skjemaregister, fordi Avro i Kafka virker ikke uten.

```kotlin
val producerProperties = mapOf<String, Any>(
    // uendret konfigurasjon
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java.name,

    // ny konfigurasjon
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaAvroSerializer::class.java.name,
    AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to "http://localhost:8081",
    KafkaAvroSerializerConfig.AVRO_REMOVE_JAVA_PROPS_CONFIG to true // finpuss, trengs egentlig ikke
)
```

Endre meldingstypen fra `String` til `AvroMessage`:
```kotlin
KafkaProducer<String, AvroMessage>(producerProperties).use { producer ->
    val message = AvroMessage("1", "a value")
    // `message` kan logges direkte, uten først å konvertere til JSON
```

Konfigurer Consumer til å deserialisere fra Avro-format til din modellklasse.
Vi ønsker å bruke den autogenererte modellklassen `AvroMessage`,
så vi bruker såkalt _specific_ Avro reader.

```kotlin
val consumerProps = mapOf(
    // uendret konfigurasjon
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name,
    ConsumerConfig.GROUP_ID_CONFIG to "kotlinConsumer-1",
    
    // ny konfigurasjon
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaAvroDeserializer::class.java.name,
    AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to "http://localhost:8081",
    KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG to true
)
```

Endre meldingstypen fra `String` til `AvroMessage` også i Consumer:
```kotlin
KafkaConsumer<String, AvroMessage>(consumerProps).use { consumer ->
    // Konsumer AvroMessage i stedet for String
    val message: AvroMessage = record.value()
```

Standard oppførsel ved publisering er at skjema blir registrert på subject `<topicnavn>-value`,
så det er ikke nødvendig med mer konfigurasjon,
så lenge man kun har ett skjema per topic.

## Nyttige lenker

* [Avro Schema Serializer and Deserializer](https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-avro.html) - en guide
* [Schema Evolution and Compatibility](https://docs.confluent.io/platform/current/schema-registry/fundamentals/schema-evolution.html#schema-evolution-and-compatibility)
* [Schema Registry API Reference](https://docs.confluent.io/platform/current/schema-registry/develop/api.html#sr-api-reference)
* [Apache Avro Getting Started](https://avro.apache.org/docs/1.10.2/gettingstartedjava.html), inkl. Maven plugin
* [Gradle Avro Plugin](https://github.com/davidmc24/gradle-avro-plugin) - fare for å bli arkivert!
* [Schema Registry Maven Plugin](https://docs.confluent.io/platform/current/schema-registry/develop/maven-plugin.html)
* [Schema-registry-plugin (Gradle)](https://github.com/ImFlog/schema-registry-plugin)
* [Putting Several Event Types in the Same Topic - Revisited](https://www.confluent.io/blog/multiple-event-types-in-the-same-kafka-topic/)
