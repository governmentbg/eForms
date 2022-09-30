import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/shared/components/dialog/dialog.component';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-profile-select',
  templateUrl: './profile-select.component.html',
  styleUrls: ['./profile-select.component.scss']
})
export class ProfileSelectComponent implements OnInit {
  user: any;
  selectedProfile: any
  isAuthenticated: boolean = false;

  constructor(
    private userProfileService: UserProfileService,
    private router: Router,
    public dialog: MatDialog,
    public oidcSecurityService: OidcSecurityService,
  ) { }

  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(isAuthenticated => this.isAuthenticated = isAuthenticated);

    this.userProfileService
      .subscribe((userProfile) => {
        if (userProfile) {
          this.user = userProfile;
          let storedProfile = this.userProfileService.selectedProfile;
          let profile = this.user.profiles.find(prof => prof.identifier === storedProfile.identifier);
          if (profile) {
            this.selectedProfile = profile;
            this.userProfileService.setSelectedProfile(profile);
          } else {
            this.userProfileService.removeSelectedProfile();
          }
        }
      });

    this.userProfileService
      .subscribeToSelectedProfile((selectedProfile) => {
        this.selectedProfile = selectedProfile;
      });
  }

  changeProfile(profile) {
    if (this.router.url.includes('process') || this.router.url.includes('administration-of-services/edit') ) {
      this.showDialog();
    } else {
      this.selectedProfile = profile;
      this.userProfileService.setSelectedProfile(profile);
      this.reloadOrNavigate();
    }
  }

  removeSelectedAdditionalProfile() {
    if (this.router.url.includes('process') || this.router.url.includes('administration-of-services/edit')) {
      this.showDialog();
    } else {
      this.selectedProfile = null;
      this.userProfileService.removeSelectedProfile();
      this.reloadOrNavigate();
    }
  }

  showDialog() {
    let body = this.router.url.includes('process') ? 'CANNOT_CHANGE_PROFILE' : 'CANNOT_CHANGE_PROFILE_METADATA_EDIT'
    this.dialog.open(DialogComponent, { data: { title: 'IMPORTANT', body: body, canProceed: false } });
  }

  private reloadOrNavigate()
  {
    if (this.router.url.includes('dashboard')) {
      window.location.reload();
    } else {
      this.router.navigate(['home']);
    }
  }
}
