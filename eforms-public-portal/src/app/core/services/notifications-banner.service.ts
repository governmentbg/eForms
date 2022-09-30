import { Injectable, EventEmitter } from '@angular/core';
import { NotificationBarModel } from 'src/app/shared/components/notifications-banner/notification-banner.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationsBannerService {
  created = new EventEmitter<NotificationBarModel>();
  closed = new EventEmitter<NotificationBarModel>();
  closeErrors = new EventEmitter<NotificationBarModel>();
  visibleNotifications = false
  constructor() {}

  show(notificationBar: NotificationBarModel) {
    this.created.emit(notificationBar);
    this.visibleNotifications = true;
  }

  hideAll() {
    this.closed.emit();
    this.visibleNotifications = false;
  }

  hideAllErrors() {
    this.closeErrors.emit();
    this.visibleNotifications = false;
  }

  closeNotification() {
    this.visibleNotifications = false;
  }

}
