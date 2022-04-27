import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { DeepLinkService } from 'src/app/core/services/deep-link.service';
import { DAEFService } from 'src/app/core/services/daef-service.service'
import { environment } from 'src/environments/environment';
import { LoginService } from 'src/app/core/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  constructor(
    public oidcSecurityService: OidcSecurityService,
    private loginService: LoginService
    ) { }

  ngOnInit() {
    const assuranceLevel = localStorage.getItem('assuranceLevel');
    if (assuranceLevel) {
      localStorage.removeItem('assuranceLevel');
      this.loginWithAssuranceLevel(assuranceLevel)
    }
  }

  public login(): void {
    this.loginService.login();
  }

  public loginWithAssuranceLevel(assuranceLevel: string) {
    this.loginService.loginWithAssuranceLevel(assuranceLevel);
  }
}
