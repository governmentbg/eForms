import { Component, OnInit } from '@angular/core';
import { TableColumn } from 'src/app/core/types/table-column';
import { Router } from '@angular/router';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { environment } from 'src/environments/environment';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { Formio } from 'formiojs';
import { combineLatest, Observable, Subject } from 'rxjs';
import * as moment from 'moment';
import { MatDialog } from '@angular/material/dialog';
import { ServiceDetailsModalComponent } from '../service-details-modal/service-details-modal.component';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { AbstractControl, FormBuilder, ValidatorFn, Validators } from '@angular/forms';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import { map, startWith } from 'rxjs/operators';


@Component({
  selector: 'app-admin-services-in-progress',
  templateUrl: './admin-services-in-progress.component.html',
  styleUrls: ['./admin-services-in-progress.component.scss']
})
export class AdminServicesInProgressComponent implements OnInit {
  tableData
  tableColumns: TableColumn[];
  apiCallUrl: string
  parameters = ""
  filterFormGroup;
  availableStatuses;
  availableEAUs;
  filteredEAUs: Observable < any > ;
  availableServices;
  isLoading = true;
  statusHasChanged = false;

  filterEventSubject: Subject<any> = new Subject<any>();

  constructor(private router: Router,
    private formioService: FormIoService,
    private deafService: DAEFService,
    private dialog: MatDialog,    
    private formBuilder: FormBuilder,
    private userProfileService: UserProfileService,
    private notificationsBannerService: NotificationsBannerService) { }

