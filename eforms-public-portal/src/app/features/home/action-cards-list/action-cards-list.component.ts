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
  fulleDeliveryLink: string;
  isProduction: boolean;
  eDeliveryUrl: URL;

  constructor(
    public userProfileService: UserProfileService
  ) {
    this.fulleDeliveryLink = `https://${environment.edeliveryURL}/Account/CertificateAuthV2?login=True&returnUrl=/Messages/Inbox`;
    this.eDeliveryUrl = new URL(this.fulleDeliveryLink);
  }

  ngOnInit(): void {
    this.roles = roles
    this.profileTypes = profileTypes
    this.egovLink = environment.egovBaseURL
    this.isProduction = environment.production && environment.environment === 'production';

    this.userProfileService.additionalProfileSubject.subscribe((selectedProfile) => {
      if (selectedProfile) {
        this.eDeliveryUrl.searchParams.set('profileID', selectedProfile.identifier);
        this.eDeliveryUrl.searchParams.set('profileIDType', selectedProfile.identifierType);
      } else {
        this.eDeliveryUrl.searchParams.delete('profileID');
        this.eDeliveryUrl.searchParams.delete('profileIDType');
      }

      this.fulleDeliveryLink = this.eDeliveryUrl.toString();
    });
  }

}
