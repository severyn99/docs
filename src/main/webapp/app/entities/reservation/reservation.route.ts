import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IReservation, Reservation } from 'app/shared/model/reservation.model';
import { ReservationService } from './reservation.service';
import { ReservationComponent } from './reservation.component';
import { ReservationDetailComponent } from './reservation-detail.component';
import { ReservationUpdateComponent } from './reservation-update.component';

@Injectable({ providedIn: 'root' })
export class ReservationResolve implements Resolve<IReservation> {
  constructor(private service: ReservationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReservation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((reservation: HttpResponse<Reservation>) => {
          if (reservation.body) {
            return of(reservation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Reservation());
  }
}

export const reservationRoute: Routes = [
  {
    path: '',
    component: ReservationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Reservations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ReservationDetailComponent,
    resolve: {
      reservation: ReservationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Reservations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ReservationUpdateComponent,
    resolve: {
      reservation: ReservationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Reservations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ReservationUpdateComponent,
    resolve: {
      reservation: ReservationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Reservations'
    },
    canActivate: [UserRouteAccessService]
  }
];
