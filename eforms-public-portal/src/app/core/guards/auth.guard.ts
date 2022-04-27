import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { UserProfileService } from '../services/user-profile.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor (private router: Router, private userProfileService: UserProfileService) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree 
    {
      if (this.userProfileService.checkUserRolesAndType(route.data.roles, route.data.profileTypes)){
        return true
      } else {
        this.router.navigate(['/home']);
        return false;
      }
    }  
}
