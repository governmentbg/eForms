import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import * as moment from 'moment';
import { BehaviorSubject, combineLatest, Subject } from 'rxjs';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { metadataResources } from 'src/app/core/types/metadata-resources';
import { AdvancedDialogComponent } from 'src/app/shared/components/advanced-dialog/advanced.dialog.component';
import { DialogComponent } from 'src/app/shared/components/dialog/dialog.component';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';

@Component({
  selector: 'app-metadata-edit',
  templateUrl: './metadata-edit.component.html',
  styleUrls: ['./metadata-edit.component.scss']
})
export class MetadataEditComponent implements OnInit {
  @HostListener('window:beforeunload', ['$event']) handleBeforeHide(event) {
    return !this.shouldPromptBeforeLeave;
  }

  @HostListener('window:pagehide', ['$event']) handleBeforeUnload(event) {
    return !this.shouldPromptBeforeLeave;
  }
  /**
   * ElementRef for supplier basic info form
  */
  @ViewChild('supplierFormElement', { static: false }) supplierFormElement: ElementRef<any>;
  /**
   * Chield Element with Administrative units. Fetch it's data before submit
  */
  @ViewChild('appChannelTax', { static: false }) appChannelTax;
  /**
   * Chield Element with Administrative units. Fetch it's data before submit
  */
  @ViewChild('appAdministrativeUnits', { static: false }) appAdministrativeUnits;
  /**
   * Unique ID of the service which is beeing edited
  */
  arId: string;
  /**
   * Unique ID of the service supplier which is beeing edited
  */
  serviceSupplierCode: string;
  /**
   * Addministrative units selected for current service
  */
  selectedAdministrativeUnitsList = [];
  /**
   * All available administartive units for the supplier
  */
  availableAdministrativeUnitsList = [];
  /**
   * Initial data for the service supplier
  */
  supplierData;
  /**
   * Selected terms and taxes for the supplier
  */
  termsTaxesData;
  /**
   * All available channels from the supplier
  */
  availableChannels = [];
  /**
   * Is chanell being curently edited. Subbmit button should be disabled while true
  */
  isEditingChannels = false;
  /**
   * Event emmiter for updating of channels that is being sent to all children of this component
  */
  eventUpdateChannel: Subject<object> = new Subject<object>();
  /**
   * Event emmiter for deliting a channel that is being sent to all children of this component
  */
  eventDeleteChannel: Subject<object> = new Subject<object>();
  /**
   * Event emmiter for opening app-collapsable-card component
  */
  eventOpenAllCollapsable: Subject<void> = new Subject<void>();
  /**
   * Event emmiter for changes in supplier payment
  */
  eventPaymentChange: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  /**
   *Form for supplier data
  */
  supplierFormGroup;
  /**
   *Availabe statuses for supplier
  */
  supplierStatuses = metadataResources.supplierStatuses
  /**
   * Has page loaded
  */
  isLoading = true;

  shouldPromptBeforeLeave = true;

  eventOpenEASCollapsable: Subject<void> = new Subject<void>();

