import { Moment } from 'moment';
import { ITicket } from 'app/shared/model/ticket.model';
import { IPassenger } from 'app/shared/model/passenger.model';
import { ICity } from 'app/shared/model/city.model';

export interface IFlight {
  id?: number;
  number?: string;
  departureTime?: Moment;
  arrivalTime?: Moment;
  tickets?: ITicket[];
  passengers?: IPassenger[];
  to?: ICity;
  from?: ICity;
}

export class Flight implements IFlight {
  constructor(
    public id?: number,
    public number?: string,
    public departureTime?: Moment,
    public arrivalTime?: Moment,
    public tickets?: ITicket[],
    public passengers?: IPassenger[],
    public to?: ICity,
    public from?: ICity
  ) {}
}
