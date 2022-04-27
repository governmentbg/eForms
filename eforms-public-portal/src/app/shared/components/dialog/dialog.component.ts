import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DialogData } from 'src/app/core/types/dialog-data';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss']
})
export class DialogComponent {

  @Output() confirmed: EventEmitter<any> = new EventEmitter();
  @Output() rejected: EventEmitter<any> = new EventEmitter();

  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) { }

  confirm() {
  }

  reject() {
    this.rejected.emit();
  }
}
