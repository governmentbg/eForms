import { animate, state, style, transition, trigger } from '@angular/animations';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { PaymentService } from 'src/app/core/services/payment.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { PaymentSubmission } from 'src/app/core/types/payment-submission';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import Utils from 'src/app/shared/utilities/utils';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-payment-method-selection',
  templateUrl: './payment-method-selection.component.html',
  styleUrls: ['./payment-method-selection.component.scss'],
  animations: [
    trigger('expandPaymentMethod', [
      state('active', style({
        height: '*'
      })),
      state('inactive', style({
        height: '71px'
      })),
      transition('active <=> inactive', [
        animate('350ms ease-in-out')
      ])
    ])
  ]
})
export class PaymentMethodSelectionComponent implements OnInit, OnDestroy {
  @Input() ornNumber: string;
  @Input() formId: string;
  @Input() taskId: string;
  @Input() nextStep: Observable<any>;
  @Input() isAdminProcess: any;
  @Input() processId: any;
  @Input() process: any;
  @Input() currentLaneIndex: any;
  @Input() currentUserTaskIndex: any;
  @Input() isOnline = true;

  @Output() nextSuccessEvent: EventEmitter<any>;
  @Output() nextErrorEvent: EventEmitter<any>;
  @Output() shouldShowNextButton: EventEmitter<boolean>;
  @Output() redirectEvent: EventEmitter<any>;

  selectedMethods: string[];
  payEgovInfoPage: string;

  queryParamsObj: any;
  showCopyError: boolean;
  nextStepSubscription: Subscription;
  showCopySuccess: boolean;
  preparedDataForSubmission: PaymentSubmission;
  payEgovAccessCodeLink: string;
  showRequiredPaymentMessage: boolean;

  constructor(
    private userProfileService: UserProfileService,
    private formIoService: FormIoService,
    private notificationsBannerService: NotificationsBannerService,
    private paymentService: PaymentService,
    private camundaProcessService: CamundaProcessService,
    private router: Router
  ) {
    this.payEgovInfoPage = `https://${environment.payEgovURL}/Home/Help`;
    this.nextSuccessEvent = new EventEmitter<any>();
    this.nextErrorEvent = new EventEmitter<any>();
    this.shouldShowNextButton = new EventEmitter<boolean>();
    this.redirectEvent = new EventEmitter<any>();
    this.selectedMethods = [];
    this.showRequiredPaymentMessage = true;
  }

  ngOnInit(): void {
    this.shouldShowNextButton.emit(true);
    this.queryParamsObj = Utils.parseQueryParamsToObject(this.formId);
    
    this.paymentService.getAdditionalTaxStatus(this.queryParamsObj.ePaymentId).subscribe((response) => {
      if (response.status === 'paid') {
        this.showRequiredPaymentMessage = false;

        this.notificationsBannerService.show({
          type: NotificationBarType.Success,
          message: 'SUCCESSFUL_PAY'
        });
      }
    });

    this.payEgovAccessCodeLink = `https://${environment.payEgovURL}/Home/AccessByCode?code=${this.queryParamsObj.ePaymentAccessCode}`

    if (this.queryParamsObj.deadlineTerm === '1') {
      this.queryParamsObj.deadlineUnit = this.queryParamsObj.deadlineUnit
        .substr(0, this.queryParamsObj.deadlineUnit.length - 1);
    }

    if (this.queryParamsObj.deadlineTerm === '0') {
      this.queryParamsObj.deadlineTerm = 'PAYMENT.NO_DATA';
    }

    this.queryParamsObj.deadlineUnitLabel = this.queryParamsObj.deadlineUnit.toUpperCase();

    this.nextStepSubscription = this.nextStep.subscribe(async () => {
      if (!this.preparedDataForSubmission) {
        await this.prepareDataForSubmission();
      }

      let data = { variables: {} };
      data['variables'][`submissionData_${Utils.getFormDataSubmissionKey(this.formId)}`] = {
        "value": {
          "data": this.preparedDataForSubmission
        }
      };

      try {
        let mapMethod = this.isAdminProcess ? 'getProcessForAdmin' : 'getProcess';
        const processResponse = await this.camundaProcessService[mapMethod](this.processId).toPromise();
        
        // Mutate the process so the property isFinal is up to date before the complete
        let paymentLane = processResponse.lanes.find(lane => lane.userTasks.length && lane.userTasks[this.currentUserTaskIndex]?.formKey.includes('common/component/payment-request-code'));
        this.process[this.currentLaneIndex].userTasks[this.currentUserTaskIndex].properties = paymentLane.userTasks[this.currentUserTaskIndex].properties;
      } catch (error) {
        this.notificationsBannerService.hideAll();
        this.redirectEvent.emit();

        throw error;
      }

      this.formIoService
        .postForm(this.taskId, data)
        .subscribe(
          (result) => {
            localStorage.removeItem('boricaForm');
            this.notificationsBannerService.hideAll();
            this.nextSuccessEvent.emit(null);
          },
          (error) => {
            this.nextErrorEvent.emit(true);
          }
        )
    });
  }

  ngOnDestroy(): void {
    if (this.nextStepSubscription) {
      this.nextStepSubscription.unsubscribe();
    }
  }

  handlePaymentMethodChange(value: string) {
    if (this.selectedMethods.includes(value)) {
      let methodIndex = this.selectedMethods.indexOf(value);
      this.selectedMethods.splice(methodIndex, 1);
    } else {
      this.selectedMethods.push(value);
    }
  }

  handleCardPaymentClick() {
    this.notificationsBannerService.hideAll();

    let data = {
      paymentRequestIdentifier: this.queryParamsObj.ePaymentId,
      redirectUrl: `${window.location.origin}${this.router.url}`
    }
    
    this.paymentService.getBoricaData(data).subscribe((response: any) => {
      this.paymentService.getBoricaPaymentForm(response).subscribe((response: any) => {
        localStorage.setItem('boricaForm', response);
        window.open('/borica', '_blank');

      }, (error: HttpErrorResponse) => {
        if (error.statusText.includes('InProcess')) {
          if (!localStorage.getItem('boricaForm')) {
            this.notificationsBannerService.show({ message: "ERRORS.VPOS_ERROR", type: NotificationBarType.Error });
            return;
          }

          window.open('/borica', '_blank');
        } else if (error.statusText.includes('Paid')) {
          localStorage.removeItem('boricaForm');
          this.notificationsBannerService.show({ message: "SUCCESSFUL_PAY", type: NotificationBarType.Success });
        } else {
          this.notificationsBannerService.show({ message: "ERRORS.500", type: NotificationBarType.Error });
        }
      });
    });
  }

  async handlePaymentCodeCopy() {
    this.showCopyError = false;
    this.showCopySuccess = false;

    if (!navigator.clipboard) {
      this.showCopyError = true;
      return;
    }

    await navigator.clipboard.writeText(this.queryParamsObj.ePaymentAccessCode);
    this.showCopySuccess = true;
  }

  handleEPaymentClick() {
    window.open(environment.ePaymentLoginUrl);
  }

  handlePayWithCodeClick() {
    window.open(this.payEgovAccessCodeLink);
  }

  private async prepareDataForSubmission() {
    this.preparedDataForSubmission = {
      businessKey: this.ornNumber,
      requestor: this.userProfileService.currentUser.personIdentifier,
      hasFormSubmition: false,
      dontOverrideUserData: true
    };
  }
}
