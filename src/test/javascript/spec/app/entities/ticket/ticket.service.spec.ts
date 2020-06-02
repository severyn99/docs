import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { TicketService } from 'app/entities/ticket/ticket.service';
import { ITicket, Ticket } from 'app/shared/model/ticket.model';

describe('Service Tests', () => {
  describe('Ticket Service', () => {
    let injector: TestBed;
    let service: TicketService;
    let httpMock: HttpTestingController;
    let elemDefault: ITicket;
    let expectedResult: ITicket | ITicket[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TicketService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Ticket(0, 'AAAAAAA', 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA', 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            purchased: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Ticket', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            purchased: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            purchased: currentDate
          },
          returnedFromService
        );

        service.create(new Ticket()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ticket', () => {
        const returnedFromService = Object.assign(
          {
            flightNumber: 'BBBBBB',
            username: 'BBBBBB',
            purchased: currentDate.format(DATE_TIME_FORMAT),
            reservationId: 'BBBBBB',
            seatNumber: 'BBBBBB',
            maxKg: 1,
            price: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            purchased: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Ticket', () => {
        const returnedFromService = Object.assign(
          {
            flightNumber: 'BBBBBB',
            username: 'BBBBBB',
            purchased: currentDate.format(DATE_TIME_FORMAT),
            reservationId: 'BBBBBB',
            seatNumber: 'BBBBBB',
            maxKg: 1,
            price: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            purchased: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Ticket', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
