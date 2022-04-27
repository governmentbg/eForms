import { Component, OnInit } from '@angular/core';
import { TableColumn } from 'src/app/core/types/table-column';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';

@Component({
  selector: 'app-services-in-progress',
  templateUrl: './services-in-progress.component.html',
  styleUrls: ['./services-in-progress.component.scss']
})
export class ServicesInProgressComponent implements OnInit {
  tableData
  tableColumns: TableColumn[];
  apiCallUrl: string

  constructor(
    private router: Router,
    private daefService: DAEFService,
    private notificationsBannerService: NotificationsBannerService,
    private userProfileService: UserProfileService,
    private camundaProcessService: CamundaProcessService
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
    if (event.data.existService) {
      this.daefService.setService(event.data.serviceId);
      this.camundaProcessService.subject.next({
        businessKey: event.data.businessKey,
        id: event.data.processInstanceId
      })
      this.router.navigate([`my-services/process/${event.data.processInstanceId}`], { queryParams: { easId: event.data.serviceId } })
    } else {
      this.notificationsBannerService.show({message: "SERVICES.MISSING_SERVICE", type: NotificationBarType.Info})
    }
  }
}
