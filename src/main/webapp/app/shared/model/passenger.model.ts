import { IFlight } from 'app/shared/model/flight.model';

export interface IPassenger {
  id?: number;
  name?: string;
  age?: number;
  flight?: IFlight;
}

export class Passenger implements IPassenger {
  constructor(public id?: number, public name?: string, public age?: number, public flight?: IFlight) {}
}
