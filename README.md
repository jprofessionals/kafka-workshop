# Kafka Workshop for Jprofessionals

## Forutsetninger
Et utviklingsmiljø som støtter nyere versjoner av Gradle, Java, og Kotlin

## Når du har klonet prosjektet
Sjekk at build.gradle.kts refererer til en Java-versjon du har installert. For øyeblikket er det satt til:
```kotlin
    kotlinOptions {
        jvmTarget = "17"
    }
```
Hvis du bruker en annen versjon av java, kan du endre versjon i konfigurasjonsfilen.

## Veiledning for deltakere
- Åpne workshop-mappen i ditt foretrukne utviklingsmiljø. Vi anbefaler IntelliJ, da workshopen er designet med dette i tankene.
- Pass på at prosjektet bygger ved hjelp av Gradle-oppsettet inkludert i prosjektet.
- Start med å lese dette: workshop/app/src/main/kotlin/kafkaworkshop/oppgave1/README.md.
- Skulle du stå fast på en oppgave, finner du løsningene i ../fasilitering/løsning-mappen.
