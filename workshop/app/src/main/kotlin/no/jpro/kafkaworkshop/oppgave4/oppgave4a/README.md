# Oppgave 4a Gå gjennom hjelpefiler for Kafka Rapids & Rivers

## Formål
Få en oversikt over felleskode som brukes for å sende og produsere meldinger til rapid.
Bli kjent med ting man trenger å tenke på når man skriver og leser fra samme topic.

## Overordnet beskrivelse
I denne mappen finner du fire filer som kan brukes til å lage Kafka Rapids & Rivers komponenter.
Filene skal gjøre det enkelt å opprette producere og konsumere mot en rapid. Gjør deg kjent med koden,
slik at du er klar for å lage applikasjoner som jobber mot rapiden i de resterende oppgavene i oppgave4.

## Ansvarsområder for filene

### RapidConfiguration
Konfigurasjonsklasse for en rapidApplikasjon

### RapidMessage
Strukturen for meldingene som sendes på rapid topicet, og operasjoner som kjøres mot meldingene

### MessageProducer
En hjelpeklasse for å opprette meldinger på Rapid topicet.

### MessageListener
Abstrakt klasse som kan brukes for å lytte på rapid meldinger.

## Spørsmål du kan tenke gjennom
Gå gjennom klassene, tenk gjennom hva de gjør, og se om du kan svare på følgende spørsmål

### Autocommit
Om en melding feiler, vil applikasjonen hoppe over meldingen ved neste anledning, eller prøve på nytt?

### Løkke
Om en applikasjon både lytter og sender til samme topic. Er det muligheter for at applikasjonen lytter på samme melding som den produserer, og vi får en evig løkke?
Kan vi få andre typer evige løkker som involverer flere applikasjoner som gjør operasjoner mot rapid topicet?
Hvilke mekanismer i disse filene er lagt til for å forhindre evig løkke om noe settes opp feil?

### Dataformat på Rapid
Hvilke metadatafelter finnes på meldingene som legges på rapid topicet? Hva kan de brukes til?
