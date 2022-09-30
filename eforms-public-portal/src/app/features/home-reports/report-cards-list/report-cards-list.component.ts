import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { profileTypes } from 'src/app/core/types/profileTypes';
import { roles } from 'src/app/core/types/roles';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-report-cards-list',
  templateUrl: './report-cards-list.component.html',
  styleUrls: ['./report-cards-list.component.scss']
})
export class ReportCardsListComponent implements OnInit {
  roles: any;
  profileTypes: any;
  environment: any;

  constructor(
    public userProfileService: UserProfileService,
    private router: Router
  ) {
    this.roles = roles;
    this.profileTypes = profileTypes;
    this.environment = environment;
  }

  back() {
    this.router.navigate(['admin-panel']);
  }

  ngOnInit(): void {
  }

}
