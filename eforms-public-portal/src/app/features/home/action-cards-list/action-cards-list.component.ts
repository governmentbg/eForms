import { Component, OnInit } from '@angular/core';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { profileTypes } from 'src/app/core/types/profileTypes';
import { roles } from 'src/app/core/types/roles';
import { environment } from 'src/environments/environment';

declare var process;

@Component({
  selector: 'app-action-cards-list',
  templateUrl: './action-cards-list.component.html',
  styleUrls: ['./action-cards-list.component.scss']
})
export class ActionCardsList implements OnInit {

  roles;
  profileTypes;
  egovLink;
  fulleDeliveryLink;
  isProduction: boolean;

  constructor(public userProfileService: UserProfileService) { }

  ngOnInit(): void {
    this.roles = roles
    this.profileTypes = profileTypes
    this.egovLink = environment.egovBaseURL
    this.isProduction = environment.production && process.env.NODE_ENV === 'production';

    let selectedProfile = this.userProfileService.selectedProfile;
    this.fulleDeliveryLink = `https://${environment.edeliveryURL}/Messages/${environment.production ? 'Inbox' : 'ReceivedMessages'}`;

    if (selectedProfile) {
      this.fulleDeliveryLink += '?selectedProfile=' + selectedProfile.identifier
    }
  }

}
