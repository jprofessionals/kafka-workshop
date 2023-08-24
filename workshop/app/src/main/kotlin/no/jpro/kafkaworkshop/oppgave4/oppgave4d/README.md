# Oppgave 4d CustomerService

##  Formål
Oppgaven har som formål å lage en komponent som bruker den interne ID-en generert av IdMappingService fra Oppgave 4c.
Denne klasssen sender ikke meldinger til ny komponent. For å sikre at også CustomerService ligger som Participating system, legger vi en kvitteringsmelding på rapid.


## Overordnet beskrivelse
CustomerService vil ta imot meldinger fra IdMappingService og utføre operasjoner basert på den interne produkt-IDen.

## CustomerService

### Kodeskjelett og test
Benytt tidligere oppgaver som referanse for å sette opp et grunnleggende kodeskjelett og kjøre en initiell test som vil feile. 
Husk å bruke en annen consumerGroupId for å unngå å motta meldinger ment for andre komponenter.

### Entry kriteria 
Meldingen må ha et produktfelt: ```kotlin incomingMessage["product"]?.isNotNull() ?: false```
Meldingen må ha en intern produkt-ID: ```kotlin incomingMessage["productInternalId"]?.isNotNull() ?: false```
Meldingen må ikke allerede være behandlet: ```kotlin incomingMessage["processed"]?.booleanValue() != true```

### Tilleggsdata for ny melding(kvittering til rapid)
```kotlin mapOf("processed" to RapidConfiguration.messageNodeFactory.booleanNode(true))```

### Utfør Enhetstest
Kjør enhetstesten for CustomerService og bekreft at den nå går grønt.

## Kjør Customerservice mot rapid
Start opp CustomerService.
Sørg for at både MessageLoggerService og IdMappingService også kjører.
Vent til alle tre tjenestene logger meldingen "Successfully joined group."
Kjør NewProductsMessageProducer for å generere en ny melding.

## Forventet output i MessageLoggerService

### Fra NewProductsMessageProducer og IdMappingService
Meldingene skal være de samme som tidligere oppgaver.

### Forventet output fra Customerservice
```json
{
  "eventName": "SampleEvent",
  "payload": {
    "productExternalId": "12",
    "product": {
      "name": "car",
      "color": "red"
    },
    "productInternalId": "H2",
    "processed": true
  },
  "participatingSystems": [
    {
      "applicationName": "NewProductsMessageProducer"
    },
    {
      "applicationName": "IdMappingService"
    },
    {
      "applicationName": "CustomerService"
    }
  ]
}
```

Legg merke til at CustomerService blir inkludert i "participatingSystems" takket være kvitteringsmeldingen, og det gir oss en komplett logg over alle operasjonene som har blitt utført.











