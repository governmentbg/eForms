import { Component, OnInit } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { DeepLinkService } from '../../services/deep-link.service';

@Component({
  selector: 'app-instant-logout',
  templateUrl: './instant-logout.component.html',
  styleUrls: ['./instant-logout.component.scss']
})
export class InstantLogoutComponent implements OnInit {

  constructor(public oidcSecurityService: OidcSecurityService,
    private deepLinkService: DeepLinkService) { }

  ngOnInit(): void {
    this.oidcSecurityService.logoff();
    this.deepLinkService.deleteAllParams();
  }
}
