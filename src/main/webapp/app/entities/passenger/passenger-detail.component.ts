import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPassenger } from 'app/shared/model/passenger.model';

@Component({
  selector: 'jhi-passenger-detail',
  templateUrl: './passenger-detail.component.html'
})
export class PassengerDetailComponent implements OnInit {
  passenger: IPassenger | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ passenger }) => (this.passenger = passenger));
  }

  previousState(): void {
    window.history.back();
  }
}
