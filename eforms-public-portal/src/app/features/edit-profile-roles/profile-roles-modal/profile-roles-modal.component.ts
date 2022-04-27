import { Component, Inject, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { roles } from 'src/app/core/types/roles';

@Component({
  selector: 'app-profile-roles-modal',
  templateUrl: './profile-roles-modal.component.html',
  styleUrls: ['./profile-roles-modal.component.scss']
})
export class ProfileRolesModalComponent implements OnInit {

  userLabel : string = roles.user.label;
  adminLabel : string = roles.admin.label;
  metadataManagerLabel : string = roles.metadataManager.label;
  serviceManagerLabel : string = roles.serviceManager.label;
  isUser: boolean;
  isAdmin: boolean;
  isMetadataManager: boolean;
  isServiceManager: boolean;
  
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, 
                      public dialogRef: MatDialogRef<any>,
                      public userProfileService: UserProfileService) {}

  ngOnInit(): void {    
    let userRoles = this.data.data.roles.map(r => r.toLowerCase());

    this.isUser = userRoles.includes(roles.user.code.toLowerCase());
    this.isAdmin = userRoles.includes(roles.admin.code.toLowerCase());
    this.isMetadataManager = userRoles.includes(roles.metadataManager.code.toLowerCase());
    this.isServiceManager = userRoles.includes(roles.serviceManager.code.toLowerCase());
  }

  saveRoles(rolesChanged: NgForm) {
    let roleLength = Object.keys(this.data.data.roles.code).length;  
    let roles = [];     
    for (let i = 0; i < roleLength; i++) {   
        if(this.data.data.roles[i] && (rolesChanged.value[this.data.data.roles.code[i]] || (typeof rolesChanged.value[this.data.data.roles.code[i]] === 'undefined'))){          
          roles.push(this.data.data.roles.code[i]);
      }              
    };
    Object.entries(rolesChanged.value).forEach(
      ([key, value]) => {
        if(value && roles.indexOf(key) === -1){
          roles.push(key);
        }
      }
    );
    this.data.data.roles=roles;
    this.userProfileService.setUserProfileRoles(this.data.data)
    .subscribe(result => {
      this.dialogRef.close();
    });    
  }

}
