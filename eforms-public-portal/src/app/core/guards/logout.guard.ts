import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {OidcSecurityService} from "angular-auth-oidc-client";
import {DeepLinkService} from "../services/deep-link.service";
import {Observable, Subscription} from "rxjs";
import {map} from "rxjs/operators";
import {UserProfileService} from "../services/user-profile.service";

@Injectable({
  providedIn: 'root'
})
export class LogoutGuard implements CanActivate {

    constructor (
      private router: Router,
      private userProfileService: UserProfileService,
      public oidcSecurityService: OidcSecurityService,
      private deepLinkService: DeepLinkService
    ) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot)
    {

        return this.oidcSecurityService.isAuthenticated$.pipe(
            map(isAuthenticated => {
                if(!isAuthenticated) {
                    this.userProfileService.setUserProfile(null);
                    this.deepLinkService.deleteAllParams();
                    this.router.navigate(['login']);
                    return false;
                }
                return true;
            })
        )


    }
}
