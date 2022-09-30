import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
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
import { Subject, Subscription } from 'rxjs';
import { enumTypes } from 'src/app/core/types/enumTypes';
import { SmartTableComponent } from 'src/app/shared/components/smart-table/smart-table.component';

@Component({
  selector: 'app-user-services',
  templateUrl: './user-services.component.html',
  styleUrls: ['./user-services.component.scss']
})
export class UserServicesComponent implements OnInit, OnDestroy {
  /**
   * Wrapper Subscription for services. To be destroyed on ngDestroy so component properly unsubscribes from all services
   * @private
   */
  private wrapperSubscription = new Subscription();

  @ViewChild(SmartTableComponent) smartTableComponent: SmartTableComponent;
  
  tableData
  tableColumns: TableColumn[];
  apiCallUrl: string
  formSrc;
  form;
  filterEventSubject: Subject<any> = new Subject<any>();
  classifier = 'serviceInApplication,serviceInRequest,serviceInCompletion,rejectedService,canceledService'
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
    let showSubmissionSuccess = localStorage.getItem('showSubmissionSuccess');
    if (showSubmissionSuccess) {
      this.notificationsBannerService.show({ message: "SUCCESSFUL_SUBMISSION", type: NotificationBarType.Success });
      localStorage.removeItem('showSubmissionSuccess');
    }


    this.apiCallUrl = `/api/projects/${environment.formioBaseProject}/cases`
    this.initializeColumns();
    this.filterTable();

    this.wrapperSubscription.add(
      this.formioService.getFormByAlias('common/component/common-case-filter', environment.formioBaseProject).subscribe(result => {
        this.formSrc = result;
        Formio.setBaseUrl(`${environment.apiUrl}`);
      })
    )
  }

  ngOnDestroy(): void {
    this.wrapperSubscription.unsubscribe();
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
        name: 'SERVICES.SERVICE_PAYMENT_STATUS_FIXED',
        dataKey: 'data.statusFixedTax',
        position: 'left',
        isSortable: true,
        doTranslate: true,
        translationPath: 'SERVICES.ENUMS.'
      },
      {
        name: 'SERVICES.SERVICE_PAYMENT_STATUS_ADDITIONAL',
        dataKey: 'data.statusCalculatedTax',
        position: 'left',
        isSortable: true,
        doTranslate: true,
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
        dataKey: 'data.deliveryDate',
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
        dataKey: 'data.requestorName',
        position: 'left',
        isSortable: true
      })
    }
  }

  handleRowActionEvent(event) {

    this.wrapperSubscription.add(
      this.daefService.getCase(event.data.businessKey).subscribe(result => {
        
        this.smartTableComponent.tableDataSource.data[event.smartTableIndex] = result;
        this.smartTableComponent.setTableDataSource(this.smartTableComponent.tableDataSource.data, false);

        let enumObject = enumTypes["serviceButtonEnum"].getDisplayByCode(result.data.statusCode);
        if (enumObject && enumObject.icon == 'edit') {
          if (result.data.existService) {
            this.daefService.setService(result.data.serviceId);
            this.camundaProcessService.subject.next({
              businessKey: result.data.businessKey,
              id: result.data.processInstanceId
            })
            this.router.navigate(['my-services', 'process', result.data.processInstanceId], { queryParams: { easId: result.data.serviceId } })
          } else {
            this.notificationsBannerService.show({message: "SERVICES.MISSING_SERVICE", type: NotificationBarType.Info})
          }
        } else {
          
          result.showInnerSection = false;
          const dialogRef =  this.dialog.open(ServiceDetailsModalComponent, {data: result})
         
        }
      })
    )

    
  }

  handleFormReady(form: any) {
    this.form = form;
    this.filterTable();
  }

  handleFilterEvent(event) {
    if (event.type === 'filter') {
      let serviceUri = event.data.serviceUri.trim();
      let serviceName = event.data.serviceName.trim();
      let businessKey = event.data.businessKey.trim();
      if (event.data.caseStageDropdown) {
        this.classifier = event.data.caseStageDropdown;
      } else {
        this.classifier = 'serviceInApplication,serviceInRequest,serviceInCompletion,rejectedService,canceledService';
      }

      this.parameters = `&classifier=${this.classifier}&serviceUri=${serviceUri}&serviceName=${serviceName}&businessKey=${businessKey}`;
      this.filterTable();
    }
  }

  filterTable() {
    this.filterEventSubject.next(this.parameters);
  }

}
