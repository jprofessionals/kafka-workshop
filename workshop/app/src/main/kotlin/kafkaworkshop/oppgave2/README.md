# Oppgave 2 Kafka Producer, Kafka Consumer og Kafka Streams

## Formål
Introduksjon til de grunnleggende byggestenene som resten av workshopen bygger på.

## Eventuelle ekstraoppgaver etter oppgave 2 om du har tid til overs

### Ekstraoppgave 2a: Ytelsesoptmialisering
Lag en løkke som produserer 100 og måler tiden.  Bruk send metode som er asynkron, og håndter feil i callback for den metoden.
Flush til slutt for å sikre sending. Er det forskjell på tiden?

### Ekstraoppgave 2b: Egen deserializer
Se om du kan lage din egen deserializer for Message slik at du kan bruke en KafkaConsumer med typesettingen 
```kotlin 
KafkaConsumer<String, Message>
```
Hvilke forbedringer kan du gjøre i 2b når du har en egen deserializer?










