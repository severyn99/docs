import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPassenger } from 'app/shared/model/passenger.model';

type EntityResponseType = HttpResponse<IPassenger>;
type EntityArrayResponseType = HttpResponse<IPassenger[]>;

@Injectable({ providedIn: 'root' })
export class PassengerService {
  public resourceUrl = SERVER_API_URL + 'api/passengers';

  constructor(protected http: HttpClient) {}

  create(passenger: IPassenger): Observable<EntityResponseType> {
    return this.http.post<IPassenger>(this.resourceUrl, passenger, { observe: 'response' });
  }

  update(passenger: IPassenger): Observable<EntityResponseType> {
    return this.http.put<IPassenger>(this.resourceUrl, passenger, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPassenger>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPassenger[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
