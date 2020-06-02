import { ITicket } from 'app/shared/model/ticket.model';
import { IPayment } from 'app/shared/model/payment.model';

export interface IServiceUser {
  id?: number;
  username?: string;
  age?: number;
  phone?: string;
  creditCard?: string;
  tickets?: ITicket[];
  payments?: IPayment[];
}

export class ServiceUser implements IServiceUser {
  constructor(
    public id?: number,
    public username?: string,
    public age?: number,
    public phone?: string,
    public creditCard?: string,
    public tickets?: ITicket[],
    public payments?: IPayment[]
  ) {}
}