  constructor(private route: ActivatedRoute,
    private formIoService: FormIoService,
    private formBuilder: FormBuilder,
    private router: Router,
    private notificationsBannerService: NotificationsBannerService,
    private translateService: TranslateService,
    private dialog: MatDialog) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
        this.arId = params['arId'];
        this.serviceSupplierCode = params['serviceSupplierCode']
        this.getData();
    });
  }
  /**
   * Get initial data for the page
  */
  getData() {
    combineLatest(this.formIoService.getFormIoSubmissionsBySupplierId("common/nom/eas-suppliers", this.arId, this.serviceSupplierCode),
    this.formIoService.getFormIoSubmissionsBySupplierId("common/resource/term-taxes", this.arId, this.serviceSupplierCode),
    this.formIoService.getFormIoResource('common/nom/service-result-channel'),
    this.formIoService.getFormIoSupplier(this.serviceSupplierCode)
    ).subscribe(([selectedData, termsTaxesData, availableChannels , availableAdministrativeUnits  ]) => {
      this.supplierData = selectedData?.[0]?.data
      this.selectedAdministrativeUnitsList = selectedData?.[0]?.data?.administrativeUnitsList ? selectedData[0].data.administrativeUnitsList : []
      this.supplierFormGroup =  this.formBuilder.group({
        status: [this.supplierData.status, [Validators.required]],
        isNotProcessable: [this.supplierData.isNotProcessable !== undefined ? this.supplierData.isNotProcessable : ''],
        serviceDescription: [this.supplierData.serviceDescription],
        hasPayment: [this.supplierData.hasPayment, [Validators.required]],
        hasFixedPayment: [this.supplierData.hasFixedPayment],
        aisClientEPayment: [this.supplierData.aisClientEPayment, [this.taxAmountRequiredWithHasPaymentValidator().bind(this)]],
        aisClientIntegrationKey: ['']
      })

      this.termsTaxesData = termsTaxesData?.[0]?.data;
      (availableChannels as Array<any>).forEach(channel => {
        this.availableChannels.push(channel.data)
      })
      if(availableAdministrativeUnits[0]) {
        availableAdministrativeUnits[0].data?.administrativeUnitsList?.forEach(au => {
          this.availableAdministrativeUnitsList.push(au)
        })
      }
      this.handlePaymentChange()
      this.handleFixPaymentChange()
      this.isLoading = false
    })
  }
  /**
   * Fire emevnt to Administrative Units component when a channel is added or edited in the Channels,Terms and Taxes component
   * @param channel the channel that is being added or edited
  */
  handleUpdateChannel(channel) {
    this.eventUpdateChannel.next(channel)
  }
  /**
   * Fire emevnt to Administrative Units component when a channel is deleted in the Channels,Terms and Taxes component
   * @param channel the channel that is being deleted
  */
  handleDeleteChannel(channel) {
    this.eventDeleteChannel.next(channel)
  }
  /**
   * Handle is editng property on event from Channels,Terms and Taxes component to disable/enable next button
   * @param isEditing are channels being edited
  */
  handleIsEditngChannel(isEditing : boolean) {
    this.isEditingChannels = isEditing
  }
  /**
   * Disable/enable fields whichs requirment depends on if service has payment
  */
  handlePaymentChange(){
    if(this.supplierFormGroup.value.hasPayment === 'no') {    
      this.supplierFormGroup.controls.hasFixedPayment.disable()
      this.supplierFormGroup.controls.hasFixedPayment.setValue('no')
      this.eventPaymentChange.next(false)
    } else {
      this.supplierFormGroup.controls.hasFixedPayment.enable()
    }
    this.supplierFormGroup.controls.aisClientEPayment.updateValueAndValidity()
  }

  handleFixPaymentChange(){
    this.eventPaymentChange.next(!!this.supplierFormGroup.value.hasFixedPayment && this.supplierFormGroup.value.hasFixedPayment !== 'no')
  }
  /**
   * Format data and save it
  */
  submitMetadata(){
    Object.keys(this.supplierFormGroup.controls).forEach(key => {
      this.supplierFormGroup.controls[key].updateValueAndValidity()
      this.supplierFormGroup.controls[key].touched = true
    });

    if (!this.supplierFormGroup.valid) {
      this.supplierFormElement.nativeElement.scrollIntoView()
      this.eventOpenAllCollapsable.next();
      return;
    }
    
    if( this.appChannelTax.channelTaxList.length < 1) {
      this.notificationsBannerService.show({
        message: 'PLEASE_ADD_ATLEAST_ONE_CHANNEL',
        type: NotificationBarType.Error
      });
      return;
    }
    let channelsAreCurrent = false; 
    this.appChannelTax.channelTaxList.forEach(channel => {
      if(moment(moment.now()).isSameOrAfter(channel.validFrom) && (!channel.validTo || moment(channel.validTo).isSameOrAfter(moment.now()))) {
        channelsAreCurrent = true
      }
    })
    
    let allUnitsHaveChannels = true
    if(this.appAdministrativeUnits.easForm.value.hasAdministrativeUnits == 'yes') {
      let hasUnits = false
      this.appAdministrativeUnits.easForm.value.administrativeUnits.forEach(unit => {
        if(unit.selected) {
          hasUnits = true
          let selectedChannels = []
          unit.channelsAndTermsList.forEach(channel => {
            if(channel.selected) {
              selectedChannels.push(channel.channelsAndTerms)
            }
          });
          if(!selectedChannels.length) {
            allUnitsHaveChannels = false
          }
        }
      })
      if(!hasUnits) {
        this.notificationsBannerService.show({
          message: 'PLEASE_ADD_ATLEAST_ONE_AU',
          type: NotificationBarType.Error
        });
        return;
      }
    }

    if(!allUnitsHaveChannels) {
      this.eventOpenEASCollapsable.next()
      this.appAdministrativeUnits.nativeElement.scrollIntoView()
      return;
    }


    let selectedStatus = this.supplierStatuses.find(s => s.value === this.supplierFormGroup.value.status);
    let bodyMessage = []
    if (!channelsAreCurrent) {
      bodyMessage.push('METADATA_REVIEW.NO_CURRENT_CHANNEL')
    }
    if (!this.supplierFormGroup.value.hasFixedPayment || this.supplierFormGroup.value.hasFixedPayment === 'no' ) {
      bodyMessage.push('METADATA_REVIEW.PRICE_WILL_BE_LOST')
    }
    let deleteSigneeModalData = {
      headerTitle: "SAVE_METADATA",
      bodyIcon: channelsAreCurrent ? "" : 'info',
      mainTitle: `${this.translateService.instant('CONFIRM_SERVICE_CONFIGURATION')} ${this.translateService.instant(selectedStatus.transKey)}  ?`,
      bodyMessage : bodyMessage,
      canGoBack: true,
      canGoBackMessage: "GO_BACK",
      canGoForward: true,
      canGoForwardMessage: "SAVE",
    }
    
    let saveMetaDataModalRef = this.dialog.open(AdvancedDialogComponent, { data: deleteSigneeModalData });
    saveMetaDataModalRef.componentInstance.confirmed.subscribe(() => {
      let selectedAdminastritiveUnits = [];
      if(this.appAdministrativeUnits.easForm.value.hasAdministrativeUnits == 'yes') {
        this.appAdministrativeUnits.easForm.value.administrativeUnits.forEach(unit => {
          if(unit.selected) {
            let selectedChannels = []
            unit.channelsAndTermsList.forEach(channel => {
              if(channel.selected) {
                selectedChannels.push(channel.channelsAndTerms)
              }
            });
            if(selectedChannels.length &&  selectedChannels.length < this.appChannelTax.channelTaxList.length) {                
              unit.unit.channelsAndTermsList = selectedChannels
            } else {
              unit.unit.channelsAndTermsList = []
            }
            selectedAdminastritiveUnits.push(unit.unit)
          }
        })
        this.supplierData.hasAdministrativeUnits = 'yes'
      } else {
        this.supplierData.hasAdministrativeUnits = 'no'
      }
      this.supplierData.administrativeUnitsList = selectedAdminastritiveUnits
      if(!this.termsTaxesData) {
        this.termsTaxesData = {
          arId: this.arId,
          supplierEAS: this.serviceSupplierCode,
          uniqueKey: `${this.arId}-${this.serviceSupplierCode}`,
          channelsAndTermsList: []
        }
      }
      this.termsTaxesData.channelsAndTermsList = this.appChannelTax.channelTaxList
      let hasFixedPayment = !!this.supplierFormGroup.value.hasFixedPayment && this.supplierFormGroup.value.hasFixedPayment !== 'no'
      if(!hasFixedPayment) {
        this.termsTaxesData.channelsAndTermsList.forEach(channel => {
          channel.hasPayment = false;
          channel.taxAmount = '';
        })
      }
      let formData  = this.supplierFormGroup.value
      formData.hasFixedPayment = this.supplierFormGroup.controls.hasFixedPayment?.value
      this.supplierData = {... this.supplierData, ... formData}
      let data = {
        commonNomEasSuppliers: this.supplierData,          
        commonResourceTermTaxes:  this.termsTaxesData
      }
      this.formIoService.submitMetaData(data).subscribe(() => {
        localStorage.setItem('showMetaDataSubmissionSuccess', `${this.supplierData.arId} - ${this.supplierData.serviceTitle}`)
        this.shouldPromptBeforeLeave = false
        this.router.navigate(['administration-of-services/services']);
      })

    })
  }
  /**
   * Go back to previous page
  */
  back() {
    this.router.navigate(['administration-of-services/services']);
  }
  /**
   * Validator for fields that are reuired only if service has payment
  */
  taxAmountRequiredWithHasPaymentValidator(): ValidatorFn {  
    return (control: AbstractControl): { [key: string]: any } | null => {
        if(this.supplierFormGroup) {
            if(this.supplierFormGroup.controls.hasPayment.value === 'yes' && !this.supplierFormGroup.controls.aisClientEPayment.value){   
              return {required: true};
            } else {
                return null;
            }
        }
        return null;
    }            
  }

}
