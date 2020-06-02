import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TicketsSharedModule } from 'app/shared/shared.module';
import { ServiceUserComponent } from './service-user.component';
import { ServiceUserDetailComponent } from './service-user-detail.component';
import { ServiceUserUpdateComponent } from './service-user-update.component';
import { ServiceUserDeleteDialogComponent } from './service-user-delete-dialog.component';
import { serviceUserRoute } from './service-user.route';

@NgModule({
  imports: [TicketsSharedModule, RouterModule.forChild(serviceUserRoute)],
  declarations: [ServiceUserComponent, ServiceUserDetailComponent, ServiceUserUpdateComponent, ServiceUserDeleteDialogComponent],
  entryComponents: [ServiceUserDeleteDialogComponent]
})
export class TicketsServiceUserModule {}
