import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Formio, FormioModule } from '@formio/angular';
import premium from '@formio/premium';
import { AppRoutingModule } from '../app-routing.module';
import { LoaderComponent } from '../core/components/loader/loader.component';
import { MaterialModule } from '../material/material.module';
import { SharedModule } from '../shared/shared.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { WelcomeComponent } from './dashboard/welcome/welcome.component';
import { DownloadNexuModalComponent } from './download-nexu-modal/download-nexu-modal.component';
import { EditProfileRolesComponent } from './edit-profile-roles/edit-profile-roles.component';
import { ProfileRolesModalComponent } from './edit-profile-roles/profile-roles-modal/profile-roles-modal.component';
import { HelpApplicationEauComponent } from './help-info/help-application-eau/help-application-eau.component';
import { HelpCenterComponent } from './help-info/help-center/help-center.component';
import { HelpInfoListComponent } from './help-info/help-info-list/help-info-list.component';
import { HelpInfoComponent } from './help-info/help-info/help-info.component';
import { HelpProcessingEauComponent } from './help-info/help-processing-eau/help-processing-eau.component';
import { ActionCard } from './home/action-card/action-card.component';
import { ActionCardsList } from './home/action-cards-list/action-cards-list.component';
import { HomeComponent } from './home/home.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { LoginComponent } from './login-page/login/login.component';
import { RegisterComponent } from './login-page/register/register.component';
import { CurrentTaskComponent } from './proccess/current-task/current-task.component';
import { IoFormBuilderComponent } from './proccess/io-form-builder/io-form-builder.component';
import { NestedFormTitleCardsComponent } from './proccess/io-form-builder/nested-form-title-cards/nested-form-title-cards.component';
import { MobileNavigationComponent } from './proccess/mobile-navigation/mobile-navigation.component';
import { SelectSigneesComponent } from "./proccess/overridden-steps/select-signees/select-signees.component";
import { DaefDocumentComponent } from './proccess/overridden-steps/signing-container/daef-document/daef-document.component';
import { SignMethodSelectComponent } from './proccess/overridden-steps/signing-container/sign-method-select/sign-method-select.component';
import { SigningContainerComponent } from './proccess/overridden-steps/signing-container/signing-container.component';
import { ProcessNavigationComponent } from './proccess/process-navigation/process-navigation.component';
import { ServiceHeaderComponent } from './proccess/service-header/service-header.component';
import { AdminServicesInProgressComponent } from './services/admin-services-in-progress/admin-services-in-progress.component';
import { AdministrationOfServicesComponent } from './services/administration-of-services/administration-of-services.component';
import { AdminServicesCardListComponent } from './services/home-administration-of-services/admin-services-card-list/admin-services-card-list.component';
import { HomeAdministrationOfServicesComponent } from './services/home-administration-of-services/home-administration-of-services.component';
import { ServiceDetailsModalComponent } from './services/service-details-modal/service-details-modal.component';
import { UserServicesComponent } from './services/user-services/user-services.component';
import { SettingsComponent } from './settings/settings.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ServiceSupplierEasComponent } from './services/administration-of-services/meta-edit/service-supplier-eas/service-supplier-eas.component';
import { ChannelTaxComponent } from './services/administration-of-services/meta-edit/channel-tax/channel-tax.component';
import { MetadataEditComponent } from './services/administration-of-services/meta-edit/metadata-edit.component';
import { CollapsableCardComponent } from './services/administration-of-services/meta-edit/collapsable-card/collapsable-card.component';
import { PaymentMethodSelectionComponent } from './proccess/overridden-steps/payment-method-selection/payment-method-selection.component';
import { HiddenBoricaFormComponent } from './proccess/overridden-steps/payment-method-selection/hidden-borica-form/hidden-borica-form.component';
import { TranslateLanguageComponent } from './translation/translate-language/translate-language.component';
import { TranslateListComponent } from './translation/translate-list/translate-list.component';
import { TranslateKeyComponent } from './translation/translate-key/translate-key.component';
import { CaseInnerSectionComponent } from './services/service-details-modal/case-inner-section/case-inner-section.component';
import { ReportComponent } from './report/report.component';
import { ReportsComponent } from './reports/reports.component';
import { HomeReportsComponent } from './home-reports/home-reports.component';
import { ReportCardsListComponent } from './home-reports/report-cards-list/report-cards-list.component';
import { AdditionalTaxStatusComponent } from './proccess/overridden-steps/additional-tax-status/additional-tax-status.component';
import { AdmissibilityOfServiceComponent } from './proccess/overridden-steps/admissibility-of-service/admissibility-of-service.component';
import { AcceptOrDenyServiceComponent } from './proccess/overridden-steps/accept-or-deny-service/accept-or-deny-service.component';

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
    ServiceDetailsModalComponent,
    UserProfileComponent,
    MobileNavigationComponent,
    AdminServicesInProgressComponent,
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
    HomeAdministrationOfServicesComponent,
    AdminServicesCardListComponent,
    UserServicesComponent,
    SelectSigneesComponent,
    SigningContainerComponent,
    DaefDocumentComponent,
    SignMethodSelectComponent,
    ServiceSupplierEasComponent,
    ChannelTaxComponent,
    CollapsableCardComponent,
    PaymentMethodSelectionComponent,
    HiddenBoricaFormComponent,
    MetadataEditComponent,
    TranslateLanguageComponent,
    TranslateListComponent,
    TranslateKeyComponent,
    CaseInnerSectionComponent,
    ReportComponent,
    ReportsComponent,
    HomeReportsComponent,
    ReportCardsListComponent,
    AdditionalTaxStatusComponent,
    AdmissibilityOfServiceComponent,
    AcceptOrDenyServiceComponent
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

