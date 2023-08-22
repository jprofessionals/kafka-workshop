Oppgave 4e Kafka streams for å logge kall mellom tjenester

##  Formål
De kan være vanskelig å ha oversikt over en R&R applikasjon når den vokser. Det kan være en fordel å analysere relasjoner for å lage diagrammer.

## Overordnet beskrivelse
Vi kommer ikke til å generere fullstendige data for diagrammer i denne oppgaven. 
Vi teller vektorer og skriver de ut på formatet
```
IdMappingService->CustomerService -> 5
```

## MetricService
Kopier inn kafka streams koden fra oppgavre 2c. Men kall den MetricService, og pass på at den går mot rapid-topic

### Kjør den nye klassen
Sjekk at den teller ord på rapid

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
Finn testen, aktiver den og se at den kjører grønt

### Kjør MetricService mot rapid
Start MetricService. Kjør samme scenarie som i 4d, der du sender en melding via NewProductsMessageProducer, og meldingen går gjennom alle de andre tjenestene.
## Forventet output fra MetricService


vector: NewProductsMessageProducer->IdMappingService -> <Antall>
vector: IdMappingService->CustomerService -> <Antall>

Kjør NewProductsMessageProduct en gang til.

Sjekk at vektorantallene øker med 1.



