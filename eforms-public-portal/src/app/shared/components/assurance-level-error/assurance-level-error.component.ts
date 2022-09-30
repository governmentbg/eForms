import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { DeepLinkService } from 'src/app/core/services/deep-link.service';
import { LoginService } from 'src/app/core/services/login.service';

@Component({
  selector: 'assurance-level-error',
  templateUrl: './assurance-level-error.component.html',
  styleUrls: ['./assurance-level-error.component.scss']
})
export class AssuranceLevelErrorComponent implements OnInit {
  @Input() error;
  @Input() requiredAssuranceLevel;

  constructor(private router: Router,
    public oidcSecurityService: OidcSecurityService,
    private loginService: LoginService,
    private deepLinkService: DeepLinkService) { }

  ngOnInit(): void {
  }

  redirectToHome() {
    this.router.navigate(['home']);
  }

  loginWithAssuranceLevel() {
    this.oidcSecurityService.logoff();
    this.deepLinkService.deleteAllParams();
    localStorage.setItem('assuranceLevel', this.requiredAssuranceLevel);
    localStorage.setItem('navigateTo', window.location.pathname + window.location.search);
  }
}