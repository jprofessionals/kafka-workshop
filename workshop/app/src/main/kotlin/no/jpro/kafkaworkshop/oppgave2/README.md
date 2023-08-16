# Oppgave 2: Kafka Producer, Kafka Consumer og Kafka Streams

## Formål
Bli kjent med de grunnleggende byggesteinene som workshopen vil fokusere på.

## Ekstraoppgaver for oppgave 2 (hvis du har tid til overs)

### Ekstraoppgave for oppgave2a: Ytelsesoptimalisering
Opprett en løkke som produserer 100 meldinger og måler hvor lang tid dette tar. 
Lær hvordan du bruker en asynkron send-metode og håndter eventuelle feil ved hjelp av en callback-funksjon for denne metoden. 
Benytt producer.flush() etter at meldingene er sendt for å sikre at alle meldinger sendes før koden avsluttes. Merker du noen forskjell i tidene?

### Ekstraoppgave for oppgave2b: Egen deserializer
Lag en egen deserializer for Message slik at du kan bruke en KafkaConsumer med følgende typesetting:
```kotlin 
KafkaConsumer<String, Message>
```
Hva tror du er fordelene med å ha en egen deserializer? Og hvilke forbedringer ser du for deg å kunne gjøre i denne oppgaven etter å ha implementert din egen deserializer?