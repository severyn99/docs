import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICity } from 'app/shared/model/city.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CityService } from './city.service';
import { CityDeleteDialogComponent } from './city-delete-dialog.component';

@Component({
  selector: 'jhi-city',
  templateUrl: './city.component.html'
})
export class CityComponent implements OnInit, OnDestroy {
  cities: ICity[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected cityService: CityService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.cities = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.cityService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICity[]>) => this.paginateCities(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.cities = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCities();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICity): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCities(): void {
    this.eventSubscriber = this.eventManager.subscribe('cityListModification', () => this.reset());
  }

  delete(city: ICity): void {
    const modalRef = this.modalService.open(CityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.city = city;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCities(data: ICity[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.cities.push(data[i]);
      }
    }
  }
}
