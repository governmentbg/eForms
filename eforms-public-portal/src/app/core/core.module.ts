import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { FeaturesModule } from '../features/features.module';
import { SharedModule } from '../shared/shared.module';
import { MaterialModule } from '../material/material.module';
import { InstantLogoutComponent } from './components/instant-logout/instant-logout.component';
import { LogoutComponent } from './components/header/logout/logout.component';
import { UserNameHeaderComponent } from './components/header/user-name-header/user-name-header.component';
import { ProfileSelectComponent } from './components/header/profile-select/profile-select.component';
import { MobileMenuComponent } from './components/header/mobile-menu/mobile-menu.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';


@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    NotFoundComponent,
    InstantLogoutComponent,
    LogoutComponent,
    UserNameHeaderComponent,
    ProfileSelectComponent,
    MobileMenuComponent,
    ForbiddenComponent
  ],
  imports: [
    FeaturesModule,
    CommonModule,
    SharedModule,
    MaterialModule,
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    MobileMenuComponent,
  ],
  providers: []
})
export class CoreModule { }
