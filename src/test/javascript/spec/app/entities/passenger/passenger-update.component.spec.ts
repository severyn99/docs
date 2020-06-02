import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TicketsTestModule } from '../../../test.module';
import { PassengerUpdateComponent } from 'app/entities/passenger/passenger-update.component';
import { PassengerService } from 'app/entities/passenger/passenger.service';
import { Passenger } from 'app/shared/model/passenger.model';

describe('Component Tests', () => {
  describe('Passenger Management Update Component', () => {
    let comp: PassengerUpdateComponent;
    let fixture: ComponentFixture<PassengerUpdateComponent>;
    let service: PassengerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TicketsTestModule],
        declarations: [PassengerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PassengerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PassengerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PassengerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Passenger(123);
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
        const entity = new Passenger();
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
