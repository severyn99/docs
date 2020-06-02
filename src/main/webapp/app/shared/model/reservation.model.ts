import { Moment } from 'moment';
import { IPayment } from 'app/shared/model/payment.model';

export interface IReservation {
  id?: number;
  date?: Moment;
  payments?: IPayment[];
}

export class Reservation implements IReservation {
  constructor(public id?: number, public date?: Moment, public payments?: IPayment[]) {}
}
