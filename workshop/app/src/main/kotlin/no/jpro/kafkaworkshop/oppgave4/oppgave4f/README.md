# Oppgave 4f Need pattern

## Formål
Eksempel på R&R need pattern

## Overordnet beskrivelse
I R&R need pattern kan man ha en tjeneste som etterspør data fra Rapid, og andre tjenester kan svare på behovet.
Eksempelet i vårt case er en flybilettjeneste der vi etterspør flybiletter, og får asynkront svar fra flere tilbydere.
Tilbydertjenestene er lagt inn. Vi skal her bare lage 'TicketOfficeService', som etterspør billettene.
Om det er lite tid igjen i workshopen, kan du hente inn 'TicketOfficeService' fra løsning for å spare tid.

## TicketOfficeService
Opprett TicketOfficeService. Se på NewProductsMessageProducer og lytterklassene for inspirasjon. 
Legg merke til at denne klassen starter en melding, og også må starte en lytter i samme klasse for å lytte på svar.
For at vektorene skal skrives ut riktig, må den også legge en kvitteringsmelding på rapid(som i CustomerService)

Finn enhetstest og aktiver den.
Bruk MetricService og MessageLoggerService for å se på resultat.
Hver gang denne klassen startes, sender den en melding, og må vente på at "Successfully joined group" kommer i loggen før den kan behandle resultat.


## Forventet output

### MetricSercice
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