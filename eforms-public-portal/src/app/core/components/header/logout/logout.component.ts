import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { DeepLinkService } from 'src/app/core/services/deep-link.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {
  isAuthenticated: boolean = false;

  constructor(public oidcSecurityService: OidcSecurityService,
    private deepLinkService: DeepLinkService,
    private router: Router) { }

  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(isAuthenticated => this.isAuthenticated = isAuthenticated);
  }

  logout(): void {
    if(this.router.url.match('my-services/process/')) {
      this.router.navigate(['logout'])
    }else {
      this.oidcSecurityService.logoff();
      this.deepLinkService.deleteAllParams();
    }
  }
}
