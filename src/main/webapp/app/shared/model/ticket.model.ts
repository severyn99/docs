import { Moment } from 'moment';
import { IServiceUser } from 'app/shared/model/service-user.model';
import { IFlight } from 'app/shared/model/flight.model';

export interface ITicket {
  id?: number;
  flightNumber?: string;
  username?: string;
  purchased?: Moment;
  reservationId?: string;
  seatNumber?: string;
  maxKg?: number;
  price?: number;
  user?: IServiceUser;
  flight?: IFlight;
}

export class Ticket implements ITicket {
  constructor(
    public id?: number,
    public flightNumber?: string,
    public username?: string,
    public purchased?: Moment,
    public reservationId?: string,
    public seatNumber?: string,
    public maxKg?: number,
    public price?: number,
    public user?: IServiceUser,
    public flight?: IFlight
  ) {}
}
