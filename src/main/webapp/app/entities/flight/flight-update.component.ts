import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IFlight, Flight } from 'app/shared/model/flight.model';
import { FlightService } from './flight.service';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city/city.service';

@Component({
  selector: 'jhi-flight-update',
  templateUrl: './flight-update.component.html'
})
export class FlightUpdateComponent implements OnInit {
  isSaving = false;
  cities: ICity[] = [];

  editForm = this.fb.group({
    id: [],
    number: [],
    departureTime: [],
    arrivalTime: [],
    to: [],
    from: []
  });

  constructor(
    protected flightService: FlightService,
    protected cityService: CityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flight }) => {
      if (!flight.id) {
        const today = moment().startOf('day');
        flight.departureTime = today;
        flight.arrivalTime = today;
      }

      this.updateForm(flight);

      this.cityService.query().subscribe((res: HttpResponse<ICity[]>) => (this.cities = res.body || []));
    });
  }

  updateForm(flight: IFlight): void {
    this.editForm.patchValue({
      id: flight.id,
      number: flight.number,
      departureTime: flight.departureTime ? flight.departureTime.format(DATE_TIME_FORMAT) : null,
      arrivalTime: flight.arrivalTime ? flight.arrivalTime.format(DATE_TIME_FORMAT) : null,
      to: flight.to,
      from: flight.from
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const flight = this.createFromForm();
    if (flight.id !== undefined) {
      this.subscribeToSaveResponse(this.flightService.update(flight));
    } else {
      this.subscribeToSaveResponse(this.flightService.create(flight));
    }
  }

  private createFromForm(): IFlight {
    return {
      ...new Flight(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      departureTime: this.editForm.get(['departureTime'])!.value
        ? moment(this.editForm.get(['departureTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      arrivalTime: this.editForm.get(['arrivalTime'])!.value
        ? moment(this.editForm.get(['arrivalTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      to: this.editForm.get(['to'])!.value,
      from: this.editForm.get(['from'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFlight>>): void {
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

  trackById(index: number, item: ICity): any {
    return item.id;
  }
}
