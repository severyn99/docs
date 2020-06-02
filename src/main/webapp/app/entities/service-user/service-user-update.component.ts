import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IServiceUser, ServiceUser } from 'app/shared/model/service-user.model';
import { ServiceUserService } from './service-user.service';

@Component({
  selector: 'jhi-service-user-update',
  templateUrl: './service-user-update.component.html'
})
export class ServiceUserUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    username: [null, [Validators.required, Validators.maxLength(100)]],
    age: [],
    phone: [],
    creditCard: []
  });

  constructor(protected serviceUserService: ServiceUserService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceUser }) => {
      this.updateForm(serviceUser);
    });
  }

  updateForm(serviceUser: IServiceUser): void {
    this.editForm.patchValue({
      id: serviceUser.id,
      username: serviceUser.username,
      age: serviceUser.age,
      phone: serviceUser.phone,
      creditCard: serviceUser.creditCard
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceUser = this.createFromForm();
    if (serviceUser.id !== undefined) {
      this.subscribeToSaveResponse(this.serviceUserService.update(serviceUser));
    } else {
      this.subscribeToSaveResponse(this.serviceUserService.create(serviceUser));
    }
  }

  private createFromForm(): IServiceUser {
    return {
      ...new ServiceUser(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      age: this.editForm.get(['age'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      creditCard: this.editForm.get(['creditCard'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceUser>>): void {
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
}
