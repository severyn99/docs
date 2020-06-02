import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { FlightService } from 'app/entities/flight/flight.service';
import { IFlight, Flight } from 'app/shared/model/flight.model';

describe('Service Tests', () => {
  describe('Flight Service', () => {
    let injector: TestBed;
    let service: FlightService;
    let httpMock: HttpTestingController;
    let elemDefault: IFlight;
    let expectedResult: IFlight | IFlight[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FlightService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Flight(0, 'AAAAAAA', currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            departureTime: currentDate.format(DATE_TIME_FORMAT),
            arrivalTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Flight', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            departureTime: currentDate.format(DATE_TIME_FORMAT),
            arrivalTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            departureTime: currentDate,
            arrivalTime: currentDate
          },
          returnedFromService
        );

        service.create(new Flight()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Flight', () => {
        const returnedFromService = Object.assign(
          {
            number: 'BBBBBB',
            departureTime: currentDate.format(DATE_TIME_FORMAT),
            arrivalTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            departureTime: currentDate,
            arrivalTime: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Flight', () => {
        const returnedFromService = Object.assign(
          {
            number: 'BBBBBB',
            departureTime: currentDate.format(DATE_TIME_FORMAT),
            arrivalTime: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            departureTime: currentDate,
            arrivalTime: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Flight', () => {
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
