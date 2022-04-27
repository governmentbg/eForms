import { Component, Input, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { TableColumn } from 'src/app/core/types/table-column';
import { environment } from 'src/environments/environment';
import { ServiceDetailsModalComponent } from '../service-details-modal/service-details-modal.component';

@Component({
  selector: 'app-case-inner-section',
  templateUrl: './case-inner-section.component.html',
  styleUrls: ['./case-inner-section.component.scss']
})
export class CaseInnerSectionComponent implements OnInit {

  tableData
  tableColumns: TableColumn[];
  apiCallUrl: string;
  personIdentifier: string;
  isServiceCompleted: boolean;
  @Input() processInstanceId: string;
  @Input() serviceId: string;
  @Input() serviceStatus: string;
  @Input() projectId: string;

  constructor(
    private router: Router,
    public dialogRef: MatDialogRef<ServiceDetailsModalComponent>,
    private daefService: DAEFService,
    private userProfileService: UserProfileService
    ) {}

  ngOnInit(): void {
    this.isServiceCompleted = this.serviceStatus == '99' ||  this.serviceStatus == '98' || this.serviceStatus == '21' || this.serviceStatus == '22' ? true : false;    
    this.apiCallUrl = `/api/admin/tasks?processInstanceId=${this.processInstanceId}`;    
    if(this.isServiceCompleted){
      this.apiCallUrl = `/api/admin/history-tasks?processInstanceId=${this.processInstanceId}`;
    };
    this.userProfileService
    .subscribe((userProfile) => {
      if (userProfile) {
        this.personIdentifier = userProfile.personIdentifier;
      }
    });
    this.initializeColumns();
    
  }

  initializeColumns(): void {
    if (this.isServiceCompleted) {
      this.tableColumns = [
        {
          name: "SERVICES.NAME",
          dataKey: 'name',
          position: 'left',
        },
        {
          name: 'SERVICES.BEGIN_DATE',
          dataKey: 'startTime',
          position: 'left',
          isDate: true
        },
        {
          name: 'SERVICES.END_DATE',
          dataKey: 'endTime',
          position: 'left',
          isDate: true
        }
      ];
    } else {
      this.tableColumns = [
        {
          name: "SERVICES.NAME",
          dataKey: 'name',
          position: 'left',
        },
        {
          name: 'SERVICES.BEGIN_DATE',
          dataKey: 'created',
          position: 'left',
          isDate: true
        }
      ];
    }
  }

  handleRowActionEvent(event) {
    this.daefService.setService(this.serviceId)
    this.router.navigate([`admin-services/process/${event.processInstanceId}`]);
    this.dialogRef.close();
  }

}
