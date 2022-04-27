import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { roles } from 'src/app/core/types/roles';


@Component({
  selector: 'app-my-services-list',
  templateUrl: './my-services-list.component.html',
  styleUrls: ['./my-services-list.component.scss']
})
export class MyServicesListComponent implements OnInit {

  roles;

  constructor(private router: Router, public userProfileService: UserProfileService) { }

  ngOnInit(): void {
    this.roles = roles
  }

  back() {
    this.router.navigate(['home']);
  }

}
