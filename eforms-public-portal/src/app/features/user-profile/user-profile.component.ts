import { Component, EventEmitter, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { TableColumn } from 'src/app/core/types/table-column';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  form
  public environment
  formSrc
  userProfile
  refreshForm
  errorMsg: string
  isProfilesOpen:boolean=true
  tableData
  tableColumns: TableColumn[]
  apiCallUrl
  subData


  constructor(private router: Router, private userProfileService: UserProfileService, private formioService: FormIoService) { }

  ngOnInit() {
    this.apiCallUrl = `/api/projects/${environment.formioBaseProject}/user-profile/update/`;
    this.subData = "profiles";
    this.initializeColumns();

    this.refreshForm = new EventEmitter();
    this.userProfileService.getUpdatedUserProfile().subscribe(result => {
      this.userProfile = result;
      this.userProfile.personIdentifier = this.userProfile.personIdentifier.split('-')[1];
      this.userProfileService.setUserProfile(this.userProfile);
        this.formioService.getFormByAlias('common/component/user-profile', environment.formioBaseProject).subscribe(result => {
        this.formSrc = result;
        this.formSrc.context = {
          selectedProfile: this.userProfileService.selectedProfile
        }
      })
    }, error => {
      if (error.status === 404 && error.message === "USER_PROFILE_NOT_FOUND") {
        this.errorMsg = "USER_PROFILE_ERRORS." + error.message;
      }
      if (error.status === 400 && error.message === "MISSING_USER_PIN") {
        this.errorMsg = "USER_PROFILE_ERRORS." + error.message;
      }
    });
    this.environment = environment;
  }

  handleFormReady(form: any) {
    this.form = form;
    if (this.form.formio.data != this.userProfile) {
      this.refreshForm.emit({
        submission: {
          data: JSON.parse(JSON.stringify(this.userProfile))
        }
      })
    }
  }

  redirectToEgovMySpace() {
    window.open(`https://${environment.egovBaseURL}/wps/myportal/egov/my-space`, '_blank');
  }

  back() {
    this.router.navigate(['home']);
  }

  initializeColumns(): void {
    this.tableColumns = [
      {
        name: 'PROFILE_ROLES.TYPE',
        dataKey: 'profileType',
        isEnum : true,
        enumeration: "profileTypeEnum",
        position: 'left',
        isSortable: false
      },
      {
        name: 'PROFILE_ROLES.TITLE',
        dataKey: 'title',
        position: 'left',
        isSortable: false
      },
      {
        name: 'PROFILE_ROLES.EIK',
        dataKey: 'identifier',
        position: 'left',
        isSortable: false
      },
      {
        name: 'PROFILE_ROLES.STATUS',
        dataKey: 'status',
        isEnum : true,
        enumeration: "profileStatusEnum",
        position: 'left',
        isSortable: false
      },
      {
        name: 'PROFILE_ROLES.ROLES',
        dataKey: 'roles.display',
        position: 'left',
        isSortable: false
      }
    ];
  }
}
