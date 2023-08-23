# Oppgave 4e Bruk av Kafka Streams for Logging av Tjenestekall

##  Formål
Når en Rapids & Rivers (R&R) applikasjon blir større, kan det være utfordrende å holde oversikt over tjenestenes interaksjoner. Målet her er å logge og telle vektorer som representerer kall mellom tjenester for å lettere kunne analysere systemets relasjoner.

## Overordnet beskrivelse
I denne oppgaven vil vi ikke generere fullstendige data for relasjonsdiagrammer. I stedet vil vi fokusere på å telle og logge vektorer som viser interaksjoner mellom tjenester i følgende format:
```
IdMappingService->CustomerService -> 5
```

## MetricService
Kopier Kafka Streams-koden fra Oppgave 2c, gi den navnet MetricService, og sikre at den bruker rapid-topicet.

### Kjør MetricService
Start tjenesten og verifiser at den teller ord i Rapid-emnet.

### Lag en vectorCounter, og bruk den i steden for ordteller:
```kotlin
  /**
 * A ValueMapper function that reads a JSON string, extracts a
 * 'participatingSystems' list, and creates vectors for the last two
 * systems. These vectors are then counted in the stream.
 */
val vectorCounter: ValueMapper<String, Iterable<String>> = ValueMapper { value ->
        val objectMapper = jacksonObjectMapper()
        try {
            val jsonNode = objectMapper.readTree(value)
            val participatingSystems = jsonNode["participatingSystems"]?.mapNotNull {
                it["applicationName"]?.asText()
            } ?: emptyList()

            if (participatingSystems.size >= 2) {
                val lastTwo = participatingSystems.takeLast(2)
                listOf("${lastTwo[0]}->${lastTwo[1]}")
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            logger().error("Failed to process JSON: $value", e)
            emptyList()
        }
    }
```

### Enhetstest
Finn den relevante testen, aktiver den, og se til at den går grønt.

### Kjør MetricService mot rapid
Start MetricService.
Kjør scenariet som beskrevet i Oppgave 4d, 
der en melding sendes via NewProductsMessageProducer og går gjennom alle de andre tjenestene.

## Forventet output fra MetricService
````
vector: NewProductsMessageProducer->IdMappingService -> <Antall>
vector: IdMappingService->CustomerService -> <Antall>
``

Kjør NewProductsMessageProducer en gang til og verifiser at vektortellingen øker med 1.



