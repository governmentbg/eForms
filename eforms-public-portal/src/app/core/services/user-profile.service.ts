import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { BehaviorSubject, Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';
import { roles } from '../types/roles';
import { UserProfile, UserProfileResponse } from '../types/user-profile';
import { DeepLinkService } from 'src/app/core/services/deep-link.service';


@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  subject: BehaviorSubject<UserProfile>;
  private userProfile: UserProfile;
  additionalProfileSubject: BehaviorSubject<any>;
  additionalProfile: any;
  user;

  private assuranceLevels = {
    LOW: 1, 
    SUBSTANTIAL: 2, 
    HIGH: 3
  }

  constructor(
    private http: HttpClient,
    public oidcSecurityService: OidcSecurityService,
    private deepLinkService: DeepLinkService,
  ) {
    this.subject = new BehaviorSubject<UserProfile>(this.userProfile);
    this.additionalProfileSubject = new BehaviorSubject<any>(this.additionalProfile);
  }

  public subscribe(callback: (model: UserProfile) => void): Subscription {
    return this.subject.subscribe(callback);
  }

  public isValidAssuranceLevel(requestedAssuranceLevel: String): boolean {
    if (requestedAssuranceLevel != null && requestedAssuranceLevel.toUpperCase() != 'NONE') {
      const idTokenPayload = this.oidcSecurityService.getPayloadFromIdToken();
      if (idTokenPayload != null) {
        if (idTokenPayload.assurance_level === undefined || this.assuranceLevels[idTokenPayload.assurance_level.toUpperCase()] === undefined) {
          idTokenPayload.assurance_level = 'LOW';
        }
        return this.assuranceLevels[requestedAssuranceLevel.toUpperCase()] <= this.assuranceLevels[idTokenPayload.assurance_level.toUpperCase()];
      }
      return false;
    }
    return true;
  }

  getUserProfiles() {
    return this.http.get<UserProfile>(`/api/projects/${environment.formioBaseProject}/user-profile`)
      .subscribe(response => {
        this.userProfile = response;
        this.subject.next(response);
      });
  }

  getUserProfilesWithUpdatedRoles() {
    return this.http.get<UserProfile>(`/api/projects/${environment.formioBaseProject}/user-profile/update-additional-profiles`)
      .subscribe(response => {
        this.userProfile = response;
        this.subject.next(response);
      });
  }

  getUpdatedUserProfile() {
    return this.http.get<UserProfile>(`/api/projects/${environment.formioBaseProject}/user-profile/update`)
  }

  setUserProfile(userProfile) {
    this.userProfile = userProfile;
    this.subject.next(userProfile);
  }

  setUserProfileRoles(userProfile) {
    return this.http.put(`/api/admin/projects/${environment.formioBaseProject}/user-profiles`, userProfile);
  }

  get currentUser() {
    return this.userProfile;
  }

  setSelectedProfile(profile: UserProfile) {
    this.deepLinkService.setParam('selectedProfile', JSON.parse(JSON.stringify(profile)));
    this.additionalProfile = profile;
    this.additionalProfileSubject.next(this.additionalProfile);
  }

  removeSelectedProfile() {
    this.deepLinkService.deleteParam('selectedProfile');
    this.additionalProfile = null;
    this.additionalProfileSubject.next(this.additionalProfile);
  }

  get selectedProfile() {
    return this.deepLinkService.getParam('selectedProfile');
  }

  public subscribeToSelectedProfile(callback: (profile) => void): Subscription {
    return this.additionalProfileSubject.subscribe(callback);
  }

  checkUserRolesAndType(allowedRoles = null, allowedProfileTypes = null) {
    let userHasRole = true
    let userIsCorrectType = true
    
    //Проверка за физическо лице
    if (!this.selectedProfile) {
      return allowedRoles
        .map(a => a.code.toLowerCase())
        .includes(roles.user.code.toLowerCase());
    }
    if (allowedRoles) {
      let rolesIntersection = allowedRoles
        .map(a => a.code.toLowerCase())
        .filter(value => 
          this.selectedProfile.roles.map(r => r.toLowerCase()).includes(value));
      userHasRole = rolesIntersection.length > 0
    }
    if (allowedProfileTypes) {
      userIsCorrectType = allowedProfileTypes.includes(this.selectedProfile.profileType)
    }
    return (userIsCorrectType && userHasRole);
  }

  decryptProfileId(encryptedProfileId) {
    return this.http.get(`/api/projects/${environment.formioBaseProject}/user-profile/profileId?encryptProfileId=${encodeURIComponent(encryptedProfileId)}`);
  }

  public setUserByProfileId(profileId: string) {
    if (this.userProfile) {
      this.user = this.userProfile
      let profile = this.user.profiles.find(prof => prof.identifier == profileId);
      if (profile) {
        this.setSelectedProfile(profile);
      }
    }
  }
}
