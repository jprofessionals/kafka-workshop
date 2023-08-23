# Oppgave 4a: Utforsk Hjelpefiler for Kafka Rapids & Rivers

## Formål
Denne deloppgaven har som mål å gi deg en oversikt over den felles koden som brukes for å sende og motta meldinger i et Rapid-miljø. 
Du vil også bli introdusert for viktige aspekter å vurdere når du både skriver til og leser fra det samme emnet (topic).

## Overordnet beskrivelse
I den angitte mappen finner du fire nøkkelfiler som tjener som grunnlag for å utvikle komponenter for Kafka Rapids & Rivers. 
Disse filene er utformet for å lette arbeidet med å produsere og konsumere meldinger i et Rapid-system. 
Bli kjent med denne koden, da den vil være nyttig for de etterfølgende oppgavene i oppgave 4.

## Filenes Funksjonalitet

### RapidConfiguration
Dette er en konfigurasjonsklasse for en rapidApplikasjon

### RapidMessage
Denne klassen definerer strukturen på meldingene som sendes på det aktuelle Rapid-topicet, samt operasjoner som kan utføres på disse meldingene.

### MessageProducer
En hjelpeklasse laget for å generere meldinger på Rapid-topicet.

### MessageListener
Dette er en abstrakt klasse som kan brukes for å lytte på meldinger i et Rapid-system.

## Refleksjonsspørsmål
Gå gjennom klassene, tenk gjennom hva de gjør, og se om du kan svare på følgende spørsmål

### Autocommit
Hvis en melding feiler, vil applikasjonen da ignorere meldingen ved neste forsøk, eller vil den prøve å prosessere den på nytt?
Om flagget for autocommit blir satt til false, vil koden sørge for at meldinger som feiler blir forsøkt plukket igjen? Er det noen mangler i koden for dette, eller ville det kunne fungere?

### Potensial for Evig Løkke
Når en applikasjon både sender og lytter til det samme emnet, kan det oppstå en situasjon hvor applikasjonen hører på sin egen melding, resulterende i en evig løkke. 
Finnes det også andre scenarier for evig løkke som involverer flere applikasjoner som bruker det samme Rapid-emnet? 
Hvilke sikkerhetsmekanismer har disse filene for å forhindre slike evige løkker?

### Dataformat i Rapid topic
Hvilke metadatafelter er inkludert i meldingene som sendes i Rapid-emnet, og hva er deres potensielle anvendelser?
