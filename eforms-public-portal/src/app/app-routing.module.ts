import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BreadcrumbsModule } from '@exalif/ngx-breadcrumbs';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { ProcessNavigationComponent } from './features/proccess/process-navigation/process-navigation.component';
import { LoginPageComponent } from './features/login-page/login-page.component';
import { HomeComponent } from './features/home/home.component';
import { ConfirmRedirectGuard } from './core/guards/confirm-redirect.guard';
import { UserProfileComponent } from './features/user-profile/user-profile.component';
import { InstantLogoutComponent } from './core/components/instant-logout/instant-logout.component';
import { ForbiddenComponent } from './core/components/forbidden/forbidden.component';
import { AdminServicesInProgressComponent } from './features/services/admin-services-in-progress/admin-services-in-progress.component';
import { SettingsComponent } from './features/settings/settings.component';
import { HelpInfoComponent } from './features/help-info/help-info/help-info.component';
import { HelpCenterComponent } from './features/help-info/help-center/help-center.component';
import { HelpApplicationEauComponent } from './features/help-info/help-application-eau/help-application-eau.component';
import { HelpProcessingEauComponent } from './features/help-info/help-processing-eau/help-processing-eau.component';
import { AuthGuard } from './core/guards/auth.guard';
import { profileTypes } from './core/types/profileTypes';
import { roles } from './core/types/roles';
import { EditProfileRolesComponent } from './features/edit-profile-roles/edit-profile-roles.component';
import { AdministrationOfServicesComponent } from './features/services/administration-of-services/administration-of-services.component';
import { CurrentTaskComponent } from './features/proccess/current-task/current-task.component';
import { UserServicesComponent } from './features/services/user-services/user-services.component';
import { LogoutGuard } from "./core/guards/logout.guard";
import { MetadataEditComponent } from "./features/services/administration-of-services/meta-edit/metadata-edit.component";
import { HiddenBoricaFormComponent } from './features/proccess/overridden-steps/payment-method-selection/hidden-borica-form/hidden-borica-form.component';
import { TranslateLanguageComponent } from './features/translation/translate-language/translate-language.component';
import { TranslateListComponent } from './features/translation/translate-list/translate-list.component';
import { TranslateKeyComponent } from './features/translation/translate-key/translate-key.component';
import { HomeAdministrationOfServicesComponent } from './features/services/home-administration-of-services/home-administration-of-services.component';
import { ReportComponent } from './features/report/report.component';
import { ReportsComponent } from './features/reports/reports.component';
import { HomeReportsComponent } from './features/home-reports/home-reports.component';

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumbs: 'HOME'
    },
    children: [
      {
        path: 'home',
        component: HomeComponent,
        canActivate: [LogoutGuard]
      },
      {
        path: '',
        component: LoginPageComponent,
      },
      {
        path: 'help-application-eau',
        component: HelpApplicationEauComponent,
        canActivate: [LogoutGuard],
        data: {
          breadcrumbs: 'USER_GUIDE'
        },
      },
      {
        path: 'help-center',
        component: HelpCenterComponent,
        canActivate: [LogoutGuard],
        data: {
          breadcrumbs: 'FOOTER.CONTACT_CENTER'
        },
      },
      {
        path: 'borica',
        component: HiddenBoricaFormComponent,
        canActivate: [LogoutGuard]
      },
      {
        path: 'my-services',
        canActivate: [LogoutGuard, AuthGuard],
        data: {
          breadcrumbs: 'SERVICES.MY_SERVICES',
          roles: [roles.user, roles.admin]
        },
        children: [
          {
            path: '',
            component: UserServicesComponent,
            data: {
              roles: [roles.user, roles.admin]
            }
          },
          {
            path: 'process/:id',
            component: ProcessNavigationComponent,
            canActivate: [LogoutGuard, AuthGuard],
            canDeactivate: [ConfirmRedirectGuard],
            data: {
              breadcrumbs: 'SERVICE_REQUEST',
              roles: [roles.user, roles.admin]
            },
          },
          {
            path: 'process/:id/current-task',
            component: CurrentTaskComponent,
            canActivate: [LogoutGuard, AuthGuard],
            canDeactivate: [ConfirmRedirectGuard],
            data: {
              breadcrumbs: 'DOCUMENT_FOR_SIGNING',
              roles: [roles.user, roles.admin]
            },
          },
          {
            path: 'in-progress',
            component: UserServicesComponent,
            canActivate: [LogoutGuard, AuthGuard],
            data: {
              roles: [roles.user, roles.admin]
            },
          },
          {
            path: 'user-services',
            component: UserServicesComponent,
            canActivate: [LogoutGuard, AuthGuard],
            data: {
              roles: [roles.user, roles.admin]
            },
          },
          {
            path: 'in-request',
            component: UserServicesComponent,
            canActivate: [LogoutGuard, AuthGuard],
            data: {
              roles: [roles.user, roles.admin]
            },
          },
          {
            path: 'completed',
            component: UserServicesComponent,
            canActivate: [LogoutGuard, AuthGuard],
            data: {
              roles: [roles.user, roles.admin]
            },
          },
        ]
      },
      {
        path: 'help-info',
        canActivate: [LogoutGuard],
        data: {
          breadcrumbs: 'HELP_INFO'
        },
        children: [
          {
            path: '',
            component: HelpInfoComponent,
          },
          {
            path: 'help-processing-eau',
            component: HelpProcessingEauComponent,
            canActivate: [LogoutGuard, AuthGuard],
            data: {
              breadcrumbs: 'HELP_INFO_LIST.PROCESS_EAU',
              roles: [roles.serviceManager],
              profileTypes: [profileTypes.administration]
            },
          },
        ]
      },
      {
        path: "user-profile",
        component: UserProfileComponent,
        canActivate: [LogoutGuard],
        data: {
          breadcrumbs: 'PROFILE_DATA'
        }
      },
      {
        path: "settings",
        component: SettingsComponent,
        canActivate: [LogoutGuard],
        data: {
          breadcrumbs: 'SETTINGS'
        }
      },
      {
        path: "edit-profile-roles",
        component: EditProfileRolesComponent,
        canActivate: [LogoutGuard, AuthGuard],
        data: {
          breadcrumbs: 'PROFILE_ROLES.ADMIN_ROLES',
          roles: [roles.admin],
          profileTypes: [profileTypes.administration]
        }
      },
      {
        path: 'admin-services',
        canActivate: [LogoutGuard, AuthGuard],
        data: {
          roles: [roles.serviceManager],
          profileTypes: [profileTypes.administration]
        },
        children: [
          {
            path: 'process/:id',
            component: ProcessNavigationComponent,
            canActivate: [LogoutGuard, AuthGuard],
            canDeactivate: [ConfirmRedirectGuard],
            data: {
              breadcrumbs: 'SERVICES.ADMIN.SERVICE_PROCESSING',
              roles: [roles.serviceManager],
              profileTypes: [profileTypes.administration]
            },
          }
        ]
      },
      {
        path: "administration-of-services",
        canActivate: [LogoutGuard, AuthGuard],
        data: {
          profileTypes: [profileTypes.administration],
        },
        children: [
          {
            path: 'services',
            component: AdministrationOfServicesComponent,
            data: {
              breadcrumbs: 'SERVICES.ADMIN.ADMINISTRATION_OF_SERVICES',
              roles: [roles.metadataManager],
              profileTypes: [profileTypes.administration]
            }
          },
          {
            path: 'edit/:arId/:serviceSupplierCode',
            component: MetadataEditComponent,
            canDeactivate: [ConfirmRedirectGuard],
            data: {
              breadcrumbs: 'SERVICES.ADMIN.ADMINISTRATION_OF_SERVICES',
              roles: [roles.metadataManager],
              profileTypes: [profileTypes.administration]
            }
          }
        ]
      },
      {
        path: "admin-panel",
        canActivate: [LogoutGuard, AuthGuard],
        data: {
          breadcrumbs: 'ADMIN_PANEL_MENU',
          profileTypes: [profileTypes.administration],
        },
        children: [
          {
            path: '',
            component: HomeAdministrationOfServicesComponent,
            data: {
              roles: [roles.admin],
              profileTypes: [profileTypes.administration]
            }
          },
          {
            path: 'translation',
            canActivate: [LogoutGuard, AuthGuard],
            data: {
              breadcrumbs: 'TRANSLATION.SELECT_LANGUAGE',
              roles: [roles.admin]
            },
            children: [
              {
                path: '',
                component: TranslateLanguageComponent,
                data: {
                  roles: [roles.admin]
                }
              },
              {
                path: ':targetLanguageCode',
                component: TranslateListComponent,
                data: {
                  breadcrumbs: 'TRANSLATION.LIST',
                  roles: [roles.admin]
                },
              },
              {
                path: ':targetLanguageCode/:key',
                component: TranslateKeyComponent,
                data: {
                  breadcrumbs: 'TRANSLATION.EDIT',
                  roles: [roles.admin]
                },
              },
            ]
          },
          {
            path: "report-panel",
            canActivate: [LogoutGuard, AuthGuard],
            data: {
              breadcrumbs: 'REPORTS',
              profileTypes: [profileTypes.administration],
            },
            children: [
              {
                path: '',
                component: HomeReportsComponent,
                data: {
                  roles: [roles.admin],
                  profileTypes: [profileTypes.administration]
                }
              },
              {
                path: 'reports',
                canActivate: [LogoutGuard, AuthGuard],
                data: {
                  breadcrumbs: 'REPORTS.LIST',
                  roles: [roles.admin]
                },
                children: [
                  {
                    path: '',
                    component: ReportsComponent,
                    data: {
                      roles: [roles.admin]
                    }
                  },
                  {
                    path: ':dashboardId',
                    component: ReportComponent,
                    data: {
                      breadcrumbs: 'REPORTS.REPORT',
                      roles: [roles.admin]
                    },
                  },
                ]
              }
            ]
          }
        ]
      },
      {
        path: 'dashboard',
        canActivate: [LogoutGuard, AuthGuard],
        data: {
          roles: [roles.user],
        },
        children: [
          {
            path: '',
            component: DashboardComponent
          },
          {
            path: ':id',
            component: DashboardComponent
          },
        ]
      },
      {
        path: 'admin-services',
        canActivate: [LogoutGuard, AuthGuard],
        data: {
          roles: [roles.serviceManager],
          profileTypes: [profileTypes.administration]
        },
        children: [
          {
            path: 'in-progress',
            component: AdminServicesInProgressComponent,
            data: {              
              breadcrumbs: 'SERVICES.ADMIN.SERVICES_IN_PROGRESS',
              roles: [roles.serviceManager],
              profileTypes: [profileTypes.administration]
            }
          },
          {
            path: 'process/:id',
            component: ProcessNavigationComponent,
            canActivate: [LogoutGuard, AuthGuard],
            canDeactivate: [ConfirmRedirectGuard],
            data: {
              breadcrumbs: 'SERVICES.ADMIN.SERVICE_PROCESSING',
              roles: [roles.serviceManager],
              profileTypes: [profileTypes.administration]
            },
          }
        ]
      }
    ]
  },
  {
    path: 'login',
    component: LoginPageComponent
  },
  {
    path: 'logout',
    component: InstantLogoutComponent
  },
  {
    path: '403',
    component: ForbiddenComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
    BreadcrumbsModule.forRoot({
      postProcess: null,
      applyDistinctOn: null,
    }),
  ],
  exports: [RouterModule, BreadcrumbsModule,]
})
export class AppRoutingModule { }
