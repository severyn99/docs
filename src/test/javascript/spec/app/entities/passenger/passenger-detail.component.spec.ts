import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TicketsTestModule } from '../../../test.module';
import { PassengerDetailComponent } from 'app/entities/passenger/passenger-detail.component';
import { Passenger } from 'app/shared/model/passenger.model';

describe('Component Tests', () => {
  describe('Passenger Management Detail Component', () => {
    let comp: PassengerDetailComponent;
    let fixture: ComponentFixture<PassengerDetailComponent>;
    const route = ({ data: of({ passenger: new Passenger(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TicketsTestModule],
        declarations: [PassengerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PassengerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PassengerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load passenger on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.passenger).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
