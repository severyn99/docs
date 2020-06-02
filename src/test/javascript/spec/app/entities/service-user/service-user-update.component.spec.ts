import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TicketsTestModule } from '../../../test.module';
import { ServiceUserUpdateComponent } from 'app/entities/service-user/service-user-update.component';
import { ServiceUserService } from 'app/entities/service-user/service-user.service';
import { ServiceUser } from 'app/shared/model/service-user.model';

describe('Component Tests', () => {
  describe('ServiceUser Management Update Component', () => {
    let comp: ServiceUserUpdateComponent;
    let fixture: ComponentFixture<ServiceUserUpdateComponent>;
    let service: ServiceUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TicketsTestModule],
        declarations: [ServiceUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ServiceUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServiceUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ServiceUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ServiceUser(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ServiceUser();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
