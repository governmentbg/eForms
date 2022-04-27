import { Component, OnInit } from '@angular/core';
import { TableColumn } from 'src/app/core/types/table-column';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { ServiceDetailsModalComponent } from '../service-details-modal/service-details-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';

@Component({
  selector: 'app-services-in-request',
  templateUrl: './services-in-request.component.html',
  styleUrls: ['./services-in-request.component.scss']
})
export class ServicesInRequestComponent implements OnInit {

  tableData
  tableColumns: TableColumn[];
  apiCallUrl: string

  constructor(private router: Router,
    public dialog: MatDialog,
    private deafService: DAEFService,
    private userProfileService: UserProfileService
  ) { }

  ngOnInit(): void {
    this.apiCallUrl = `/api/projects/${environment.formioBaseProject}/cases`
    this.initializeColumns();
  }

  back() {
    this.router.navigate(['my-services']);
  }

  initializeColumns(): void {
    this.tableColumns = [
      {
        name: "NUMERO_SIGN",
        dataKey: 'data.businessKey',
        position: 'left',
        isSortable: true
      },
      {
        name: "SERVICES.SERVICE_ID",
        dataKey: 'data.serviceId',
        position: 'left',
        isSortable: true
      },
      {
        name: 'SERVICES.NAME',
        dataKey: 'data.serviceName',
        position: 'left',
        isSortable: true
      },
      {
        name: 'SERVICES.SUPPLIER',
        dataKey: 'data.serviceSupplierName',
        position: 'left',
        isSortable: true
      },
      {
        name: 'SERVICES.SERVICE_PAYMENT_STATUS',
        dataKey: 'data.statusFixedTax',
        position: 'left',
        isSortable: true,
        translationPath: 'SERVICES.ENUMS.'
      },
      {
        name: 'SERVICES.STAGE',
        dataKey: 'data.stageName',
        position: 'left',
        isSortable: true
      },
      {
        name: 'SERVICES.STATUS',
        dataKey: 'data.statusName',
        position: 'left',
        isSortable: true
      },
      {
        name: 'SERVICES.ISSUE_DATE',
        dataKey: 'data.issueDate',
        position: 'left',
        isSortable: true,
        isDate: true
      },
      {
        name: 'SERVICES.LAST_CHANGE',
        dataKey: 'modified',
        position: 'left',
        isSortable: true,
        isDate: true
      }
    ];
    if (this.userProfileService.selectedProfile) {
      this.tableColumns.push({
        name: "SERVICES.REQUESTOR",
        dataKey: 'data.requestorFullUserName',
        position: 'left',
        isSortable: true
      })
    }
  }

  handleRowActionEvent(event) {
    if (event.data.statusId == 10) {
      this.router.navigate([`my-services/process/${event.data.processInstanceId}`], { queryParams: { easId: event.data.serviceId } })
    } else {
      this.deafService.getCase(event.data.businessKey).subscribe(result => {
        result.showInnerSection = false;
        this.dialog.open(ServiceDetailsModalComponent, {data: result})
      })
    }
  }

}
