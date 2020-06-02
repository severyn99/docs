import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServiceUser } from 'app/shared/model/service-user.model';

@Component({
  selector: 'jhi-service-user-detail',
  templateUrl: './service-user-detail.component.html'
})
export class ServiceUserDetailComponent implements OnInit {
  serviceUser: IServiceUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceUser }) => (this.serviceUser = serviceUser));
  }

  previousState(): void {
    window.history.back();
  }
}
