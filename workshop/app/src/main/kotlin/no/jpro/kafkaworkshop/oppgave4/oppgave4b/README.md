# Oppgave 4b Message producer og message consumer mot rapid topic.

##  Formål
Formålet med denne deloppgaven er å bli fortrolig med produksjon og lytting av rapid-meldinger. 
Oppgaven involverer å lage komponenter som enkelt kan produsere og logge meldinger for Rapid-topicet.

## Overordnet beskrivelse
I denne deloppgaven vil vi utvikle en meldingsprodusent (NewProductsMessageProducer) som sender meldinger til Rapid-emnet. I tillegg vil vi også opprette en meldingslytter (MessageLoggerService) som vil logge alle meldinger på rapid-topicet.

## Nytt topic: "rapid-1"
Se på oppskriften i oppgave 1, og lag et nytt topic som du kaller "rapid-1"

## NewProductsMessageProducer

### Forbered Enhetstesting
Kommenter inn testklassen NewProductsMessageProducerTest. Kontroller at testen kan kjøres, selv om den vil feile i denne omgang.

### Implementasjon av NewProductsMessageProducer
Fullfør "TODO"-merket kode i produceMessage()-funksjonen.

### Utfør Enhetstesting
Sørg for at NewProductsMessageProducerTest nå kjøres uten feil.

## MessageLoggerService

### Opprett klasse
Kommenter inn koden for MessageLoggerService og generer metoder som skal overrides.

### Forbered Enhetstesting
Finn enhetstesten for klassen, aktiver den, kjør testen, og bekreft at den feiler.

### Fullfør MessageLoggerService
Fullfør alle todos i koden og bekreft at testen nå kjøres uten feil.


## Test Komponentene mot rapid-topic
Start lytter ved å  å kjøre  main-funksjonen i MessageLoggerService.
Send en testmelding til rapid ved å kjøre main-funksjonen i NewProductsMessageProducer.

## Forventet resultat i MessageLoggerService

### Forventet loggutskrift fra NewProductsMessageProducer 
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




