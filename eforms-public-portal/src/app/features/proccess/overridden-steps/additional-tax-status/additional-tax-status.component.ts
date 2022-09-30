import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { result } from 'lodash';
import { Observable, Subscription } from 'rxjs';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { PaymentService } from 'src/app/core/services/payment.service';
import Utils from 'src/app/shared/utilities/utils';

@Component({
  selector: 'app-additional-tax-status',
  templateUrl: './additional-tax-status.component.html',
  styleUrls: ['./additional-tax-status.component.scss']
})
export class AdditionalTaxStatusComponent implements OnInit, OnDestroy {
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
  @Output() onForceIsFinalFlagLabel = new EventEmitter<boolean>();

  
  nextStepSubscription: Subscription;
  paymentStatusForm
  channel: string;
  epaymentStatus: {
    value: string,
    color: string
  };
  paymentId: string;
  availableStatuses = [
    {
      label: "Има задължение",
      value: "payment"
    },
    {
      label: "Платена",
      value: "payed"
    },
    {
      label: "Неплатена",
      value: "notPaid"
    }
  ]
  constructor(
    private formBuilder: FormBuilder,
    private paymentService: PaymentService,
    private formIoService: FormIoService,
    private notificationsBannerService: NotificationsBannerService
  ) {
    this.nextSuccessEvent = new EventEmitter<any>();
    this.nextErrorEvent = new EventEmitter<any>();
    this.shouldShowNextButton = new EventEmitter<boolean>();
    this.redirectEvent = new EventEmitter<any>();
    this.onForceIsFinalFlagLabel = new EventEmitter<boolean>();
   }

  ngOnInit(): void {
    this.nextStepSubscription = this.nextStep.subscribe(async () => {
      this.paymentStatusForm.controls.statusCode.updateValueAndValidity()
      if(this.paymentStatusForm.valid) {
        let data = { variables: {} };
        data['variables'][`submissionData_${Utils.getFormDataSubmissionKey(this.formId)}`] = {
          "value": {
            "data": {
              statusAdditionalTax : this.paymentStatusForm.value.statusCode
            }
          }
        };
        this.formIoService
        .postForm(this.taskId, data)
        .subscribe(
          (result) => {
            this.notificationsBannerService.hideAll();
            if(this.channel !== '0006-000080' && this.paymentStatusForm.value.statusCode === 'notPaid') {
              localStorage.setItem('showSubmissionSuccess', 'true');
              this.redirectEvent.emit({redirectTo: 'admin-services/in-progress'})
            } else {
              this.nextSuccessEvent.emit(true);
            }
          },
          (error) => {
            this.nextErrorEvent.emit(true);
          }
        )
      } else {
        this.nextSuccessEvent.emit(true);
      }  
    });
    this.paymentStatusForm =  this.formBuilder.group({
      statusCode: [this.availableStatuses[0].value, [Validators.required]],
    })
    this.paymentId = (Utils.parseQueryParamsToObject(this.formId) as any).paymentId;
    this.channel = (Utils.parseQueryParamsToObject(this.formId) as any)?.channel;
    this.fetchPaymentStatus()
  }

  ngOnDestroy(){
    if (this.nextStepSubscription) {
      this.nextStepSubscription.unsubscribe();
    }
  }

  fetchPaymentStatus() {
    this.paymentService.getAdditionalTaxStatus(this.paymentId).subscribe(result => {
      switch(result.status) {
        case 'pending' :         
        this.epaymentStatus = {
          value: 'Очаква плащане',
          color: 'yellow'
        }; break;
        case 'inProcess' :         
        this.epaymentStatus = {
          value: 'В процес на обработка',
          color: 'yellow'
        }; break;
        case 'authorized' :         
        this.epaymentStatus = {
          value: 'Получена е авторизация от виртуалния ПОС терминал',
          color: 'green'
        }; break;
        case 'ordered' :         
        this.epaymentStatus = {
          value: 'Плащането е наредено',
          color: 'green'
        }; break;
        case 'paid' :         
        this.epaymentStatus = {
          value: 'Плащането е получено по сметката на доставчика',
          color: 'green'
        }; break;
        case 'expired' :         
        this.epaymentStatus = {
          value: 'Заявката за плащане е изтеклаа',
          color: 'gray'
        }; break;
        case 'canceled' :         
        this.epaymentStatus = {
          value: 'Заявката за плащане е отказана от потребителя',
          color: 'gray'
        }; break;
        case 'suspended' :         
        this.epaymentStatus = {
          value: 'Заявката за плащане е отказана от АИС',
          color: 'gray'
        }; break;
        default:         
        this.epaymentStatus = {
          value: 'Неплатена',
          color: 'gray'
        }; break;
      }     
    })
  }

  handleStatusCodeeChange() {
    this.onForceIsFinalFlagLabel.emit(this.channel !== '0006-000080' && this.paymentStatusForm.statusCode.value === 'notPaid');
  }

}
