import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from "@angular/core";
import {
  AbstractControl,
  FormBuilder,
  ValidatorFn,
  Validators,
} from "@angular/forms";
import { Observable, Subscription } from "rxjs";
import { FormIoService } from "src/app/core/services/form-io.service";
import { NotificationsBannerService } from "src/app/core/services/notifications-banner.service";
import Utils from "src/app/shared/utilities/utils";

@Component({
  selector: "app-admissibility-of-service",
  templateUrl: "./admissibility-of-service.component.html",
  styleUrls: ["./admissibility-of-service.component.scss"],
})
export class AdmissibilityOfServiceComponent implements OnInit, OnDestroy {
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

  admissibilityForm;
  channel: string;

  constructor(
    private formBuilder: FormBuilder,
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
      Object.keys(this.admissibilityForm.controls).forEach((key) => {
        this.admissibilityForm.controls[key].touched = true;
        this.admissibilityForm.controls[key].updateValueAndValidity();
      });
      if (this.admissibilityForm.valid) {
        let data = { variables: {} };
        data["variables"][
          `submissionData_${Utils.getFormDataSubmissionKey(this.formId)}`
        ] = {
          value: {
            data: this.admissibilityForm.value,
          },
        };
        this.formIoService.postForm(this.taskId, data).subscribe(
          (result) => {
            this.notificationsBannerService.hideAll();
            if (
              this.channel !== "0006-000080" &&
              !this.admissibilityForm.value.admissibilityOfService
            ) {
              localStorage.setItem("showSubmissionSuccess", "true");
              this.redirectEvent.emit({
                redirectTo: "admin-services/in-progress",
              });
            } else {
              this.nextSuccessEvent.emit(null);
            }
          },
          (error) => {
            this.nextErrorEvent.emit(false);
          }
        );
      } else {
        this.nextErrorEvent.emit(false);
      }
    });
    this.channel = (Utils.parseQueryParamsToObject(
      this.formId
    ) as any)?.channel;
    this.admissibilityForm = this.formBuilder.group({
      admissibilityOfService: ["", [Validators.required]],
      additionalTax: ["", [this.additionalTaxValidator().bind(this)]],
      amount: [
        "",
        [
          this.amauntRequiredWithAdditionalTaxValidator().bind(this),
          Validators.min(0.01),
        ],
      ],
    });
  }

  ngOnDestroy() {
    if (this.nextStepSubscription) {
      this.nextStepSubscription.unsubscribe();
    }
  }

  handleAdmissibilityOfServiceChange() {
    if (!this.admissibilityForm.value.admissibilityOfService) {
      this.admissibilityForm.controls.additionalTax.setValue("", {
        emitEvent: false,
      });
      this.admissibilityForm.controls.amount.setValue("", { emitEvent: false });
    }
    this.admissibilityForm.controls.amount.touched = false;
    this.admissibilityForm.controls.additionalTax.touched = false;

    this.onForceIsFinalFlagLabel.emit(
      this.channel !== "0006-000080" &&
        !this.admissibilityForm.value.admissibilityOfService
    );
  }

  handleAdditionalTaxChange() {
    this.admissibilityForm.controls.additionalTax.updateValueAndValidity();
    if (!this.admissibilityForm.value.handleAdditionalTaxChange) {
      this.admissibilityForm.controls.amount.setValue("");
    }
    this.admissibilityForm.controls.amount.touched = false;
  }

  handleAmountChange() {
    this.admissibilityForm.controls.amount.updateValueAndValidity();
  }

  amauntRequiredWithAdditionalTaxValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      if (this.admissibilityForm) {
        if (!this.admissibilityForm.value.additionalTax) {
          return null;
        } else if (
          !this.admissibilityForm.value.amount &&
          this.admissibilityForm.value.amount !== 0
        ) {
          return { required: true };
        } else {
          return null;
        }
      }
      return null;
    };
  }

  additionalTaxValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      if (this.admissibilityForm) {
        if (!this.admissibilityForm.value.admissibilityOfService) {
          return null;
        } else if (this.admissibilityForm.value.additionalTax === "") {
          return { required: true };
        } else {
          return null;
        }
      }
      return null;
    };
  }
}
