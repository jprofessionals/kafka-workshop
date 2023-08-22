# Oppgave 4c IdMappingSevice

##  Formål
Se hvordan beriking av en melding kan fungere i Rapids & Rivers.

## Overordnet beskrivelse
Lage en komponent som tar i mot medlingen fra oppgave 4b.
Meldingen har 'productExternalId'. Vi skal mappen veriden til et nytt felt productInternalId.
Feltet skal legges på samme nivå som productExternalId


## IdMappingServivce

### Kodeskjelett og test
Lag main-funksjon, opprett klassen, og kjør test, på samme måte som for MessageLoggerService. Testen vil feile siden en del kode mangler.
Pass på at du sender inn annen consumerGroupId enn i forrige oppgave, ellers vil komponentene fordele meldinger mellom seg.

### ShouldProcessMessage
Denne koden kjøres to ganger. Først på meldingen som kommer inn fra rapid for å finne ut om den skal behandles.
Etter at ny melding for rapid er konstrert, blir den kjørt på nytt for å se om neste melding vil igjen bli plukket opp og lage en loop
Bruk følgende uttrekk fra inkommende melding for å finne ut om meldingen skal prosesseres i denne komponenten.
```kotlin
        val hasExternalId = incomingMessage["productExternalId"]?.isTextual ?: false
        val lacksInternalId = !incomingMessage["productInternalId"].isNotNull()
```

### Legg inn mappingdata
Vi legger det inn i klassen som en hardkodet map:
```kotlin
    mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")

```

### processMessage(...)
Denne metoden skal nå gjøre mappingen, oppdatere melding med intern id, 
og returnere meldingen slik at den kan bli lagt på topic
```kotlin
        val externalId = originalMessage.payload["productExternalId"]?.asText()
        val internalId = //TODO: mapping til ny kode

        return originalMessage.copyWithAdditionalData(
            this::class.simpleName!!,
            mapOf("productInternalId" to messageNodeFactory.textNode(internalId))
        )
```

### Kjør enhetstest
Sjekk at den nå går grønt

## Kjør opp IdMappingSevice mot rapid
Start IdMappingService.
Pass på at MessageLoggerService kjører.
Vent til begge har beskjeden: "Successfully joined group"
Kjør NewProductsMessageProducer for å legge ut en melding.

## Forventet output i MessageLoggerService

### Forventet output fra NewProductsMessageProducer
Samme melding som i oppgave 4b


### Forventet output fra IdMappingService
```json
{
  "eventName": "SampleEvent",
  "payload": {
    "productExternalId": "12",
    "product": {
      "name": "car",
      "color": "red"
    },
    "productInternalId": "H2"
  },
  "participatingSystems": [
    {
      "applicationName": "NewProductsMessageProducer"
    },
    {
      "applicationName": "IdMappingService"
    }
  ]
}
```