import { Component, Input, OnInit } from '@angular/core';
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
          if(this.selectedSupplier.value.data.hasAdministrativeUnits !== 'no'){
            this.selectedSupplier.value.easAdministrativeUnitsList = this.suppliers[0].data.administrativeUnitsList;
            if (this.selectedSupplier.value.easAdministrativeUnitsList.length === 1) {
              this.selectedUnit.value = this.selectedSupplier.value.easAdministrativeUnitsList[0];
            } else {
              this.onSelectSupplier()
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
          }))
      }
    } else {
      this.suppliersInitialLenght = 0;
      this.notificationsBannerService.show({message: "SERVICES.NO_SUPPLIERS", type: NotificationBarType.Info})
    }
    this.selectedUnit.valueChanges.subscribe(() => {
        this.notificationsBannerService.hideAllErrors()

      })
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
        this.isUnitValid) {
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
    if (this.selectedSupplier.valid || this.suppliers.length === 1) {
      return true;
    }
    return false;
  }

  get isUnitValid() {
    if (this.selectedSupplier.value.easAdministrativeUnitsList?.length > 1 && !this.selectedUnit.valid) {
      return false;
    }
    return true;
  }

  private _filterUnits(value: any) {
    if(!value){
      if(this.selectedSupplier.value.easAdministrativeUnitsList.length > 10) {
        return this.selectedSupplier.value.easAdministrativeUnitsList.slice(0,10)
      }     
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
    if(administrativeUnitsList.length>10){
      administrativeUnitsList = administrativeUnitsList.slice(0,10)
    }
    return administrativeUnitsList
  }
  
  displayFnSupplier(value: any) {
    return value ? value.data.serviceSupplierTitle : undefined;
  }

  displayFnUnit(value: any) {
    return value ? value.administrationUnit : undefined;
  }

  clearSelectedSupplier() {
    this.selectedSupplier.setValue(''); 
  }

  clearSelectedUnit() {
    this.selectedUnit.setValue(''); 
  }

  onSelectSupplier(){
    this.clearSelectedUnit(); 
    return this.unitsFiltered = this.selectedUnit.valueChanges.pipe(
      startWith(""),
      map(value => 
        {
          return this._filterUnits(value)})
    )
  }

  goToLink(url: string){
    if (!(url.includes('https://') || url.includes('http://'))) {
      url = 'https://' + url
    }
    window.open(url, "_blank");
  }

  get disableNextButton() {
    return (this.existUserProfile && this.iseDeliveryActive && this.suppliersInitialLenght > 0 && this.isUnitValid && this.isSupplierValid )
  }
}
