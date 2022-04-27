import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/shared/components/dialog/dialog.component';

@Component({
  selector: 'app-profile-select',
  templateUrl: './profile-select.component.html',
  styleUrls: ['./profile-select.component.scss']
})
export class ProfileSelectComponent implements OnInit {
  user: any;
  selectedProfile: any

  constructor(
    private userProfileService: UserProfileService,
    private router: Router,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
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
    if (this.router.url.includes('process')) {
      this.showDialog();
    } else {
      this.selectedProfile = profile;
      this.userProfileService.setSelectedProfile(profile);
      this.router.navigate(['home']);
    }
  }

  removeSelectedAdditionalProfile() {
    if (this.router.url.includes('process')) {
      this.showDialog();
    } else {
      this.selectedProfile = null;
      this.userProfileService.removeSelectedProfile();
      this.router.navigate(['home']);
    }
  }

  showDialog() {
    this.dialog.open(DialogComponent, { data: { title: 'IMPORTANT', body: 'CANNOT_CHANGE_PROFILE', canProceed: false } });
  }
}
