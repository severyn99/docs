import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFlight } from 'app/shared/model/flight.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FlightService } from './flight.service';
import { FlightDeleteDialogComponent } from './flight-delete-dialog.component';

@Component({
  selector: 'jhi-flight',
  templateUrl: './flight.component.html'
})
export class FlightComponent implements OnInit, OnDestroy {
  flights: IFlight[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected flightService: FlightService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.flights = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.flightService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFlight[]>) => this.paginateFlights(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.flights = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFlights();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFlight): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFlights(): void {
    this.eventSubscriber = this.eventManager.subscribe('flightListModification', () => this.reset());
  }

  delete(flight: IFlight): void {
    const modalRef = this.modalService.open(FlightDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.flight = flight;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFlights(data: IFlight[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.flights.push(data[i]);
      }
    }
  }
}
