import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TicketsTestModule } from '../../../test.module';
import { ServiceUserDetailComponent } from 'app/entities/service-user/service-user-detail.component';
import { ServiceUser } from 'app/shared/model/service-user.model';

describe('Component Tests', () => {
  describe('ServiceUser Management Detail Component', () => {
    let comp: ServiceUserDetailComponent;
    let fixture: ComponentFixture<ServiceUserDetailComponent>;
    const route = ({ data: of({ serviceUser: new ServiceUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TicketsTestModule],
        declarations: [ServiceUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ServiceUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServiceUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load serviceUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.serviceUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
