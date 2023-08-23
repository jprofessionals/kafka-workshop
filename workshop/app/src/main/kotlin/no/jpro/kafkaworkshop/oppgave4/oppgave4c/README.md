# Oppgave 4c IdMappingSevice

##  Formål
Denne oppgaven fokuserer på hvordan en melding kan bli beriket i et Rapids & Rivers rammeverk.

## Overordnet beskrivelse
Målet er å lage en komponent som mottar meldinger fra Oppgave 4b. 
Disse meldingene inneholder et felt kalt 'productExternalId', og oppgaven er å kartlegge denne verdien til et nytt felt, 'productInternalId'. 
Dette nye feltet skal eksistere på samme nivå i json som 'productExternalId'.

## IdMappingServivce

### Oppsett av Kodebase og Tester
Opprett en main-funksjon og klassestruktur for IdMappingService, lik den du lagde for MessageLoggerService. Siden deler av koden mangler, vil den innledende testen feile. 
Husk å bruke en annen consumerGroupId enn i forrige oppgave for å unngå konflikter i meldingsfordeling.

### ShouldProcessMessage funksjonen
Denne metoden kjøres to ganger: først på den innkommende meldingen fra Rapid for å avgjøre om den skal behandles, og deretter på den nyproduserte meldingen for å forhindre uønskede løkker. 
For å avgjøre om en melding skal behandles, bruk følgende kodesnutt:
```kotlin
        val hasExternalId = incomingMessage["productExternalId"]?.isTextual ?: false
        val lacksInternalId = !incomingMessage["productInternalId"].isNotNull()
```

### Legg inn mappingdata
Gjør dette tilgjengelig inne i klassen. Den skal brukes til å mappe fra ekstern til intern id
```kotlin
    mapOf("10" to "A14", "11" to "B55", "12" to "H2", "13" to "X91", "14" to "V20")

```

### Implementering av processMessage(...)
Metoden skal mappe 'productExternalId' til 'productInternalId' og returnere den oppdaterte meldingen. Bruk følgende kode for å oppnå dette:
```kotlin
        val externalId = originalMessage.payload["productExternalId"]?.asText()
        val internalId = //TODO: mapping til ny kode

        return originalMessage.copyWithAdditionalData(
            this::class.simpleName!!,
            mapOf("productInternalId" to messageNodeFactory.textNode(internalId))
        )
```

### Utfør Tester
Kjør enhetstestene for IdMappingService og bekreft at de nå passerer.

## Test IdMappingService mot Rapid-topic
Start IdMappingService.
Sørg for at MessageLoggerService også er aktiv.
Vent til begge logger meldingen "Successfully joined group."
Kjør NewProductsMessageProducer for å sende en testmelding.

## Forventet utskrifdt i MessageLoggerService

### Fra NewProductsMessageProducer
Den forventede utskriften skal være identisk med den i Oppgave 4b.


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