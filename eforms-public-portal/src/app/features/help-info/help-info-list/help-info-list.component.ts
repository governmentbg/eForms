import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { profileTypes } from 'src/app/core/types/profileTypes';
import { roles } from 'src/app/core/types/roles';


@Component({
  selector: 'app-help-info-list',
  templateUrl: './help-info-list.component.html',
  styleUrls: ['./help-info-list.component.scss']
})
export class HelpInfoListComponent implements OnInit {

  roles;
  profileTypes;

  constructor(private router: Router, public userProfileService: UserProfileService) { }

  ngOnInit(): void {
    this.roles = roles
    this.profileTypes = profileTypes
  }

  back() {
    this.router.navigate(['home']);
  }

}
