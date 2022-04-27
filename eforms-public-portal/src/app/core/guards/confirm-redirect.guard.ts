import { Component, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { DialogComponent } from 'src/app/shared/components/dialog/dialog.component';

@Injectable({ providedIn: 'root' })
export class ConfirmRedirectGuard implements CanDeactivate<Component>
{
    constructor(
        public dialog: MatDialog,
    ) { }

    async canDeactivate(component, route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

        if (component.shouldPromptBeforeLeave) {
            const modalRef = this.dialog.open(DialogComponent, {data : {title: 'IMPORTANT', body: 'CONFIRM_LEAVE', canProceed: true}});
            return await new Promise<boolean>(resolve=>{
                modalRef.afterClosed().pipe()
                 .subscribe(
                    (data:any) => {
                        if(data) {
                            resolve(true);
                        } else {
                            resolve(false);
                        }
                 })
            })
        }
        return true;
    }
}
