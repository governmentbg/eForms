import { Component, OnInit } from '@angular/core';
import { TableColumn } from 'src/app/core/types/table-column';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { MatDialog } from '@angular/material/dialog';
import { ProfileRolesModalComponent } from './profile-roles-modal/profile-roles-modal.component';
import { Subject } from 'rxjs';


@Component({
  selector: 'app-edit-profile-roles',
  templateUrl: './edit-profile-roles.component.html',
  styleUrls: ['./edit-profile-roles.component.scss']
})
export class EditProfileRolesComponent implements OnInit {

  tableData
  tableColumns: TableColumn[];
  apiCallUrl: string
  userProfile;
  refreshEventSubject: Subject<any> = new Subject<any>();
  
  constructor(
    private router: Router,
    private dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.apiCallUrl = `/api/admin/projects/${environment.formioBaseProject}/user-profiles`;
    this.initializeColumns();
  }

  back() {
    this.router.navigate(['home']);
  }

  initializeColumns(): void {
    this.tableColumns = [
      {
        name: "PROFILE_ROLES.PROFILE",
        dataKey: 'data.personIdentifier',
        position: 'left',
        isSortable: true
      },
      {
        name: 'PROFILE_ROLES.USER_NAME',
        dataKey: 'data.name',
        position: 'left',
        isSortable: false
      },
      {
        name: 'PROFILE_ROLES.ROLES',
        dataKey: 'data.roles.display',
        position: 'left',
        isSortable: false
      }
    ];
  }

    handleRowActionEvent(event) {
      delete event.data.uniqueKey;

      const dialogRef = this.dialog.open(ProfileRolesModalComponent, {data: event});
      dialogRef.afterClosed().subscribe(result => {
        if(result !== true){
          this.refreshTable()
        }    
      });
    }

    refreshTable() {
      this.refreshEventSubject.next("");
    }
}
