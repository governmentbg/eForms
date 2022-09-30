import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { profileTypes } from 'src/app/core/types/profileTypes';
import { roles } from 'src/app/core/types/roles';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-admin-services-card-list',
  templateUrl: './admin-services-card-list.component.html',
  styleUrls: ['./admin-services-card-list.component.scss']
})
export class AdminServicesCardListComponent implements OnInit {
  roles;
  profileTypes;
  environment;
  isProduction: boolean;

  constructor(
    public userProfileService: UserProfileService,
    private router: Router
    ) { }

  ngOnInit(): void {
    this.roles = roles;
    this.profileTypes = profileTypes;
    this.environment = environment;
    this.isProduction = environment.production && environment.environment === 'production';
  }

  back() {
    this.router.navigate(['home']);
  }
}
