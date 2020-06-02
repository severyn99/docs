import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFlight, Flight } from 'app/shared/model/flight.model';
import { FlightService } from './flight.service';
import { FlightComponent } from './flight.component';
import { FlightDetailComponent } from './flight-detail.component';
import { FlightUpdateComponent } from './flight-update.component';

@Injectable({ providedIn: 'root' })
export class FlightResolve implements Resolve<IFlight> {
  constructor(private service: FlightService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFlight> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((flight: HttpResponse<Flight>) => {
          if (flight.body) {
            return of(flight.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Flight());
  }
}

export const flightRoute: Routes = [
  {
    path: '',
    component: FlightComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Flights'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FlightDetailComponent,
    resolve: {
      flight: FlightResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Flights'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FlightUpdateComponent,
    resolve: {
      flight: FlightResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Flights'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FlightUpdateComponent,
    resolve: {
      flight: FlightResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Flights'
    },
    canActivate: [UserRouteAccessService]
  }
];
