import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, Subscription } from 'rxjs';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { ConnectionService } from 'src/app/core/services/connection.service';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { CamundaProcess } from 'src/app/core/types/camunda-process';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-current-task',
  templateUrl: './current-task.component.html',
  styleUrls: ['./current-task.component.scss']
})
export class CurrentTaskComponent implements OnInit {
  /**
   * Wrapper Subscription for services. To be destroyed on ngDestroy so component properly unsubscribes from all services
   * @private
   */
  private wrapperSubscription = new Subscription();

  nextSubmittedSubject: Subject<void> = new Subject<void>();
  prevSubmittedSubject: Subject<void> = new Subject<void>();
  isOnlineSubject: Subject<boolean> = new Subject<boolean>();

  formSrc;
  disableNext = false;

  processId;
  taskError;
  pageTitle: string;

  projectId: string;
  ornNumber: string;
  process: Array<CamundaProcess> = [];
  formId: string | null = null;

  currentLaneIndex = 0;
  currentUserTaskIndex = 0;

  showCustomContent: boolean = false;
  showSignContent: boolean = false;

  constructor(private router: Router,
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private camundaProcessService: CamundaProcessService,
    private formIoService: FormIoService,
    private notificationsBannerService: NotificationsBannerService,
    private connectionService: ConnectionService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe((paramsMap) => {
      this.processId = paramsMap.get('id');
    });

    this.checkForInternetConnection();

    this.projectId = environment.formioBaseProject;
    this.getProcess();
  }

  getProcess() {
    this.wrapperSubscription.add(
      this.camundaProcessService['getCurrentTask'](this.processId)
        .subscribe(processResponse => {
          this.process = processResponse.lanes.sort((a, b) => a.properties.order - b.properties.order);
          this.pageTitle = this.process[0].name;
          this.formId = this.process[this.currentLaneIndex].userTasks[this.currentUserTaskIndex].formKey;
          this.ornNumber = processResponse.businessKey;


          switch (true) {
            case this.formId.includes('common/component/attachment/sign'):
              this.showCustomContent = true
              this.showSignContent = true
              break;

            default:
              this.router.navigate([`my-services/process/${this.processId}`])
          }

        }, (error) => {
          this.taskError = (error.status == 404) ? "FORBIDDEN" : "ERRORS.CURRENT_TASK_FINALIZED";
        })
    );
  }

  back() {
    this.router.navigate(['home']);
  }

  callNextOnForm() {
    if (!this.disableNext && (this.formSrc || this.showCustomContent)) {
      this.disableNext = true;
      this.nextSubmittedSubject.next();
    }
  }

  public next() {
    this.notificationsBannerService.show({ message: "CURRENT_TASK_SUCCESS", type: NotificationBarType.Success });
    this.router.navigate(['home']);
  }

  public handleFormError() {
    this.disableNext = false;
  }

  ngOnDestroy(): void {
    this.wrapperSubscription.unsubscribe();
  }

  checkForInternetConnection() {
    this.wrapperSubscription.add(
      this.connectionService.checkInternetConnection().subscribe((isOnline: boolean) => {
        this.isOnlineSubject.next(isOnline);
        if (isOnline) {
          this.notificationsBannerService.hideAll();
          this.notificationsBannerService.show({
            message: 'MESSAGE.INTERNET_CONNECTION_RESTORED',
            type: NotificationBarType.Success
          });
        } else {
          this.notificationsBannerService.hideAll();
          this.notificationsBannerService.show({
            message: 'ERRORS.NO_INTERNET_CONNECTION',
            type: NotificationBarType.Error
          });
        }
      })
    );
  }
}
