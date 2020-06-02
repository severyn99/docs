import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPassenger, Passenger } from 'app/shared/model/passenger.model';
import { PassengerService } from './passenger.service';
import { IFlight } from 'app/shared/model/flight.model';
import { FlightService } from 'app/entities/flight/flight.service';

@Component({
  selector: 'jhi-passenger-update',
  templateUrl: './passenger-update.component.html'
})
export class PassengerUpdateComponent implements OnInit {
  isSaving = false;
  flights: IFlight[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    age: [],
    flight: []
  });

  constructor(
    protected passengerService: PassengerService,
    protected flightService: FlightService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ passenger }) => {
      this.updateForm(passenger);

      this.flightService.query().subscribe((res: HttpResponse<IFlight[]>) => (this.flights = res.body || []));
    });
  }

  updateForm(passenger: IPassenger): void {
    this.editForm.patchValue({
      id: passenger.id,
      name: passenger.name,
      age: passenger.age,
      flight: passenger.flight
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const passenger = this.createFromForm();
    if (passenger.id !== undefined) {
      this.subscribeToSaveResponse(this.passengerService.update(passenger));
    } else {
      this.subscribeToSaveResponse(this.passengerService.create(passenger));
    }
  }

  private createFromForm(): IPassenger {
    return {
      ...new Passenger(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      age: this.editForm.get(['age'])!.value,
      flight: this.editForm.get(['flight'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPassenger>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IFlight): any {
    return item.id;
  }
}
