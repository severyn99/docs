import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPassenger } from 'app/shared/model/passenger.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PassengerService } from './passenger.service';
import { PassengerDeleteDialogComponent } from './passenger-delete-dialog.component';

@Component({
  selector: 'jhi-passenger',
  templateUrl: './passenger.component.html'
})
export class PassengerComponent implements OnInit, OnDestroy {
  passengers: IPassenger[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected passengerService: PassengerService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.passengers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.passengerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPassenger[]>) => this.paginatePassengers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.passengers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPassengers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPassenger): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPassengers(): void {
    this.eventSubscriber = this.eventManager.subscribe('passengerListModification', () => this.reset());
  }

  delete(passenger: IPassenger): void {
    const modalRef = this.modalService.open(PassengerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.passenger = passenger;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePassengers(data: IPassenger[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.passengers.push(data[i]);
      }
    }
  }
}
