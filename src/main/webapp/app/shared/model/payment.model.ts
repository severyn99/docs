import { Moment } from 'moment';
import { IServiceUser } from 'app/shared/model/service-user.model';
import { IReservation } from 'app/shared/model/reservation.model';

export interface IPayment {
  id?: number;
  date?: Moment;
  user?: IServiceUser;
  reservation?: IReservation;
}

export class Payment implements IPayment {
  constructor(public id?: number, public date?: Moment, public user?: IServiceUser, public reservation?: IReservation) {}
}
