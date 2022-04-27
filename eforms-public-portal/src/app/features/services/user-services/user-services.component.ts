import { Component, OnInit } from '@angular/core';
import { TableColumn } from 'src/app/core/types/table-column';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { ServiceDetailsModalComponent } from '../service-details-modal/service-details-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { Formio } from 'formiojs';
import { Subject } from 'rxjs';
import { enumTypes } from 'src/app/core/types/enumTypes';

@Component({
  selector: 'app-user-services',
  templateUrl: './user-services.component.html',
  styleUrls: ['./user-services.component.scss']
})
export class UserServicesComponent implements OnInit {
  tableData
  tableColumns: TableColumn[];
  apiCallUrl: string
  formSrc;
  form;
  filterEventSubject: Subject<any> = new Subject<any>();
  classifier = 'serviceInApplication,serviceInRequest,serviceInCompletion'
  parameters: string = `&classifier=${this.classifier}`;

  constructor(
    private router: Router,
    private daefService: DAEFService,
    private notificationsBannerService: NotificationsBannerService,
    private userProfileService: UserProfileService,
    private camundaProcessService: CamundaProcessService,
    private formioService: FormIoService,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.apiCallUrl = `/api/projects/${environment.formioBaseProject}/cases`
    this.initializeColumns();
    this.filterTable();

    this.formioService.getFormByAlias('common/component/common-case-filter', environment.formioBaseProject).subscribe(result => {
      this.formSrc = result;
      Formio.setBaseUrl(`${environment.apiUrl}`);
    })
  }

  back() {
    this.router.navigate(['/home']);
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
    let enumObject = enumTypes["serviceButtonEnum"].getDisplayByCode(event.data.statusCode);
    if (enumObject && enumObject.icon == 'edit') {
      if (event.data.existService) {
        this.daefService.setService(event.data.serviceId);
        this.camundaProcessService.subject.next({
          businessKey: event.data.businessKey,
          id: event.data.processInstanceId
        })
        this.router.navigate(['my-services', 'process', event.data.processInstanceId], { queryParams: { easId: event.data.serviceId } })
      } else {
        this.notificationsBannerService.show({message: "SERVICES.MISSING_SERVICE", type: NotificationBarType.Info})
      }
    } else {
      this.daefService.getCase(event.data.businessKey).subscribe(result => {
        result.showInnerSection = false;
        this.dialog.open(ServiceDetailsModalComponent, {data: result})
      })
    }
  }

  handleFormReady(form: any) {
    this.form = form;
    this.filterTable();
  }

  handleFilterEvent(event) {
    if (event.type === 'filter') {
      let serviceUri = event.data.serviceUri;
      let serviceName = event.data.serviceName;
      let businessKey = event.data.businessKey;
      if (event.data.caseStageDropdown) {
        this.classifier = event.data.caseStageDropdown;
      } else {
        this.classifier = 'serviceInApplication,serviceInRequest,serviceInCompletion';
      }

      this.parameters = `&classifier=${this.classifier}&serviceUri=${serviceUri}&serviceName=${serviceName}&businessKey=${businessKey}`;
      this.filterTable();
    }
  }

  filterTable() {
    this.filterEventSubject.next(this.parameters);
  }

}