  ngOnInit(): void {
    let showSubmissionSuccess = localStorage.getItem('showSubmissionSuccess');
    if (showSubmissionSuccess) {
      this.notificationsBannerService.show({ message: "SUCCESSFUL_SUBMISSION_ADMIN", type: NotificationBarType.Success });
      localStorage.removeItem('showSubmissionSuccess');
    }
    this.apiCallUrl = `/api/admin/projects/${environment.formioBaseProject}/cases`
    this.initializeColumns();
    this.formioService.getFormIoResource('common/resource/case/status','select=data&data.classifier__nin=serviceInApplication,canceledService').subscribe( statuses => {
      this.availableStatuses = statuses
      this.formioService.getFormIoSupplierByIdentifier(this.userProfileService.selectedProfile.identifier).subscribe(supplier => {
        this.availableEAUs = supplier[0].data.administrativeUnitsList
        let defaultStatuses = (statuses as Array<any>).filter(s => { return s.data.classifier === 'serviceInRequest'}).map( s => { return s.data.code})
        this.formioService.getEASforSupplier(defaultStatuses, moment().subtract(1, "months"), '', '', true).subscribe (services => {
          this.availableServices = services
          this.filterFormGroup =  this.formBuilder.group({
            statusCode: [defaultStatuses, [Validators.required]],
            administrationUnitEDelivery: [''],
            fromFilter: [moment().subtract(1, "months"), [Validators.required]],
            toFilter: ['',[this.validToMustBeBiggerThanValidFromValidator().bind(this)]],
            serviceDropdown: [''],
            requestor: [''],
            onBehalfOf: [''],
            uinFilter: ['']
          })
          if(!this.availableEAUs?.length){
            this.filterFormGroup.controls.administrationUnitEDelivery.disable()
          }
          this.filteredEAUs = this.filterFormGroup.controls.administrationUnitEDelivery.valueChanges.pipe(
            startWith(""),
            map(value => 
              {
                if(value){
                  let filteredUnits = this.availableEAUs.filter( u => u.administrationUnit.toLowerCase().includes(value.toString().toLowerCase()))
                  if(!filteredUnits.length) {
                    this.filterFormGroup.controls.administrationUnitEDelivery.setErrors({"no_units_found": "no_units_found"});
                    this.filterFormGroup.controls.administrationUnitEDelivery.touched = true;
                  } else {
                    if(this.availableEAUs.findIndex( u => u.administrationUnit === value) !== - 1) {
                      this.filterFormGroup.controls.administrationUnitEDelivery.setValue(value, {emitEvent: false})
                      this.updateAvailableEAS()
                    }
                    this.filterFormGroup.controls.administrationUnitEDelivery.setErrors();
                    return filteredUnits
                  }
                }
                if(!value) {
                  this.filterFormGroup.controls.administrationUnitEDelivery.setValue(value, {emitEvent: false})
                  this.updateAvailableEAS()
                }

                return this.availableEAUs                
              })
          )
          this.isLoading = false;
          this.handleFilterEvent()
        })
      })
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
        name: 'IDENTIFIER_FL',
        dataKey: 'data.requestor',
        position: 'left',
        isSortable: true,
        isPIN: true
      },
      {
        name: 'IDENTIFIER_UL',
        dataKey: 'data.applicant',
        position: 'left',
        isSortable: true,
      },
      {
        name: 'SERVICES.STATUS',
        dataKey: 'data.statusName',
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
  }

  handleRowActionEvent(event) {
    this.deafService.getCaseForAdmin(event.data.businessKey).subscribe(result => {
      this.dialog.open(ServiceDetailsModalComponent, {data: result})
    })
  }

  handleStatusChange() {
    this.statusHasChanged = true
  }

  handleStatusBlur() {
    if(this.statusHasChanged) {
      this.statusHasChanged = false
      this.updateAvailableEAS()
    }
  }

  handleDateChange(){
    this.filterFormGroup.controls.toFilter.updateValueAndValidity()
    this.filterFormGroup.controls.fromFilter.updateValueAndValidity()
    this.updateAvailableEAS()
  }

  clearUnitValue(){
    this.filterFormGroup.controls.administrationUnitEDelivery.setValue('')
  }

  clearServiceValue(){
    this.filterFormGroup.controls.serviceDropdown.setValue('')
  }

  updateAvailableEAS() {
    if(this.filterFormGroup.value.fromFilter) {
      let unit = this.availableEAUs ? this.availableEAUs.find( u => u.administrationUnit === this.filterFormGroup.value.administrationUnitEDelivery) : ''
      this.formioService.getEASforSupplier(this.filterFormGroup.value.statusCode, this.filterFormGroup.value.fromFilter, this.filterFormGroup.value.toFilter, unit?.administrationUnitEDelivery).subscribe(result => {
      this.availableServices = result
        if(this.availableServices.findIndex(s => { s.serviceId === this.filterFormGroup.value.serviceDropdown}) === -1) {
          this.filterFormGroup.controls.serviceDropdown.setValue('')
        }
      })
    }
  }

  handleFilterEvent() {
    let data = this.filterFormGroup.value
    if(data) {
      let fromDate =  '';
      let toDate =  '';
      if (data.fromFilter) {
        fromDate = '&fromIssueDate='
        fromDate += moment(data.fromFilter).format("YYYY-MM-DD");
        fromDate += 'T00:00:00';
      }
      if (data.toFilter) {
        toDate = '&toIssueDate='
        toDate += moment(data.toFilter).format("YYYY-MM-DD");
        toDate += "T23:59:59";
      }
      let businessKey = ''
      if (data.uinFilter) {
        businessKey= `&businessKey=${data.uinFilter.trim()}`
      }
      let serviceId = ''
      if (data.serviceDropdown) {
        serviceId= `&serviceId=${data.serviceDropdown}`
      }
      let administrationUnitEDelivery = ''
      if (data.administrationUnitEDelivery) {
        let unit = this.availableEAUs.find( u => u.administrationUnit === data.administrationUnitEDelivery)
        administrationUnitEDelivery= `&administrationUnitEDelivery=${unit.administrationUnitEDelivery}`
      }
      let requestor = ''
      if (data.requestor) {
        requestor= `&requestor=${data.requestor}`
      }
      let staturequestorsCode = ''
      if (data.statusCode) {
        staturequestorsCode= `&statusCode=${data.statusCode.join(',')}`
      }      
      let onBehalfOf = ''
      if (data.onBehalfOf) {
        onBehalfOf= `&onBehalfOf=${data.onBehalfOf}`
      }
      this.parameters = `${serviceId}${administrationUnitEDelivery}${requestor}${staturequestorsCode}${businessKey}${fromDate}${toDate}${onBehalfOf}`
      this.filterTable();
    }
  }

  filterTable() {
    this.filterEventSubject.next(this.parameters);
  }

  validToMustBeBiggerThanValidFromValidator(): ValidatorFn {  
    return (control: AbstractControl): { [key: string]: any } | null => {
        if(this.filterFormGroup) {
            this.filterFormGroup.controls.toFilter.errors = {}
            this.filterFormGroup.controls.fromFilter.errors = {}
            if(!this.filterFormGroup.value.toFilter){   
                return null;
            } else if(moment(this.filterFormGroup.value.toFilter).isAfter(this.filterFormGroup.value.fromFilter)) {
                return null;
            } else {
                return {toFilterMustBeBiggerFromFilter: true};
            }
        }
        return null;
    }            
}
}
