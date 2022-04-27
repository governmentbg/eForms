import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { environment } from 'src/environments/environment';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { Subject } from 'rxjs';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { CamundaProcess } from 'src/app/core/types/camunda-process';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';

@Component({
  selector: 'app-current-task',
  templateUrl: './current-task.component.html',
  styleUrls: ['./current-task.component.scss']
})
export class CurrentTaskComponent implements OnInit {
  nextSubmittedSubject: Subject<void> = new Subject<void>();
  prevSubmittedSubject: Subject<void> = new Subject<void>();

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

  constructor(private router: Router,
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private camundaProcessService: CamundaProcessService,
    private formIoService: FormIoService,
    private notificationsBannerService: NotificationsBannerService,
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe((paramsMap) => {
      this.processId = paramsMap.get('id');
    });

    this.projectId = environment.formioBaseProject;
    this.getProcess();
  }

  getProcess() {
    this.camundaProcessService['getCurrentTask'](this.processId)
    .subscribe(processResponse => {
      this.process = processResponse.lanes.sort((a, b) => a.properties.order - b.properties.order);
      this.pageTitle = this.process[0].name;
      this.formId = this.process[this.currentLaneIndex].userTasks[this.currentUserTaskIndex].formKey;
      this.ornNumber = processResponse.businessKey;

      this.formIoService.getFormByAlias(this.formId, this.projectId).subscribe((form) => {
        this.formSrc = form;
      });
    }, (error) => {
      this.taskError = (error.status == 404) ? "FORBIDDEN" : "ERRORS.CURRENT_TASK_FINALIZED";
    });
  }

  back() {
    this.router.navigate(['home']);
  }

  callNextOnForm() {
    if (!this.disableNext && this.formSrc) {
      this.disableNext = true;
      this.nextSubmittedSubject.next();
    }
  }

  public next() {
    this.notificationsBannerService.show({message: "CURRENT_TASK_SUCCESS", type: NotificationBarType.Success});
    this.router.navigate(['home']);
  }

  public handleFormError() {
    this.disableNext = false;
  }

}
