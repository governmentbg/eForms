import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { CamundaProcess } from 'src/app/core/types/camunda-process';
import { UserTaskStatuses } from "src/app/core/types/user-task-statuses";
import { FormIoService } from 'src/app/core/services/form-io.service';
import { IoForm } from 'src/app/core/types/io-form';

import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { MAT_PROGRESS_SPINNER_DEFAULT_OPTIONS_FACTORY } from '@angular/material/progress-spinner';
import { Subject } from 'rxjs';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { DeepLinkService } from 'src/app/core/services/deep-link.service';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { SignService } from 'src/app/core/services/sign.service';
import { DAEFServiceResponse } from 'src/app/core/types/daefservice';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/shared/components/dialog/dialog.component';
// import { runInThisContext } from 'node:vm';
interface FoodNode {
  name: string;
  children?: FoodNode[];
  content?;
  current?;
  id: string;
  completed?: boolean;
  status: string;
  properties?: object;
}
/** Flat node with expandable and level information */
interface ExampleFlatNode {
  expandable: boolean;
  name: string;
  level: number;
  current: boolean;
  content?;
  completed: boolean;
  id;
  status: string;
  properties? : object;
}
@Component({
  selector: 'app-process-navigation',
  templateUrl: './process-navigation.component.html',
  styleUrls: ['./process-navigation.component.scss']
})

export class ProcessNavigationComponent implements OnInit {
  @ViewChild('navigationContainer', { static: false }) navigationContainer: ElementRef;

  nextSubmittedSubject: Subject<void> = new Subject<void>();
  prevSubmittedSubject: Subject<void> = new Subject<void>();
  projectId: string;
  serviceId: string;
  process: Array<CamundaProcess> = [];
  processId: string | null = null;
  processNodes: Array<FoodNode> = []; // array of lanes
  private formId: string | null = null;
  ornNumber: string;
  currentOpenNode = 0;

  currentLaneIndex = 0;
  currentUserTaskIndex = 0;

  currentLaneIndexInTreeControlDataNodes = 0;
  currentUserTaskIndexInTreeControlDataNodes = 0;

  formSrc;
  disableNext = false;
  disablePrev = false;
  shouldPromptBeforeLeave = false;

  activeParent;
  activeChild;
  content?: ExampleFlatNode;

  public innerWidth: any;
  isMobile: boolean;
  isCollapsed = true;

  mainStepNumber: any;
  subStepNumber: any;
  stepPercentage: any;
  currentLaneName: string;
  currentSubStepName: string;
  totalLanes: number;
  showNextButton = true;

  daefServiceData: DAEFServiceResponse;
  requiredAssuranceLevel;
  isValidAssuranceLevel: boolean = false;
  serviceError;
  taskError;

  private _transformer = (node: FoodNode, level: number) => {
    return {
      expandable: !!node.children && node.children.length > 0,
      name: node.name,
      level: level,
      current: node.current,
      id: node.id,
      completed: node.completed,
      status: node.status,
      properties : node.properties
    };
  }

  treeControl = new FlatTreeControl<ExampleFlatNode>(
    node => node.level,
    node => node.expandable
  );

