import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IoFormBuilderComponent } from './proccess/io-form-builder/io-form-builder.component';
import { ProcessNavigationComponent } from './proccess/process-navigation/process-navigation.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { RegisterComponent } from './login-page/register/register.component';
import { LoginComponent } from './login-page/login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { WelcomeComponent } from './dashboard/welcome/welcome.component';
import { LoaderComponent } from '../core/components/loader/loader.component';
import { FormioModule } from '@formio/angular';
import { Formio } from '@formio/angular';
import premium from '@formio/premium';
import { MaterialModule } from '../material/material.module';
import { HomeComponent } from './home/home.component';
import { ActionCardsList } from './home/action-cards-list/action-cards-list.component';
import { ActionCard } from './home/action-card/action-card.component';
import { AppRoutingModule } from '../app-routing.module';
import { SharedModule } from '../shared/shared.module';
import { ServiceHeaderComponent } from './proccess/service-header/service-header.component'
import { MyServicesComponent } from './services/my-services/my-services.component';
import { MyServicesListComponent } from './services/my-services-list/my-services-list.component';
import { ServicesInProgressComponent } from './services/services-in-progress/services-in-progress.component';
import { ServicesInRequestComponent } from './services/services-in-request/services-in-request.component';
import { CompletedServicesComponent } from './services/completed-services/completed-services.component';
import { ServiceDetailsModalComponent } from './services/service-details-modal/service-details-modal.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { MobileNavigationComponent } from './proccess/mobile-navigation/mobile-navigation.component';
import { CaseInnerSectionComponent } from './services/case-inner-section/case-inner-section.component';
import { AdminServicesInProgressComponent } from './services/admin-services-in-progress/admin-services-in-progress.component';
import { AdminServicesCompletedComponent } from './services/admin-services-completed/admin-services-completed.component';
import { SettingsComponent } from './settings/settings.component';
import { HelpInfoComponent } from './help-info/help-info/help-info.component';
import { HelpInfoListComponent } from './help-info/help-info-list/help-info-list.component';
import { HelpCenterComponent } from './help-info/help-center/help-center.component';
import { HelpApplicationEauComponent } from './help-info/help-application-eau/help-application-eau.component';
import { HelpProcessingEauComponent } from './help-info/help-processing-eau/help-processing-eau.component';
import { EditProfileRolesComponent } from './edit-profile-roles/edit-profile-roles.component';
import { ProfileRolesModalComponent } from './edit-profile-roles/profile-roles-modal/profile-roles-modal.component';
import { FormsModule } from '@angular/forms';
import { AdministrationOfServicesComponent } from './services/administration-of-services/administration-of-services.component';
import { CurrentTaskComponent } from './proccess/current-task/current-task.component';
import { DownloadNexuModalComponent } from './download-nexu-modal/download-nexu-modal.component'
import { NestedFormTitleCardsComponent } from './proccess/nested-form-title-cards/nested-form-title-cards.component';
import { UserServicesComponent } from './services/user-services/user-services.component';


Formio.use(premium);

@NgModule({
  declarations: [
    IoFormBuilderComponent,
    ProcessNavigationComponent,
    LoginPageComponent,
    RegisterComponent,
    LoginComponent,
    DashboardComponent,
    WelcomeComponent,
    LoaderComponent,
    HomeComponent,
    ActionCardsList,
    ActionCard,
    ServiceHeaderComponent,
    MyServicesComponent,
    MyServicesListComponent,
    ServicesInProgressComponent,
    ServicesInRequestComponent,
    CompletedServicesComponent,
    ServiceDetailsModalComponent,
    UserProfileComponent,
    MobileNavigationComponent,
    CaseInnerSectionComponent,
    AdminServicesInProgressComponent,
    AdminServicesCompletedComponent,
    SettingsComponent,
    HelpInfoComponent,
    HelpInfoListComponent,
    HelpCenterComponent,
    HelpApplicationEauComponent,
    HelpProcessingEauComponent,
    EditProfileRolesComponent,
    ProfileRolesModalComponent,
    AdministrationOfServicesComponent,
    CurrentTaskComponent,
    DownloadNexuModalComponent,
    NestedFormTitleCardsComponent,
    UserServicesComponent,
  ],
  imports: [
    CommonModule,
    FormioModule,
    MaterialModule,
    FormsModule,
    AppRoutingModule,
    SharedModule
  ]
})
export class FeaturesModule { }

