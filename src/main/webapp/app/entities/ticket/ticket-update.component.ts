import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITicket, Ticket } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';
import { IServiceUser } from 'app/shared/model/service-user.model';
import { ServiceUserService } from 'app/entities/service-user/service-user.service';
import { IFlight } from 'app/shared/model/flight.model';
import { FlightService } from 'app/entities/flight/flight.service';

type SelectableEntity = IServiceUser | IFlight;

@Component({
  selector: 'jhi-ticket-update',
  templateUrl: './ticket-update.component.html'
})
export class TicketUpdateComponent implements OnInit {
  isSaving = false;
  serviceusers: IServiceUser[] = [];
  flights: IFlight[] = [];

  editForm = this.fb.group({
    id: [],
    flightNumber: [],
    username: [],
    purchased: [],
    reservationId: [],
    seatNumber: [],
    maxKg: [],
    price: [],
    user: [],
    flight: []
  });

  constructor(
    protected ticketService: TicketService,
    protected serviceUserService: ServiceUserService,
    protected flightService: FlightService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticket }) => {
      if (!ticket.id) {
        const today = moment().startOf('day');
        ticket.purchased = today;
      }

      this.updateForm(ticket);

      this.serviceUserService.query().subscribe((res: HttpResponse<IServiceUser[]>) => (this.serviceusers = res.body || []));

      this.flightService.query().subscribe((res: HttpResponse<IFlight[]>) => (this.flights = res.body || []));
    });
  }

  updateForm(ticket: ITicket): void {
    this.editForm.patchValue({
      id: ticket.id,
      flightNumber: ticket.flightNumber,
      username: ticket.username,
      purchased: ticket.purchased ? ticket.purchased.format(DATE_TIME_FORMAT) : null,
      reservationId: ticket.reservationId,
      seatNumber: ticket.seatNumber,
      maxKg: ticket.maxKg,
      price: ticket.price,
      user: ticket.user,
      flight: ticket.flight
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ticket = this.createFromForm();
    if (ticket.id !== undefined) {
      this.subscribeToSaveResponse(this.ticketService.update(ticket));
    } else {
      this.subscribeToSaveResponse(this.ticketService.create(ticket));
    }
  }

  private createFromForm(): ITicket {
    return {
      ...new Ticket(),
      id: this.editForm.get(['id'])!.value,
      flightNumber: this.editForm.get(['flightNumber'])!.value,
      username: this.editForm.get(['username'])!.value,
      purchased: this.editForm.get(['purchased'])!.value ? moment(this.editForm.get(['purchased'])!.value, DATE_TIME_FORMAT) : undefined,
      reservationId: this.editForm.get(['reservationId'])!.value,
      seatNumber: this.editForm.get(['seatNumber'])!.value,
      maxKg: this.editForm.get(['maxKg'])!.value,
      price: this.editForm.get(['price'])!.value,
      user: this.editForm.get(['user'])!.value,
      flight: this.editForm.get(['flight'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITicket>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
