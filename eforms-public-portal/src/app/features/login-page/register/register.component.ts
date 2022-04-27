import { Component } from '@angular/core';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  constructor() { }

  openEDelivery() {
    const url = `https://${environment.edeliveryURL}`;
    window.open(url, '_blank');
  }

  openPay() {
    const url = `https://${environment.payEgovURL}`;
    window.open(url, '_blank');
  }
}
