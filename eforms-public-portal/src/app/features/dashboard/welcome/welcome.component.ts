import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { DeepLinkService } from 'src/app/core/services/deep-link.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { UserProfile } from 'src/app/core/types/user-profile';
import { environment } from 'src/environments/environment';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import { Observable } from 'rxjs';
import { DialogComponent } from 'src/app/shared/components/dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { distinctUntilChanged, map, startWith, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {
  @Input() easId: string;
  userData: any;
  @Input() keycloakId: string;
  @Input() service;
  @Input() suppliers;
  @Input() serviceCategory;
  @Input() existUserProfile;
  @Input() iseDeliveryActive;

  selectedSupplier: any;
  selectedUnit: any;
  userProfile: UserProfile;
  suppliersFiltered: Observable < any > ;
  unitsFiltered: Observable < any > ;
  suppliersInitialLenght;
  chosenUserProfile: any;
  hasDeliveryCannel = false;

  constructor(
    public oidcSecurityService: OidcSecurityService,
    private camundaProcessService: CamundaProcessService,
    private deepLinkService: DeepLinkService,
    public userProfileService: UserProfileService,
    public router: Router,
    private daefService: DAEFService,
    private notificationsBannerService: NotificationsBannerService,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.userProfileService
      .subscribe((userProfile) => {
        this.userProfile = userProfile;

        // Build the Chosen User Profile Object starting from the current user
        this.chosenUserProfile = {
          personIdentifier: userProfile?.personIdentifier,
          personName: userProfile?.personName
        }
        // Add info in case of a selected profile
        if(!!this.userProfileService.selectedProfile){
          this.chosenUserProfile.title = this.userProfileService.selectedProfile.title;
        }
      });


    this.selectedSupplier = new FormControl('', Validators.required);
    this.selectedUnit = new FormControl('', Validators.required);
    if (this.suppliers.length > 0) {
      this.suppliersInitialLenght = this.suppliers.length;
      if (this.suppliers.length === 1) {
        this.daefService.getFullDAEFService(this.easId, this.suppliers[0].supplierEAS).subscribe(response => {
          this.suppliers[0] = response;
          this.selectedSupplier.value = this.suppliers[0];

          if (!this.selectedSupplier.value.data.channelsAndTermsList?.length && !this.selectedSupplier.value.data.administrativeUnitsList?.length) {
            this.notificationsBannerService.show({message: "ERRORS.VALIDATE_CHANNEL", type: NotificationBarType.Error });
          } else {
            this.hasDeliveryCannel = true;
          }

          if(this.selectedSupplier.value.data.hasAdministrativeUnits !== 'no'){
            this.selectedSupplier.value.easAdministrativeUnitsList = this.suppliers[0].data.administrativeUnitsList;
            if (this.selectedSupplier.value.easAdministrativeUnitsList.length === 1) {
              this.selectedUnit.value = this.selectedSupplier.value.easAdministrativeUnitsList[0];
            } else {
              this.onSelectSupplier(false)
            }
          }
        });
      } else {
        this.suppliersFiltered = this.selectedSupplier.valueChanges.pipe(
          startWith(""),
          distinctUntilChanged(),
          switchMap(val => {
            if (typeof val === "string") {
              return this.daefService.getFilteredDAEFService(this.easId, val).pipe(
                map(response => {
                return this.suppliers = response
              }))
            } else {
              return this.daefService.getFullDAEFService(this.easId, this.selectedSupplier.value.data.supplierEAS).pipe(
                map(response => {
                this.selectedSupplier.value = response;

                if (!this.selectedSupplier.value.data.channelsAndTermsList?.length && !this.selectedSupplier.value.data.administrativeUnitsList?.length) {
                  this.notificationsBannerService.show({message: "ERRORS.VALIDATE_CHANNEL", type: NotificationBarType.Error });
                  return;
                } else if(this.selectedSupplier.value.data.administrativeUnitsList?.length === 1 && !this.selectedSupplier.value.data.administrativeUnitsList[0].useSupplierChannelsAndTermsList && !this.selectedSupplier.value.data.administrativeUnitsList[0].channelsAndTermsList.length) {
                  this.notificationsBannerService.show({message: "ERRORS.VALIDATE_CHANNEL", type: NotificationBarType.Error });
                  return;
                } else {
                  this.hasDeliveryCannel = true;
                }
                
                if(this.selectedSupplier.value.data.hasAdministrativeUnits !== 'no'){
                  this.selectedSupplier.value.easAdministrativeUnitsList = this.selectedSupplier.value.data.administrativeUnitsList;
                  if (this.selectedSupplier.value.easAdministrativeUnitsList.length === 1) {
                    this.selectedUnit.value = this.selectedSupplier.value.easAdministrativeUnitsList[0];
                    return this.suppliers
                  } else {
                    this.onSelectSupplier();
                    this.notificationsBannerService.hideAllErrors()
                    return this.suppliers
                  }
                }
              }))
            }
          })
          )

      }
    } else {
      this.suppliersInitialLenght = 0;
      this.notificationsBannerService.show({message: "SERVICES.NO_SUPPLIERS", type: NotificationBarType.Info})
    }
    this.selectedUnit.valueChanges.subscribe(() => {
        this.notificationsBannerService.hideAllErrors()
        if (this.selectedUnit.value.channelsAndTermsList) {
          if (this.selectedUnit.value.channelsAndTermsList?.length) {
            this.hasDeliveryCannel = true;
          } else if(this.selectedUnit.value.useSupplierChannelsAndTermsList && this.selectedSupplier.value.data.channelsAndTermsList?.length) {
            this.hasDeliveryCannel = true;
          } else {
            this.hasDeliveryCannel = false;
            setTimeout(() => { // The timeout is used to prevent scrolling down to the units list
              this.notificationsBannerService.show({message: "ERRORS.VALIDATE_CHANNEL", type: NotificationBarType.Error });
            }, 100)
          }
        }
      })

      if(this.suppliersFiltered){

        this.suppliersFiltered.subscribe((val) => {
          if(!val.length){
            this.selectedSupplier.setErrors({"no_results_from_service": "no_results_from_service"});
            this.selectedSupplier.touched = true;
          } 
        })

      }
  }

  rejectService(): void {
    this.deepLinkService.deleteParam("easId");
    this.deepLinkService.deleteParam("projectId");
    this.router.navigate(['home']);
  }

  showDialog() {
    const dialogRef = this.dialog.open(DialogComponent, { data: { title: 'IMPORTANT', body: 'CANCEL_SERVICE', canProceed: true } });
    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.rejectService();
      }
    });
  }

  acceptService(): void {
    if (this.isSupplierValid &&
        this.isUnitValid && 
        this.hasDeliveryCannel) {
      if (this.selectedSupplier.value.easAdministrativeUnitsList?.length === 1) {
        this.selectedUnit.value = this.selectedSupplier.value.easAdministrativeUnitsList[0];
      }
      this.oidcSecurityService.userData$
        .subscribe(userData => {
          if (userData) {
            let easVariables = {
              service: JSON.parse(JSON.stringify(this.service)),
              serviceSupplier: JSON.parse(JSON.stringify(this.selectedSupplier.value)),
              userProfile: this.userProfile,
              formioBaseProject: environment.formioBaseProject,
              serviceSupplierAdministrativeUnit: JSON.parse(JSON.stringify(this.selectedUnit.value)),
              dataProcessingAgreement: false
            }
            delete easVariables.service.data.serviceDescription
            let variables = {};
            variables['context'] = {
              value: easVariables
            }
            this.daefService.setService(this.service.data.arId);
            this.camundaProcessService.startProcess(this.service.data.arId, { variables });
          }
        })
    } else if (!this.isSupplierValid) {
      this.notificationsBannerService.show({message: "SERVICES.ERRORS.SELECT_SUPPLIER", type: NotificationBarType.Error })
      this.selectedSupplier.touched = true;
    } else if (!this.isUnitValid) {
      this.notificationsBannerService.show({message: "SERVICES.ERRORS.SELECT_UNIT", type: NotificationBarType.Error })
      this.selectedUnit.touched = true;
    }
  }

  get isSupplierValid() {
    if ((this.selectedSupplier.valid && this.selectedSupplier.value?.data) || this.suppliersInitialLenght === 1) {
      return true;
    }
    return false;
  }

  get isUnitValid() {
    if (this.selectedSupplier.value.easAdministrativeUnitsList?.length > 1 && (!this.selectedUnit.valid || !this.selectedUnit.value?.administrationUnit)) {
      return false;
    }
    return true;
  }

  private _filterUnits(value: any) {
    if(!value){ 
      return this.selectedSupplier.value.easAdministrativeUnitsList
    }
    let filterValue = '';
    if (typeof value === "string") {
      filterValue = value.toLowerCase();
    } else {
      filterValue = value.administrationUnit.toLowerCase();
    }
    let administrativeUnitsList = this.selectedSupplier.value.easAdministrativeUnitsList.filter(
      unit => unit.administrationUnit.toLowerCase().indexOf(filterValue) > -1
    )
    return administrativeUnitsList
  }
  
  displayFnSupplier(value: any) {
    return value ? value.data.serviceSupplierTitle : undefined;
  }

  displayFnUnit(value: any) {
    return value ? value.administrationUnit : undefined;
  }

  clearSelectedSupplier(event: Event) {
    event.stopImmediatePropagation();
    this.selectedSupplier.setValue('');
  }

  clearSelectedUnit(emitEvent: boolean) {
    this.selectedUnit.setValue('', { emitEvent: emitEvent }); 
  }

  clearSelectedUnitOnClose(event: Event) {
    event.stopImmediatePropagation();
    this.selectedUnit.setValue(''); 
  }

  onSelectSupplier(emitEvent: boolean = true){
    this.clearSelectedUnit(emitEvent); 
    
    this.unitsFiltered = this.selectedUnit.valueChanges.pipe(
      startWith(""),
      map(value => 
        {
          return this._filterUnits(value)})
    )

    this.unitsFiltered.subscribe((val) => {
      if(!val.length){
        this.selectedUnit.setErrors({"no_results_from_service": "no_results_from_service"});
        this.selectedUnit.touched = true;
      } 
    })

    return this.unitsFiltered
  }

  goToLink(url: string){
    if (!(url.includes('https://') || url.includes('http://'))) {
      url = 'https://' + url
    }
    window.open(url, "_blank");
  }

  get disableNextButton() {
    return (this.existUserProfile && this.iseDeliveryActive && this.suppliersInitialLenght > 0 && this.isUnitValid && this.isSupplierValid && this.hasDeliveryCannel)
  }

}
