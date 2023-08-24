# Oppgave 4a: Utforsk Hjelpefiler for Kafka Rapids & Rivers

## Formål
Denne deloppgaven har som formål å gi deg en oversikt over den felles koden som brukes for å sende og motta meldinger i et Rapid-miljø. 
Du vil også bli introdusert for noen aspekter du kan vurdere når du både skriver til, og leser fra det samme topicet.

## Overordnet beskrivelse
I den angitte mappen finner du fire filer som hjelper deg å utvikle komponenter for Kafka Rapids & Rivers. 
Disse filene er utformet for å lette arbeidet med å produsere og konsumere meldinger. 
Gjør deg kjent med denne koden, den vil være nyttig for de etterfølgende oppgavene i oppgave 4.

## Filenes Funksjonalitet

### RapidConfiguration
Konfigurasjonsklasse for en rapid-applikasjon.

### RapidMessage
Definerer strukturen på meldingene som sendes til Rapid-topicet, samt operasjoner som kan utføres på disse meldingene.

### MessageProducer
Hjelpeklasse laget for å generere meldinger på rapid-topicet.

### MessageListener
Abstrakt klasse som kan brukes for å lytte på rapid-meldinger.

## Refleksjonsspørsmål
Gå gjennom klassene, tenk gjennom hva de gjør, og se om du kan svare på følgende spørsmål

### Autocommit
Hvis en melding feiler, hopper applikasjonen likevel videre til neste melding. Det fungerer greit i en workshop.
Hvis vi på et tidspunkt endrer til autocommit false, går vi ikke til neste melding. Se på den logikken, og sjekk også om applikasjonen også vil stanse ved feil.

### Problemer med evig Løkke
Når en applikasjon både sender og lytter til det samme topicet, kan det oppstå en situasjon hvor applikasjonen lytter på sin egen melding, som resulterer i en evig løkke. 
Finnes det også andre scenarier for evig løkke, som involverer flere servicer? 
Hvilke sikkerhetsmekanismer har felleskoden for å forhindre slike evige løkker?

### Dataformat i Rapid topic
Hvilke metadatafelter er inkludert i meldingene som sendes i Rapid-emnet, og hva brukes de til?
