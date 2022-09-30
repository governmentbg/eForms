import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import Utils from 'src/app/shared/utilities/utils';

@Component({
  selector: 'app-accept-or-deny-service',
  templateUrl: './accept-or-deny-service.component.html',
  styleUrls: ['./accept-or-deny-service.component.scss']
})
export class AcceptOrDenyServiceComponent implements OnInit, OnDestroy {
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

  acceptOrDenyForm;
  channel:string;
  constructor(private formBuilder: FormBuilder,
    private formIoService: FormIoService,
    private notificationsBannerService: NotificationsBannerService) {
      this.nextSuccessEvent = new EventEmitter<any>();
      this.nextErrorEvent = new EventEmitter<any>();
      this.shouldShowNextButton = new EventEmitter<boolean>();
      this.redirectEvent = new EventEmitter<any>();      
      this.onForceIsFinalFlagLabel = new EventEmitter<boolean>();
     }

  ngOnInit(): void {
    this.nextStepSubscription = this.nextStep.subscribe(async () => {
      Object.keys(this.acceptOrDenyForm.controls).forEach(key => {
        this.acceptOrDenyForm.controls[key].touched = true
        this.acceptOrDenyForm.controls[key].updateValueAndValidity()
      });
      if(this.acceptOrDenyForm.valid) {
        let data = { variables: {} };
        data['variables'][`submissionData_${Utils.getFormDataSubmissionKey(this.formId)}`] = {
          "value": {
            "data": this.acceptOrDenyForm.value
          }
        };
        this.formIoService
        .postForm(this.taskId, data)
        .subscribe(
          (result) => {
            this.notificationsBannerService.hideAll();
            if(this.channel && this.channel !== '0006-000080') {
              localStorage.setItem('showSubmissionSuccess', 'true');
              this.redirectEvent.emit({redirectTo: 'admin-services/in-progress'})
            } else {
              this.nextSuccessEvent.emit(null);
            }

          },
          (error) => {
            this.nextErrorEvent.emit(false);
          }
        )
      } else{
        this.nextErrorEvent.emit(false);
      }
  
    });
    this.channel = (Utils.parseQueryParamsToObject(this.formId) as any)?.channel;
    this.acceptOrDenyForm =  this.formBuilder.group({
      serviceResult: ['', [Validators.required]]
    }) 
  }
  ngOnDestroy(){
    if (this.nextStepSubscription) {
      this.nextStepSubscription.unsubscribe();
    }
  }

}
