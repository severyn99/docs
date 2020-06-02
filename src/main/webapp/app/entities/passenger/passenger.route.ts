import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPassenger, Passenger } from 'app/shared/model/passenger.model';
import { PassengerService } from './passenger.service';
import { PassengerComponent } from './passenger.component';
import { PassengerDetailComponent } from './passenger-detail.component';
import { PassengerUpdateComponent } from './passenger-update.component';

@Injectable({ providedIn: 'root' })
export class PassengerResolve implements Resolve<IPassenger> {
  constructor(private service: PassengerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPassenger> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((passenger: HttpResponse<Passenger>) => {
          if (passenger.body) {
            return of(passenger.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Passenger());
  }
}

export const passengerRoute: Routes = [
  {
    path: '',
    component: PassengerComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Passengers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PassengerDetailComponent,
    resolve: {
      passenger: PassengerResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Passengers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PassengerUpdateComponent,
    resolve: {
      passenger: PassengerResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Passengers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PassengerUpdateComponent,
    resolve: {
      passenger: PassengerResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Passengers'
    },
    canActivate: [UserRouteAccessService]
  }
];
