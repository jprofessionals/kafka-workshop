# Oppgave 4b Message producer og enkel message consumer(logger) mot rapid topic.

##  Formål
Målet med denne deloppgaven er å bli fortrolig med produksjon og lytting av meldinger i et Rapid-rammeverk. 
Oppgaven involverer å lage komponenter som enkelt kan produsere og logge meldinger for Rapid-emnet, noe som vil være nyttig for de resterende oppgavene.

## Overordnet beskrivelse
I denne deloppgaven vil vi utvikle en meldingsprodusent (NewProductsMessageProducer) for å sende meldinger til Rapid-emnet. I tillegg vil vi også opprette en meldingslytter (MessageLoggerService) som vil logge alle meldinger som sendes på dette emnet.

## Nytt topic: "rapid-1"
Se på oppskriften i oppgave 1, og lag et nytt topic som du kaller "rapid-1"

## NewProductsMessageProducer

### Forbered Enhetstesting
Aktiver testklassen NewProductsMessageProducerTest. Kontroller at testen kan kjøres, selv om den vil feile i denne omgang.

### Implementasjon av NewProductsMessageProducer
Fullfør "TODO"-merket kode i produceMessage()-funksjonen.

### Utfør Enhetstesting
Sørg for at NewProductsMessageProducerTest nå kjøres uten feil.

## MessageLoggerService

### Etabler kodebase
Kommenter inn koden for MessageLoggerService og generer nødvendige metoder som skal overrides.

### Forbered Enhetstesting
Finn enhetstesten for klassen, aktiver den, kjør testen og bekreft at den feiler.

### Fullfør MessageLoggerService
Fullfør alle "TODO"-merkede oppgaver i koden og bekreft at testen nå kjøres uten feil.


## Test Komponentene mot Rapid-emnet
Start MessageLoggerService ved å kjøre dens main-funksjon.
Vent på loggmeldingen "Successfully joined group."
Send en testmelding til "rapid-1" ved å kjøre main-funksjonen i NewProductsMessageProducer.

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




