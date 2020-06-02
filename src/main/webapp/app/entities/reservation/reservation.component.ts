import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReservation } from 'app/shared/model/reservation.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ReservationService } from './reservation.service';
import { ReservationDeleteDialogComponent } from './reservation-delete-dialog.component';

@Component({
  selector: 'jhi-reservation',
  templateUrl: './reservation.component.html'
})
export class ReservationComponent implements OnInit, OnDestroy {
  reservations: IReservation[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected reservationService: ReservationService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.reservations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.reservationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IReservation[]>) => this.paginateReservations(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.reservations = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInReservations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IReservation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInReservations(): void {
    this.eventSubscriber = this.eventManager.subscribe('reservationListModification', () => this.reset());
  }

  delete(reservation: IReservation): void {
    const modalRef = this.modalService.open(ReservationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.reservation = reservation;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateReservations(data: IReservation[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.reservations.push(data[i]);
      }
    }
  }
}
