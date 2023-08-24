# Demo for rapids & rivers

## Formål
Eksempel på R&R need pattern der en tjeneste etterspør informasjon som andre tjenester kan levere.
Fungerer også som eksempel på å sende og motta meldinger til/fra rapid for oppgavene.

## Overordnet beskrivelse
I R&R need pattern kan man ha en tjeneste som etterspør data fra Rapid, og andre tjenester kan svare på behovet.
Eksempelet i vårt case er en flybilettjeneste der vi etterspør flybiletter, og får asynkront svar fra flere tilbydere.

## Forventet output

### TravelService
Ticket offer received, offer: 25
Ticket offer received, offer: 22

### MetricService(om den er startet)
TravelService->TicketOfferService1 -> <Antall>
TravelService->TicketOfferService2 -> <Antall>
TicketOfferService1->TravelService -> <Antall>
TicketOfferService2->Travelervice -> <Antall>

### MessageLoggerService(om den er startet)
```
{"eventName":"ticketEvent","payload":{"need":"ticketOffer"},"participatingSystems":[{"applicationName":"TravelService"}]}
{"eventName":"ticketEvent","payload":{"need":"ticketOffer","ticketOffer":22},"participatingSystems":[{"applicationName":"TravelService"},{"applicationName":"TicketOfferService1"}]}
{"eventName":"ticketEvent","payload":{"need":"ticketOffer","ticketOffer":25},"participatingSystems":[{"applicationName":"TravelService"},{"applicationName":"TicketOfferService2"}]}
{"eventName":"ticketEvent","payload":{"need":"ticketOffer","ticketOffer":22,"processed":true},"participatingSystems":[{"applicationName":"TravelService"},{"applicationName":"TicketOfferService1"},{"applicationName":"TicketService"}]}
{"eventName":"ticketEvent","payload":{"need":"ticketOffer","ticketOffer":25,"processed":true},"participatingSystems":[{"applicationName":"TravelService"},{"applicationName":"TicketOfferService2"},{"applicationName":"TicketService"}]}
```