import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFlight } from 'app/shared/model/flight.model';
import { FlightService } from './flight.service';

@Component({
  templateUrl: './flight-delete-dialog.component.html'
})
export class FlightDeleteDialogComponent {
  flight?: IFlight;

  constructor(protected flightService: FlightService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.flightService.delete(id).subscribe(() => {
      this.eventManager.broadcast('flightListModification');
      this.activeModal.close();
    });
  }
}