  treeFlattener = new MatTreeFlattener(
    this._transformer, node => node.level, node => node.expandable, node => node.children);

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);


  constructor(
    private camundaProcessService: CamundaProcessService,
    private formIoService: FormIoService,
    private route: ActivatedRoute,
    private deepLinkService: DeepLinkService,
    private router: Router,
    private daefService: DAEFService,
    private userProfileService: UserProfileService,
    private signService: SignService,
    private notificationsBannerService: NotificationsBannerService,
    private dialog: MatDialog
  ) { }
  @HostListener('window:beforeunload', ['$event']) handleBeforeHide(event) {
    return false;
  }

  @HostListener('window:pagehide', ['$event']) handleBeforeUnload(event) {
    return false;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerWidth = window.innerWidth;
    this.isMobile = this.innerWidth < 481;
  }

  hasChild = (_: number, node: ExampleFlatNode) => !node.level;
  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {

      if (params.easId) {
        this.daefService.setService(params.easId);
      } else {
        const easId = this.deepLinkService.getParam("easId")
        if (easId) {
          this.daefService.setService(easId);
        }
      }

      this.innerWidth = window.innerWidth;
      this.projectId = this.daefService.subject.value.projectId;
  
      this.serviceId = this.daefService.subject.value.serviceId;
      this.daefService.getDAEFService(this.serviceId).subscribe(response => {
        this.daefServiceData = response;
  
        this.requiredAssuranceLevel = this.daefServiceData.service.data.requiredSecurityLevel;
        if (this.requiredAssuranceLevel != null && this.requiredAssuranceLevel != '') {
          this.isValidAssuranceLevel = this.userProfileService.isValidAssuranceLevel(this.requiredAssuranceLevel);
          if (!this.isValidAssuranceLevel) {
            this.daefServiceData = null;
            this.serviceError = "ASSURANCE_LEVEL_MISSMATCH";
          }
        }
      })
  
      this.getProcess();
      this.isMobile = this.innerWidth < 481;
    });

  }

  getProcess() {
    this.camundaProcessService.subscribe((processInfo) => {
      if (!processInfo) {
        this.route.paramMap.subscribe((paramsMap) => {
          this.processId = paramsMap.get('id');
        });
      } else {
        this.processId = processInfo.id;
        this.ornNumber = processInfo.businessKey;
      }
      let mapMethod = this.router.url.includes('admin-services') || this.router.url.includes('administration-of-services') ? 'getProcessForAdmin' : 'getProcess';
      this.camundaProcessService[mapMethod](this.processId)
        .subscribe(processResponse => {
          this.shouldPromptBeforeLeave = true;
          this.process = processResponse.lanes.sort((a, b) => a.properties.order - b.properties.order);
          this.processNodes = [];
          this.ornNumber = processResponse.businessKey;
          let index = this.process.length - 1;
          while (index >= 0) {
            if (this.process[index].userTasks?.length) {
              this.processNodes.push({ // array of lanes
                name: this.process[index].name,
                children: this.process[index].userTasks.sort((a, b) => a.properties.order - b.properties.order),
                current: false,
                id: this.process[index].id,
                completed: false,
                status: UserTaskStatuses[0],
              });
            } else if (window.location.href.match('administration-of-services')) {
              this.taskError = "ERRORS.CURRENT_TASK_FINALIZED";
              this.shouldPromptBeforeLeave = false;
            } else {
              this.process.splice(index, 1)
            }
          
            index -= 1;
          }
          this.processNodes.reverse()
          this.totalLanes = this.process.length;
          this.formId = this.process[this.currentLaneIndex].userTasks[this.currentUserTaskIndex].formKey;
          if(this.formId) {
            this.getCurrentForm();
          } else {
            this.notificationsBannerService.show({message: "ERRORS.TASK_ACTION_NOT_FOUND", type: NotificationBarType.Error});
          }         
        });
    });
  }
  updateLanesAndTasksStatuses() {
    this.processNodes.forEach((lane, laneIndex) => {
      let isCompleted = true;
      lane.children.forEach((task, taskIndex) => {
        if (task.status === UserTaskStatuses[1]) {
          lane.current = true;
          task.current = true;

          this.currentLaneIndex = laneIndex;
          this.currentUserTaskIndex = taskIndex;

          lane.status = UserTaskStatuses[1];
          isCompleted = false;
        }

        if (task.status === UserTaskStatuses[0]) {
          isCompleted = false;
        }
      })
      if (isCompleted) {
        lane.status = UserTaskStatuses[2];
        lane.completed = true;
      }
    })
  }

  getDataNodeIndexes() {
    this.currentLaneIndexInTreeControlDataNodes = this.treeControl.dataNodes.findIndex(lane => lane.id === this.process[this.currentLaneIndex].id);
    this.currentUserTaskIndexInTreeControlDataNodes = this.treeControl.dataNodes.findIndex(task => task.id === this.process[this.currentLaneIndex].userTasks[this.currentUserTaskIndex].id);
  }

  callNextOnForm() {
    if (!this.disableNext && this.formSrc) {
      let currentTask = this.process[this.currentLaneIndex].userTasks[this.currentUserTaskIndex];

      if (currentTask.properties.hasConfirmation) {
        const dialogRef = this.dialog.open(DialogComponent, {
          data: { title: 'IMPORTANT', body: 'CONFIRM_FORM_CONTINUATION', canProceed: true },
          width: '40em',
          panelClass: 'full-screen-modal'
        });
  
        dialogRef.afterClosed().subscribe((isConfirmed) => {
          if (isConfirmed) {
            this.continueTask();
          }
        })
      } else {
        this.continueTask();
      }
    }
  }

  private continueTask() {
    this.disableNext = true;
    this.nextSubmittedSubject.next();
  }

  goToPreviousStep() {
    if (!this.disableNext && this.formSrc) {
      this.disablePrev = true;
      this.prevSubmittedSubject.next();
    }
  }

  async getCurrentForm() {
    this.updateLanesAndTasksStatuses();
    this.formSrc = null;
    this.formId = this.process[this.currentLaneIndex].userTasks[this.currentUserTaskIndex].formKey; 
    this.getCurrentFormByAlias()
  }

  getCurrentFormByAlias(){
    this.formIoService.getFormByAlias(this.formId, this.projectId).subscribe((form) => {
      this.formSrc = form;
      this.process[this.currentLaneIndex].formio = form;
      this.dataSource.data = this.processNodes;
      this.treeControl.expandAll();
      this.disableNext = false;
      this.disablePrev = false;
      this.getDataNodeIndexes();
      this.mainStepNumber = this.currentLaneIndex + 1;
      this.subStepNumber = `${this.currentLaneIndex + 1}.${this.currentUserTaskIndex + 1}.`
      this.stepPercentage = Math.round(this.mainStepNumber * 100 / this.totalLanes)
      this.currentLaneName = this.process[this.currentLaneIndex].name;
      this.currentSubStepName = this.process[this.currentLaneIndex].userTasks[this.currentUserTaskIndex].name;
      this.navigationContainer.nativeElement.scrollIntoView()
    })
  }

  public next() {
    let currentTask = this.process[this.currentLaneIndex].userTasks[this.currentUserTaskIndex];

    if (currentTask.properties.isFinal) {
      this.shouldPromptBeforeLeave = false;
      currentTask.properties.redirectTo ? this.router.navigate([currentTask.properties.redirectTo]) : this.router.navigate(['home']);
    } else {
      this.getProcess();
    }
  }

  public handleFormError() {
    this.disableNext = false;
    this.disablePrev = false;
  }

  public getMainStepNumber(node) {
    let index = this.dataSource.data.findIndex(x => x.id === node.id);
    return index + 1;
  }

  public getSubStepNumber(node) {
    let mainStepIndex;
    this.dataSource.data.forEach((mainStep, index) => {
      if (mainStep.children) {
        mainStep.children.forEach((child) => {
          if (child.id === node.id) {
            mainStepIndex = index;
          }
        })
      }
    })
    return `${mainStepIndex + 1}.${parseInt(node.properties.order) + 1}.`;
  }

  public handleShouldShowNextButtonEvent(event){
    this.showNextButton = event
  }
}
