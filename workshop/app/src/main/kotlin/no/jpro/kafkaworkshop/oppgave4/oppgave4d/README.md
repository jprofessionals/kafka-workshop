# Oppgave 4d CustomerService

##  Formål
For å ha et complett case, trenger vi en tjenste som benytter seg av intern id
Vise at vi ønsker kvitteringsmelinger når det ikke er flere operasjoner som skal gjøres, 
slik at rapid, og listen participatingSystems, blir en komplett logg over operasjonene som er utført.


## Overordnet beskrivelse
Lage en komponent som tar i mot medlingen fra IdMappingService fra oppgave 4b.

## CustomerService

### Kodeskjelett og test
Hent inspirasjon fra tidligere oppgaver for å sette opp initielt skjelett og kjøre feilende test. 
Husk å bruke ny consumerGroupId.

### Entry kriteria 
Skal ha: ```kotlin incomingMessage["product"]?.isNotNull() ?: false```
Skal ha: ```kotlin incomingMessage["productInternalId"]?.isNotNull() ?: false```
Skal ikke ha: ```kotlin incomingMessage["processed"]?.booleanValue() == true```

### Tilleggsdata for ny melding(kvittering til rapid)
```kotlin mapOf("processed" to messageNodeFactory.booleanNode(true))```

### Kjør enhetstest
Sjekk at den nå går grønt

## Kjør Customerservice mot rapid
Start CustomerService
Pass på at MessageLoggerservice og IdMappingService kjører
Vent til alle tre har beskjeden: "Successfully joined group"
Kjør NewProductsMessageProducer for å legge ut en melding.

## Forventet output i MessageLoggerService

### Forventet output fra NewProductsMessageProducer og IdMappingService
Samme meldinger som tidligere

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

Legg merke til at det er på grunn av kvitteringsmeldingen fra CustomerService at vi kan se CustomerService som participatingSystem på rapid topic.











