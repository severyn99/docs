import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPassenger } from 'app/shared/model/passenger.model';
import { PassengerService } from './passenger.service';

@Component({
  templateUrl: './passenger-delete-dialog.component.html'
})
export class PassengerDeleteDialogComponent {
  passenger?: IPassenger;

  constructor(protected passengerService: PassengerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.passengerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('passengerListModification');
      this.activeModal.close();
    });
  }
}
