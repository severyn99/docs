import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReservation } from 'app/shared/model/reservation.model';
import { ReservationService } from './reservation.service';

@Component({
  templateUrl: './reservation-delete-dialog.component.html'
})
export class ReservationDeleteDialogComponent {
  reservation?: IReservation;

  constructor(
    protected reservationService: ReservationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reservationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('reservationListModification');
      this.activeModal.close();
    });
  }
}
