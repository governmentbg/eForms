import { Component, ComponentFactoryResolver } from '@angular/core';
import { Router } from '@angular/router';
import { Breadcrumb, BreadcrumbsConfig } from '@exalif/ngx-breadcrumbs';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { isEmpty } from 'lodash';
import { DeepLinkService } from './core/services/deep-link.service';
import { TranslateService } from '@ngx-translate/core';
import { environment } from 'src/environments/environment';
import { UserProfileService } from './core/services/user-profile.service';
import { Formio } from 'formiojs';
import premium from '@formio/premium';
import { Subject } from 'rxjs';
import S3 from './core/providers/s3';
import XHR from './core/providers/xhr';
import FileComponent from './core/providers/file';
import DataGridComponent from './core/providers/datagrid';
import ColumnsComponent from './core/providers/columns';
import { LoginService } from './core/services/login.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'public-portal';
  langSelect: string;
  closedEventSubject: Subject<void> = new Subject<void>();

  constructor(
    public oidcSecurityService: OidcSecurityService,
    private router: Router,
    private deepLinkService: DeepLinkService,
    private userProfileService: UserProfileService,
    breadcrumbsConfig: BreadcrumbsConfig,
    translate: TranslateService,
    private loginService: LoginService
  ) {
    translate.addLangs(['en', 'bg']);
    translate.setDefaultLang('bg');
    this.langSelect = localStorage.getItem('language');
    if (this.langSelect && this.langSelect.match(/en|bg/)) {
      translate.use(this.langSelect);
    } else {
      this.langSelect = environment.defaultLanguage;
      translate.use(environment.defaultLanguage);
      localStorage.setItem('language', this.langSelect);
    }
    breadcrumbsConfig.postProcess = (breadcrumbs): Breadcrumb[] => {

      // Ensure that the first breadcrumb always points to home
      if(breadcrumbs.length && breadcrumbs[0]) {
        breadcrumbs[0] = {
          text: '????????????',
          path: '/home'
        }
      }

      return breadcrumbs;
    };
   }

  ngOnInit(): void {
    this.oidcSecurityService.checkAuth()
      .subscribe((isAuthenticated: boolean) => {
        let queryParams = this.extractAndDecodeQueryParams();
        this.redirectProfile(queryParams);
        this.deepLinkService.saveQueryParams(JSON.stringify(queryParams));
        if (!isAuthenticated) {
          if(queryParams.hasOwnProperty('easId') && !window.location.href.match('current-task')){
            localStorage.setItem('navigateTo', `${window.location.origin}/dashboard/${queryParams.easId}`);
            this.loginService.loginWithoutAssuranceLevel()
          } else {
            if (!window.location.href.match('login')) {
              localStorage.setItem('navigateTo', window.location.href);
            }
          this.router.navigate(['login']);
          }
        } else {
          this.oidcSecurityService.userData$
            .subscribe(userData => {
              if (userData) {
                if (environment.production) {
                  this.userProfileService.getUserProfilesWithUpdatedRoles();
                } else {
                  this.userProfileService.getUserProfiles();
                }
                let navigateTo = localStorage.getItem('navigateTo');
                if (navigateTo) {
                  localStorage.removeItem('navigateTo');
                  window.location.href = navigateTo;
                }
              }
            });
        }
      });
      Formio.use(premium);
      Formio.use({
        providers: {
          storage : {
            xhr : XHR,
            s3 :  S3 
          }
        }, 
        components: {
          file: FileComponent,
          columns: ColumnsComponent,
          datagrid: DataGridComponent
        }      
      })
      
      let bearerTokenPlugin = {
        priority: 1,
        preRequest: function (requestArgs) {
          if(requestArgs.opts){
            if(!requestArgs.opts.headers) {
              requestArgs.opts.header.append('Authorization', `Bearer ${JSON.parse(localStorage.getItem('deepLink')).accessToken}`);
            } else {
              if (typeof requestArgs.opts.headers.append === 'function') {
                requestArgs.opts.headers.append('Authorization', `Bearer ${JSON.parse(localStorage.getItem('deepLink')).accessToken}`);
              } else {
                requestArgs.opts.headers['Authorization'] = `Bearer ${JSON.parse(localStorage.getItem('deepLink')).accessToken}`;
              }
            }
          }
        },
        wrapRequestPromise: function(promise, requestArgs){
          return promise.then(result => {
            if(result.hasOwnProperty('type') && result.type === 'form') {
              result['context'] = JSON.parse(localStorage.getItem('formContext'))
              var formatFormComponentsJSON = function (components, pannelIndex) {
                if (components) {
            
                  components.forEach((component) => {

                    if(result['context']?.readOnly){
                      if(!(component?.attributes?.collapseAllParentComponent || component?.event === 'open-all' || component?.event === 'collapse-all')){
                        component['disabled'] = true;
                      }
                    }

                    if (
                      component?.type === "panel" &&
                      !!component.customClass &&
                      component.customClass.includes('bullet-indexed-panel')
                    ) {
                      pannelIndex++
                      component.collapsible = true;
                      component.hideLabel = false;
                      component.title = `<i class="fa bullet-indexed-panel-icon remaining"></i>` + component.title
                      component.collapsed = pannelIndex !== 1
                    }
                    // recursion down the rabbit hole
                    if (component?.components) {
                      formatFormComponentsJSON(component.components, pannelIndex);
                    }
                    if (component.columns) {
                      component.columns.forEach((column) => {
                        formatFormComponentsJSON(column.components,pannelIndex);
                      });
                    }
                    if (component.rows) {
                      if (component.rows.length) {
                        component.rows.forEach((row) => {
                          row.forEach((element) => {
                            formatFormComponentsJSON(element.components,pannelIndex);
                          });
                        });
                      }
                    }
                  });
                }
              }
              
              formatFormComponentsJSON(result.components, 0)
            }
            return result
          })
        }
    };
    
    Formio.registerPlugin(bearerTokenPlugin, 'bearerTokenPlugin');
  }

  private extractAndDecodeQueryParams(queryString?: string): any {
    // if the query string is NULL or undefined
    if (!queryString) {
        queryString = window.location.search.substring(1);
    }

    const params = {};
    if (!isEmpty(queryString)) {
      const queries = queryString.split("&");
  
      queries.forEach((indexQuery: string) => {
          const indexPair = indexQuery.split("=");
  
          const queryKey = decodeURIComponent(indexPair[0]);
          const queryValue = decodeURIComponent(indexPair.length > 1 ? indexPair[1] : "");
  
          params[queryKey] = queryValue;
      });
    }
    params['accessToken'] = this.oidcSecurityService.getToken()
    return params;
  }

  handleOnCloseEvent() {
    this.closedEventSubject.next();
  }

  private redirectProfile(queryParams) {
    if ( queryParams.profileType != 1 && queryParams.profileID ) {
      localStorage.setItem('redirectQueryParams', JSON.stringify(queryParams));
    }

    let redirectQueryParams = JSON.parse(localStorage.getItem('redirectQueryParams'));
    if ( redirectQueryParams && redirectQueryParams.profileType != 1 && redirectQueryParams.profileID ) {
      this.userProfileService.decryptProfileId(redirectQueryParams.profileID).subscribe((profileId) => {
        this.userProfileService.subscribe((userProfile: any) => {
          if ( userProfile ) {
            userProfile.profiles.forEach((profile) => {
              if (profile.profileType == redirectQueryParams.profileType && profile.identifier == profileId) {
                this.userProfileService.setUserByProfileId(profileId.toString());
                localStorage.removeItem('redirectQueryParams');
              }
            });
          }
        });
      });
      this.userProfileService.removeSelectedProfile();
    }
  }
}
