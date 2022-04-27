import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { Observable } from 'rxjs';
import { isEmpty } from 'lodash';

import { DeepLinkService } from 'src/app/core/services/deep-link.service';
@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit {
  isAuthenticated$: Observable<boolean>;
  private easId = null;

  constructor(
    public oidcSecurityService: OidcSecurityService,
    private router: Router,
    private deepLinkService: DeepLinkService,
    ) { }

  ngOnInit(): void {
    this.isAuthenticated$ = this.oidcSecurityService.isAuthenticated$;
    this.isAuthenticated$.subscribe(isAuthenticated => {
      if (isAuthenticated) {
        this.easId = this.deepLinkService.getParam('easId');
        if (isEmpty(this.easId)) {
          this.router.navigate(['home']);
        } else {
          this.router.navigate(['dashboard']);
        }
      }
    });
  }
}
