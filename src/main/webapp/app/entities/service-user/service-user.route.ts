import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IServiceUser, ServiceUser } from 'app/shared/model/service-user.model';
import { ServiceUserService } from './service-user.service';
import { ServiceUserComponent } from './service-user.component';
import { ServiceUserDetailComponent } from './service-user-detail.component';
import { ServiceUserUpdateComponent } from './service-user-update.component';

@Injectable({ providedIn: 'root' })
export class ServiceUserResolve implements Resolve<IServiceUser> {
  constructor(private service: ServiceUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServiceUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((serviceUser: HttpResponse<ServiceUser>) => {
          if (serviceUser.body) {
            return of(serviceUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServiceUser());
  }
}

export const serviceUserRoute: Routes = [
  {
    path: '',
    component: ServiceUserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ServiceUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ServiceUserDetailComponent,
    resolve: {
      serviceUser: ServiceUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ServiceUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ServiceUserUpdateComponent,
    resolve: {
      serviceUser: ServiceUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ServiceUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ServiceUserUpdateComponent,
    resolve: {
      serviceUser: ServiceUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ServiceUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];
