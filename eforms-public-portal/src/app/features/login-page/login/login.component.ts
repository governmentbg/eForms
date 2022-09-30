import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { DeepLinkService } from 'src/app/core/services/deep-link.service';
import { DAEFService } from 'src/app/core/services/daef-service.service'
import { environment } from 'src/environments/environment';
import { LoginService } from 'src/app/core/services/login.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  constructor(
    private notificationsBannerService: NotificationsBannerService,
    public oidcSecurityService: OidcSecurityService,
    private loginService: LoginService
    ) { }

  ngOnInit() {
    const assuranceLevel = localStorage.getItem('assuranceLevel');
    if (assuranceLevel) {
      localStorage.removeItem('assuranceLevel');
      this.loginWithAssuranceLevel(assuranceLevel)
    }

    let invalidGrantMessage = localStorage.getItem('INVALID_GRANT_MESSAGE');
    if (invalidGrantMessage) {
      localStorage.removeItem('INVALID_GRANT_MESSAGE');
      this.notificationsBannerService.show({message: "ERRORS.INVALID_GRANT", type: NotificationBarType.Error});
    }
  }

  public login(): void {
    this.loginService.login();
  }

  public loginWithAssuranceLevel(assuranceLevel: string) {
    this.loginService.loginWithAssuranceLevel(assuranceLevel);
  }
}
