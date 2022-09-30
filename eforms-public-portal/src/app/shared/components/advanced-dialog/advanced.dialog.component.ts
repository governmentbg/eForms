import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AdvancedDialogData } from 'src/app/core/types/advanced-dialog-data';

@Component({
    selector: 'app-advanced-dialog',
    templateUrl: './advanced.dialog.component.html',
    styleUrls: ['./advanced.dialog.component.scss']
})
export class AdvancedDialogComponent {

    @Output() confirmed: EventEmitter<any> = new EventEmitter();
    @Output() rejected: EventEmitter<any> = new EventEmitter();

    constructor(@Inject(MAT_DIALOG_DATA) public data: AdvancedDialogData) { }

    confirm() {
        this.confirmed.emit();
    }

    reject() {
        this.rejected.emit();
    }

    get typeOfBody() {
        return typeof this.data.bodyMessage
    }

}
