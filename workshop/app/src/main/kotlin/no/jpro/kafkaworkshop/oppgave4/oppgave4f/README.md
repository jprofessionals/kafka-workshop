# Oppgave 4f Implementering av "Need Pattern" i R&R

## Formål
Eksempel på R&R need pattern der en tjeneste etterspør informasjon som andre tjenester kan levere.

## Overordnet beskrivelse
I R&R need pattern kan man ha en tjeneste som etterspør data fra Rapid, og andre tjenester kan svare på behovet.
Eksempelet i vårt case er en flybilettjeneste der vi etterspør flybiletter, og får asynkront svar fra flere tilbydere.
Tilbydertjenestene er lagt inn. Vi skal her bare lage 'TicketOfficeService', som etterspør billettene.
Om det er lite tid igjen i workshopen, kan du hente inn 'TicketOfficeService' fra løsning for å spare tid.

## TicketOfficeService
Opprett en ny klasse kalt TicketOfficeService.
Inspirasjon for oppsettet kan hentes fra NewProductsMessageProducer og lytterklassene.
Du trenger både å implementere lyttermetoder, og ha en start metode for å produsere need. Det blir to kall fra main metoden.
Klassen skal sende en startmelding og også initiere en lytter for å motta svar.
Legg til en kvitteringsmelding i meldingsflyten, likt som i CustomerService.
Se på forventet json for format på meldingene.

I TicketOfficeService, hent ut alle 'ticketOffer', og logg de til infologgen.

## Enhetstesting
Aktiver den tilhørende enhetstesten for TicketOfficeService.
Bruk MetricService og MessageLoggerService for å observere og verifisere resultatene.

## Forventet output

### MetricService
TicketOfficeService->TicketOfferService1 -> <Antall>
TicketOfficeService->TicketOfferService2 -> <Antall>
TicketOfferService1->TicketOfficeService -> <Antall>
TicketOfferService2->TicketOfficeService -> <Antall>

### MessageLoggerService
```
{"eventName":"ticketEvent","payload":{"need":"ticketOffer"},"participatingSystems":[{"applicationName":"TicketOfficeService"}]}
{"eventName":"ticketEvent","payload":{"need":"ticketOffer","ticketOffer":22},"participatingSystems":[{"applicationName":"TicketOfficeService"},{"applicationName":"TicketOfferService1"}]}
{"eventName":"ticketEvent","payload":{"need":"ticketOffer","ticketOffer":25},"participatingSystems":[{"applicationName":"TicketOfficeService"},{"applicationName":"TicketOfferService2"}]}
{"eventName":"ticketEvent","payload":{"need":"ticketOffer","ticketOffer":22,"processed":true},"participatingSystems":[{"applicationName":"TicketOfficeService"},{"applicationName":"TicketOfferService1"},{"applicationName":"TicketOfficeService"}]}
{"eventName":"ticketEvent","payload":{"need":"ticketOffer","ticketOffer":25,"processed":true},"participatingSystems":[{"applicationName":"TicketOfficeService"},{"applicationName":"TicketOfferService2"},{"applicationName":"TicketOfficeService"}]}
```