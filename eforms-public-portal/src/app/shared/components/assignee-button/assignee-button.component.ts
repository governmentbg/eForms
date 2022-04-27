import { Component, Input, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TaskService } from 'src/app/core/services/task.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { ServiceDetailsModalComponent } from 'src/app/features/services/service-details-modal/service-details-modal.component';

@Component({
  selector: 'app-assignee-button',
  templateUrl: './assignee-button.component.html',
  styleUrls: ['./assignee-button.component.scss']
})
export class AssigneeButtonComponent implements OnInit {
  @Input() currentTask: any;
  @Input() allowAssignment: boolean = true;

  constructor(
    private router: Router,
    private dialogRef: MatDialogRef<ServiceDetailsModalComponent>,
    private taskService: TaskService,
    private userService: UserProfileService
  ) { }

  ngOnInit(): void {
  }

  assignTask() {
    let currentUser = this.userService.currentUser;
    if (this.currentTask.assignee) {
      this.taskService.unclaimTask(this.currentTask.id)
        .subscribe(result => {
          this.currentTask.assignee = '';
          this.currentTask.assigneeFullName = '';
        })
    } else {
      if (currentUser != null) {
        this.taskService.claimTask(this.currentTask.id, currentUser.personIdentifier)
          .subscribe(res => {
            this.currentTask.assignee = currentUser.personIdentifier;
            this.currentTask.assigneeFullName = currentUser.personName;
          })
      }
    }
  }
}
