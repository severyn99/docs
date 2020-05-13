import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { TicketsSharedModule } from 'app/shared/shared.module';
import { TicketsCoreModule } from 'app/core/core.module';
import { TicketsAppRoutingModule } from './app-routing.module';
import { TicketsHomeModule } from './home/home.module';
import { TicketsEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    TicketsSharedModule,
    TicketsCoreModule,
    TicketsHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    TicketsEntityModule,
    TicketsAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class TicketsAppModule {}
