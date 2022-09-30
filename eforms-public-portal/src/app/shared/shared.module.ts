import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TooltipDirective } from './directives/tooltip.directive';
import { DialogComponent } from './components/dialog/dialog.component';
import { MaterialModule } from '../material/material.module';
import { BreadcrumbsComponent } from './components/breadcrumbs/breadcrumbs.component';
import { ErrorComponent } from './components/error/error.component';
import { AssuranceLevelErrorComponent } from './components/assurance-level-error/assurance-level-error.component';
import { NotificationsBannerComponent } from './components/notifications-banner/notifications-banner.component';
import { OrnComponent } from './components/orn/orn.component';
import { SmartTableComponent } from './components/smart-table/smart-table.component';
import { DataPropertyGetterPipe } from './components/smart-table/data-property-getter-pipe/data-property-getter.pipe';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { AssigneeButtonComponent } from './components/assignee-button/assignee-button.component';
import { UserComponent } from './components/user/user.component';
import {AdvancedDialogComponent} from "./components/advanced-dialog/advanced.dialog.component";
import { environment } from 'src/environments/environment';
import { LanguageSelectComponent } from './components/language-select/language-select.component';
import { StaticTableComponent } from './components/static-table/static-table.component';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, `api/public/projects/${environment.formioBaseProject}/i18n/`,  '');
}

@NgModule({
  declarations: [
    TooltipDirective,
    DialogComponent,
    AdvancedDialogComponent,
    BreadcrumbsComponent,
    ErrorComponent,
    AssuranceLevelErrorComponent,
    NotificationsBannerComponent,
    OrnComponent,
    SmartTableComponent,
    DataPropertyGetterPipe,
    AssigneeButtonComponent,
    UserComponent,
    LanguageSelectComponent,
    StaticTableComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    RouterModule,
    TranslateModule.forRoot({
      defaultLanguage: environment.defaultLanguage,
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    InfiniteScrollModule,
  ],
  exports: [
    TranslateModule,
    TooltipDirective,
    DialogComponent,
    AdvancedDialogComponent,
    BreadcrumbsComponent,
    ErrorComponent,
    AssuranceLevelErrorComponent,
    NotificationsBannerComponent,
    OrnComponent,
    SmartTableComponent,
    DataPropertyGetterPipe,
    UserComponent,
    LanguageSelectComponent,
    StaticTableComponent
  ]
})
export class SharedModule { }
