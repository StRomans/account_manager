import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { AccountManagerSharedModule } from 'app/shared/shared.module';
import { AccountManagerCoreModule } from 'app/core/core.module';
import { AccountManagerAppRoutingModule } from './app-routing.module';
import { AccountManagerHomeModule } from './home/home.module';
import { AccountManagerEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { AccountManagerRenderingModule } from 'app/rendering/rendering.module';

@NgModule({
  imports: [
    BrowserModule,
    AccountManagerSharedModule,
    AccountManagerCoreModule,
    AccountManagerHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    AccountManagerEntityModule,
    AccountManagerRenderingModule,
    AccountManagerAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class AccountManagerAppModule {}
