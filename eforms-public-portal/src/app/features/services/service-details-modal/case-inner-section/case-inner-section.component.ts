import { Component, Input, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { combineLatest, Subject } from 'rxjs';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { TaskService } from 'src/app/core/services/task.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { profileTypeEnum } from 'src/app/core/types/profileTypeEnum';
import { TableColumn } from 'src/app/core/types/table-column';
import { environment } from 'src/environments/environment';
import { ServiceDetailsModalComponent } from '../service-details-modal.component';

@Component({
  selector: 'app-case-inner-section',
  templateUrl: './case-inner-section.component.html',
  styleUrls: ['./case-inner-section.component.scss']
})
export class CaseInnerSectionComponent implements OnInit {

  tableColumns = [
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
    },
    {
      name: 'SERVICES.ASSIGNEE',
      dataKey: 'assigneeFullName',
      position: 'left',
    }
  ];
  staticTableColumns = [
    {
      name: "NAME",
      dataKey: 'personName',
      position: 'left',
    },
    {
      name: "IDENTIFIER",
      dataKey: 'personIdentifier',
    },    
    {
      name: "IDENTIFIER_TYPE",
      dataKey: 'personIdentifierType',
    }
  ];
  apiCallUrl: string;
  personIdentifier: string;
  isServiceCompleted: boolean;
  @Input() caseData;
  applicant;
  signeesProfiles = [];
  tasks = []
  isLastTaskActive : boolean;
  isLastTaskAssigned : boolean;
  isLastTaskAssignedToMe : boolean;
  refreshDataEmmiter: Subject<void> = new Subject<void>();

  constructor(
    private router: Router,
    public dialogRef: MatDialogRef<ServiceDetailsModalComponent>,
    private daefService: DAEFService,
    private translateService: TranslateService,
    private taskService: TaskService,
    private userService: UserProfileService,
    private camundaProcessService: CamundaProcessService
    ) {}

  ngOnInit(): void {
    this.isServiceCompleted = this.caseData.serviceStatus == '99' ||  this.caseData.serviceStatus == '98' || this.caseData.serviceStatus == '21' || this.caseData.serviceStatus == '22' ? true : false;    
    combineLatest(this.taskService.getPreviousTasks(this.caseData.processInstanceId),
    this.daefService.getCaseDetails(this.caseData.businessKey)).subscribe(([previousTasks, caseDetails]) => {
      if((previousTasks as Array<any>).length) {
        this.tasks = (previousTasks as Array<any>)
        this.isLastTaskActive = !this.tasks[this.tasks.length - 1].endTime        
        this.isLastTaskAssigned = !!this.tasks[this.tasks.length - 1].assignee 
        this.isLastTaskAssignedToMe = this.tasks[this.tasks.length - 1].assignee === this.userService.currentUser.personIdentifier
      }
      this.applicant = caseDetails.applicant?.data;
      if(this.applicant) {
        this.applicant.profileType = profileTypeEnum.getDisplayByCode(this.applicant.profileType.toString()).label
      }
      if(caseDetails.signeesProfiles) {
        this.signeesProfiles = caseDetails.signeesProfiles
        this.signeesProfiles.forEach(s => {
          s.personIdentifier = s.personIdentifier.split('-')[1]
          s.personIdentifierType = this.translateService.instant('EGN')
        })
      }
      if(caseDetails.requiredSignaturesSelect === 'requestorAndSigneesSignature' || caseDetails.requiredSignaturesSelect === 'requestor') {
        this.signeesProfiles.splice( 0, 0, {
          personIdentifier : this.caseData.requestor.split('-')[1],
          personName : this.caseData.requestorName,
          personIdentifierType : this.translateService.instant('EGN')
        });
      }
    })
  }

  handleRowActionEvent() {
    this.daefService.setService(this.caseData.serviceId)
    this.camundaProcessService.subject.next({
      businessKey: this.caseData.businessKey,
      id: this.caseData.processInstanceId
    })
    this.router.navigate([`admin-services/process/${this.caseData.processInstanceId}`]);
    this.dialogRef.close();
  }

  handleClaimUnclaim(){
    let currentUser = this.userService.currentUser;
    let task = this.tasks[this.tasks.length - 1]
    if (this.isLastTaskAssigned) {
      this.taskService.unclaimTask(task.id)
        .subscribe(result => {
          this.isLastTaskAssigned = false
          this.isLastTaskAssignedToMe = false
          task.assignee = null;
          task.assigneeFullName = null;
          this.refreshDataEmmiter.next()
        })
    } else {
      if (currentUser != null) {
        this.taskService.claimTask(task.id, currentUser.personIdentifier)
          .subscribe(res => {
            this.isLastTaskAssigned = true
            this.isLastTaskAssignedToMe = true
            task.assignee = currentUser.personIdentifier;
            task.assigneeFullName = currentUser.personName;
            this.refreshDataEmmiter.next()
          })
      }
    }
  }

}
