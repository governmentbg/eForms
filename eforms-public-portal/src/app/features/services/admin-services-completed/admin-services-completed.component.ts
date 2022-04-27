import { Component, OnInit } from '@angular/core';
import { TableColumn } from 'src/app/core/types/table-column';
import { Router } from '@angular/router';
import { ServiceDetailsModalComponent } from '../service-details-modal/service-details-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { environment } from 'src/environments/environment';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { Formio } from 'formiojs';
import { Subject } from 'rxjs';
import * as moment from 'moment';
import { UserProfileService } from 'src/app/core/services/user-profile.service';

@Component({
  selector: 'app-admin-services-completed',
  templateUrl: './admin-services-completed.component.html',
  styleUrls: ['./admin-services-completed.component.scss']
})
export class AdminServicesCompletedComponent implements OnInit {
  tableData;
  tableColumns: TableColumn[];
  apiCallUrl: string;
  form;
  formSrc;
  parameters: string;
  filterEventSubject: Subject<any> = new Subject<any>();

  constructor(private router: Router,
    public dialog: MatDialog,
    private deafService: DAEFService,
    private formioService: FormIoService,
    private userProfileService: UserProfileService) { }

  ngOnInit(): void {
    this.initializeColumns();
    this.apiCallUrl = `/api/admin/projects/${environment.formioBaseProject}/cases`;

      this.formioService.getFormByAlias('common/component/case-filter', environment.formioBaseProject).subscribe(result => {
      this.formSrc = result;
      let baseUrl = `${environment.apiUrl}`
      Formio.setBaseUrl(baseUrl);
      this.formSrc.context = {
        classifier: 'serviceInCompletion',    
        formioBaseProject: environment.formioBaseProject,
        apiUrl: environment.apiUrl,
        selectedProfile: this.userProfileService.selectedProfile
      }
      })
  }

  back() {
    this.router.navigate(['home']);
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
        name: 'SERVICES.ADMIN.REQUESTOR',
        dataKey: 'data.requestorFullUserName',
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
        name: 'SERVICES.SERVICE_PAYMENT_STATUS',
        dataKey: 'data.statusFixedTax',
        position: 'left',
        isSortable: true,
        translationPath: 'SERVICES.ENUMS.'
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
  }

  handleRowActionEvent(event) {
    this.deafService.getCaseForAdmin(event.data.businessKey).subscribe(result => {
      this.dialog.open(ServiceDetailsModalComponent, {data: result})
    })
  }

  handleFormReady(form: any) {
    this.form = form;
    this.filterTable();
  }

  handleFilterEvent(event) {
    if (event.type === 'filter') {
      let fromDate = event.data.fromFilter;
      let toDate = event.data.toFilter;
      if (fromDate) {
        fromDate = moment(event.data.fromFilter).format("YYYY-MM-DD");
        fromDate += 'T00:00:00';
      }
      if (toDate) {
        toDate = moment(event.data.toFilter).format("YYYY-MM-DD");
        toDate += "T23:59:59";
      }
      let serviceId = '';      
      if(event.data.serviceDropdown.serviceId){
        serviceId =event.data.serviceDropdown.serviceId;
      }
      this.parameters = `&serviceId=${serviceId}&businessKey=${event.data.uinFilter}&requestorName=${event.data.requestorNameFilter}&fromIssueDate=${fromDate}&toIssueDate=${toDate}`
      this.filterTable();
    }
  }

  filterTable() {
    this.filterEventSubject.next(this.parameters);
  }
}
