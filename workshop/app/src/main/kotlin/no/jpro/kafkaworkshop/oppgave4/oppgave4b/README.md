# Oppgave 4b Message producer og enkel message consumer(logger) mot rapid topic.

##  Formål
Bli kjent med hvordan vi kan produsere og lytte på meldinger med et enkelt rapid rammeverk.
Lage komponenter som gjør det enkelt å produsere og se meldinger mot rapid topic for resterende oppgaver.

## Overordnet beskrivelse
Vi lager en producer som kan produsere meldinger mot rapid. 
Vi lager en meldingslytter som skriver alle meldinger på rapid topic til log.

## Nytt topic: "rapid-1"
Se på oppskriften i oppgave 1, og lag et nytt topic som du kaller "rapid-1"

## NewProductsMessageProducer

### Klargjør enhetstest
Kommenter inn testklassen NewProductsMessageProducerTest. 
Sjekk at testen kan kjøres men feiler.

### NewProductsMessageProducer
Fullfør TODO i produceMessage() funksjonen.

### Kjør enhetstest
Sjekk at NewProductsMessageProducerTest nå kjører uten feil

## MessageLoggerService

### Sett opp kodeskjelett for klassen
Kommenter inn koden for MessageLoggerService
Genererer metoder spm skal overrides

### Klargjør enhetstest
Finn enhetstest for klassen, kommenter den inn, kjør den og se at den feiler.

### Fullfør MessageLoggerService
Gjør TODOs ferdig, og sjekk at testen kjører


## Kjør opp komponentene mot rapid
Start MessageLoggerService via main funksjonen i klassen, 
Vent på meldingen "Successfully joined group" i loggen.
Send en melding til rapid-1 ved å kjøre main funkjsonen i NewProductsMessageProducer

## Forventet output fra MessageLoggerService
```json
{
  "eventName": "SampleEvent",
  "payload": {
    "productExternalId": "12",
    "product": {
      "name": "car",
      "color": "red"
    }
  },
  "participatingSystems": [
    {
      "applicationName": "NewProductsMessageProducer"
    }
  ]
}
```

### Melding fra NewProductsMessageProducer




