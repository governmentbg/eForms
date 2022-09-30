import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(
    private http: HttpClient
  ) { }

  getBoricaData(data: any) {
    return this.http.post('/api/ePayment/vpos/pay-with-borica', data);
  }

  getBoricaPaymentForm(data: any) {
    let body = new URLSearchParams();

    for (const key in data) {
      if (Object.prototype.hasOwnProperty.call(data, key)) {
        const element = data[key];
        body.set(key, element)
      }
    }

    let options: { headers: HttpHeaders, responseType: "text" } = {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded'),
      responseType: 'text'
    };

    return this.http.post('/Vpos/PayWithBorica', body.toString(), options);
  }

  getAdditionalTaxStatus(paymentId : string) {
    return this.http.get<any>(`/api/ePayment/payment-status?paymentId=${paymentId}`);
  }
}
