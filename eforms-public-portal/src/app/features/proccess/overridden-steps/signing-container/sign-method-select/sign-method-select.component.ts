import { Component, Input, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectChange } from '@angular/material/select';
import { SignService } from 'src/app/core/services/sign.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { signMethods } from 'src/app/core/types/sign-methods';
import { DownloadNexuModalComponent } from 'src/app/features/download-nexu-modal/download-nexu-modal.component';

@Component({
  selector: 'app-sign-method-select',
  templateUrl: './sign-method-select.component.html',
  styleUrls: ['./sign-method-select.component.scss']
})
export class SignMethodSelectComponent implements OnInit {
  @Input() signMethodControl: FormControl;

  signMethods: any[]

  constructor(
    private signService: SignService,
    private dialog: MatDialog,
    private userProfileService: UserProfileService
  ) {
    this.signMethodControl = new FormControl('', [ Validators.required ]);
    this.signMethods = [ signMethods.kep ];
  }

  ngOnInit(): void {
    this.getSignMethods();
  }

  handleSignMethodChange(event: MatSelectChange): void {
    if (event.value === signMethods.kep.value) {
      this.signService.nexuJS().subscribe((result) => {
        // Ignore if NexU is installed
      },
        (error) => {
          this.showDialog();
        }
      );
    }
  }

  private showDialog(): void {
    this.dialog.open(DownloadNexuModalComponent, {
      data: { title: "IMPORTANT", body: "CANCEL_SERVICE", canProceed: true },
    });
  }

  private async getSignMethods(): Promise<void> {
    try {
      const evrotrustResponse = await this.signService.getSignMethodEvrotrust(this.userProfileService.currentUser.personIdentifier.split('-')[1]);
      if (evrotrustResponse['isReadyToSign']) {
        this.signMethods.push(signMethods.evrotrust);
      }
    } catch (error) {}
    
    try {
      const boricaResponse = await this.signService.getSignMethodBorica(this.userProfileService.currentUser.personIdentifier.split('-')[1]);
      if (boricaResponse['responseCode'] === 'OK') {
        this.signMethods.push(signMethods.borica);
      } 
    } catch (error) {
      // Borica responds with an error when you can't use it, so we just ignore it
    }

    if (this.signMethods.length === 1) {
      this.signMethodControl.setValue(signMethods.kep.value);
    }
  }
}
