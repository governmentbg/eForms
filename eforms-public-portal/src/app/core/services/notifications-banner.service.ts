import { Injectable, EventEmitter } from '@angular/core';
import { NotificationBarModel } from 'src/app/shared/components/notifications-banner/notification-banner.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationsBannerService {
  created = new EventEmitter<NotificationBarModel>();
  closed = new EventEmitter<NotificationBarModel>();
  closeErrors = new EventEmitter<NotificationBarModel>();

  constructor() {}

  show(notificationBar: NotificationBarModel) {
    this.created.emit(notificationBar);
  }

  hideAll() {
    this.closed.emit();
  }

  hideAllErrors() {
    this.closeErrors.emit();
  }

}
