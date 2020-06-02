import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'service-user',
        loadChildren: () => import('./service-user/service-user.module').then(m => m.TicketsServiceUserModule)
      },
      {
        path: 'passenger',
        loadChildren: () => import('./passenger/passenger.module').then(m => m.TicketsPassengerModule)
      },
      {
        path: 'ticket',
        loadChildren: () => import('./ticket/ticket.module').then(m => m.TicketsTicketModule)
      },
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.TicketsPaymentModule)
      },
      {
        path: 'reservation',
        loadChildren: () => import('./reservation/reservation.module').then(m => m.TicketsReservationModule)
      },
      {
        path: 'city',
        loadChildren: () => import('./city/city.module').then(m => m.TicketsCityModule)
      },
      {
        path: 'flight',
        loadChildren: () => import('./flight/flight.module').then(m => m.TicketsFlightModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class TicketsEntityModule {}
