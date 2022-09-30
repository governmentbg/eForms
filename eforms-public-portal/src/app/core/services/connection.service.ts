import { Injectable } from "@angular/core";
import * as moment from "moment";
import { interval, Subject} from "rxjs";
import { timestamp } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ConnectionService {
  
  private hasInternetAccess = new Subject<boolean>();
  private currentAccess = true;
  private interval;

  constructor() {
    this.interval = interval(5000).subscribe(() => this.checkOnlineStatus());
  }

  checkInternetConnection(): Subject<boolean> {
    return this.hasInternetAccess;
  }

  async checkOnlineStatus() {
    try {
      const online = await fetch("/favicon.ico?t=" + moment().unix());
      let status = online.status >= 200 && online.status < 300;
      if (status && this.currentAccess == false) {
        this.hasInternetAccess.next(status);
        this.currentAccess = true;
      }
    } catch (err) {
      if (this.currentAccess == true) {
        this.hasInternetAccess.next(false);
        this.currentAccess = false;
      }
    }
  };

}
