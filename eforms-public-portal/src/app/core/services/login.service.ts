import { Injectable } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { environment } from 'src/environments/environment';
import { DAEFService } from './daef-service.service';
import { DeepLinkService } from './deep-link.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private deepLinkService: DeepLinkService,
    private daefService: DAEFService,
    private oidcSecurityService: OidcSecurityService) { }

  public login(): void {
    let customParams = { };
    const serviceId = this.deepLinkService.getParam('easId');
    if (serviceId) {
      this.daefService.getDAEFServiceAssuranceLevel(serviceId).subscribe(
        (response) => {
          const requiredAssuranceLevel = response.data.requiredSecurityLevel;
          if (requiredAssuranceLevel != null && requiredAssuranceLevel != '' && requiredAssuranceLevel.toUpperCase() != 'NONE') {
            customParams['requested_assurance_level'] = requiredAssuranceLevel.toUpperCase();
          }
          this.doLogin(customParams);
        }, 
        (error) => {
          this.doLogin(customParams);
        })
    } else {
      this.doLogin(customParams);
    }
  }

  public loginWithAssuranceLevel(assuranceLevel: string) {
    this.doLogin({
      requested_assurance_level: assuranceLevel.toUpperCase()
    });
  }

  public loginWithoutAssuranceLevel() {
    this.doLogin({ });
  }

  private doLogin(params): void {
    if (environment.defaultIdP != null && environment.defaultIdP != '') {
      params['kc_idp_hint'] = environment.defaultIdP;
    }
    this.oidcSecurityService.authorize({ customParams: params });
    this.deepLinkService.deleteAllParams();
  }
}
