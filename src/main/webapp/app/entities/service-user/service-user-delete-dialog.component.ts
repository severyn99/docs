import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IServiceUser } from 'app/shared/model/service-user.model';
import { ServiceUserService } from './service-user.service';

@Component({
  templateUrl: './service-user-delete-dialog.component.html'
})
export class ServiceUserDeleteDialogComponent {
  serviceUser?: IServiceUser;

  constructor(
    protected serviceUserService: ServiceUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('serviceUserListModification');
      this.activeModal.close();
    });
  }
}
