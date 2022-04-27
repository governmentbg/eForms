import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { UserProfileService } from 'src/app/core/services/user-profile.service';

@Component({
  selector: 'app-user-name-header',
  templateUrl: './user-name-header.component.html',
  styleUrls: ['./user-name-header.component.scss']
})
export class UserNameHeaderComponent implements OnInit {
  displayName: string;
  isAuthenticated: boolean = false;

  @Output() public sidenavClose = new EventEmitter();

  constructor(
    private router: Router,
    private oidcSecurityService: OidcSecurityService,
    private userProfileService: UserProfileService,
  ) { }

  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(isAuthenticated => this.isAuthenticated = isAuthenticated);
    this.userProfileService
      .subscribe((userProfile) => {
        if (userProfile) {
          this.displayName = userProfile.personName;
        }
      });
  }

  openProfile() {
    this.router.navigate(['user-profile']);
    this.sidenavClose.emit();
  }

}
