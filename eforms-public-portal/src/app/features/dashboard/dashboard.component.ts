import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { Observable } from 'rxjs';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { DeepLinkService } from 'src/app/core/services/deep-link.service';
import { DAEFServiceResponse } from 'src/app/core/types/daefservice';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from '../../shared/components/notifications-banner/notification-banner.model';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  userData$: Observable<any>;
  isAuthenticated$: Observable<boolean>;
  daefServiceData: DAEFServiceResponse;
  serviceId: string;
  serviceError;
  existUserProfile = true;
  isValidAssuranceLevel: boolean = false;
  iseDeliveryActive = true;
  requiredAssuranceLevel;

  constructor(
    public oidcSecurityService: OidcSecurityService,
    private deepLinkService: DeepLinkService,
    private DAEFService: DAEFService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private notificationsBannerService: NotificationsBannerService,
    public userProfileService: UserProfileService,
    private daefService: DAEFService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params: ParamMap) => {
      if(params.get('id')) {
        const easId = params.get('id');
        this.serviceId = easId;
        this.deepLinkService.setParam('easId', easId);
      } else if( this.deepLinkService.getParam('easId') ){
        this.serviceId = this.deepLinkService.getParam('easId')
      }else if( this.daefService.getServiceId() ){
        this.serviceId = this.daefService.getServiceId()
      } else {
        this.router.navigate(['home'])
      }
      this.daefService.setService(this.serviceId);
      this.deepLinkService.deleteParam("easId");
      this.userData$ = this.oidcSecurityService.userData$;
      this.isAuthenticated$ = this.oidcSecurityService.isAuthenticated$;
      this.DAEFService.getDAEFService(this.serviceId).subscribe(response => {
        this.daefServiceData = response;
        if (this.daefServiceData.existIncompleteCases) {
          this.notificationsBannerService.show({message: "SERVICES.ERRORS.SERVICE_NOT_COMPLETED", type: NotificationBarType.Info, actionText: "SERVICES.MY_SERVICES", url: "my-services", openInNewTab: false })
        }
        if (this.daefServiceData.eDeliveryStatus === "PROFILE_NOT_FOUND") {
          this.existUserProfile = false;
          this.notificationsBannerService.show({message: "SERVICES.ERRORS.USER_PROFILE_NOT_FOUND", type: NotificationBarType.Error, actionText: "HERE", url: `https://${environment.edeliveryURL}`, openInNewTab: true })
        }

        this.requiredAssuranceLevel = this.daefServiceData.service.data.requiredSecurityLevel;
        if (this.requiredAssuranceLevel != null && this.requiredAssuranceLevel != '') {
          this.isValidAssuranceLevel = this.userProfileService.isValidAssuranceLevel(this.requiredAssuranceLevel);
          if (!this.isValidAssuranceLevel) {
            this.daefServiceData = null;
            this.serviceError = "ASSURANCE_LEVEL_MISSMATCH";
          }
        }
        if (this.daefServiceData.eDeliveryStatus === "NOT_AUTHORIZED") {
          this.existUserProfile = false;
          this.notificationsBannerService.show({message: "SERVICES.ERRORS.USER_PROFILE_NOT_AUTHORIZED", type: NotificationBarType.Error})
        }
        if (this.daefServiceData.eDeliveryStatus === "SERVICE_NOT_AVAILABLE") {
          this.iseDeliveryActive = false;
          this.notificationsBannerService.show({message: "SERVICES.ERRORS.EDELIVERY_NOT_AVAILABLE", type: NotificationBarType.Error })
        }
      }, (error) => {
        this.showServiceError(error);
      })
    })
  }

  showServiceError(error: any) {
    if (error.status === 400 && error.message === "SERVICE_NOT_ACTIVE") {
      return this.serviceError = "SERVICES.ERRORS." + error.message;
    }
    if (error.status === 404 && error.message === "RESOURCE_NOT_FOUND") {
      return this.serviceError = "SERVICES.ERRORS." + error.message;
    }
    return this.router.navigate(['home']);
  }

  redirectToHome() {
    this.router.navigate(['home']);
  }
}
