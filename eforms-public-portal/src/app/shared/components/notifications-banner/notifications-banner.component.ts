import { Component, EventEmitter, Optional, Inject, InjectionToken, OnDestroy, Output } from '@angular/core';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarModel, NotificationBarMessage, NotificationBarType } from './notification-banner.model';
import { Subscription } from 'rxjs';
import { NavigationEnd, Router } from '@angular/router';
export const NOTIFICATION_BAR_MESSAGES_CONFIG = new InjectionToken('notification-bar.messages.config');

@Component({
  selector: 'app-notifications-banner',
  templateUrl: './notifications-banner.component.html',
  styleUrls: ['./notifications-banner.component.scss']
})
export class NotificationsBannerComponent implements OnDestroy {
  notifications: NotificationBarModel[] = [];
  placeholders = {};
  additionalMessage: string;

  @Output() closed = new EventEmitter<any>();
  @Output() action = new EventEmitter<any>();

  defaultNotification: NotificationBarModel = {
    action: false,
    actionable: true,
    actionText: '',
    url: '',
    openInNewTab: true,
    autoHide: false,
    closed: false,
    closeable: true,
    hideDelay: 3000,
    hideOnHover: false,
    id: 0,
    isHtml: false,
    message: '',
    type: NotificationBarType.Info
  }

  subscription: Subscription;
  closeSubscribtion: Subscription;
  closeErrorsSubscribtion: Subscription;

  constructor(
    private notificationsBannerService: NotificationsBannerService,
    private router: Router,
    @Inject(NOTIFICATION_BAR_MESSAGES_CONFIG) @Optional() private config?: NotificationBarMessage,
  ) {
    this.subscription = this.notificationsBannerService.created.subscribe(this.addNotificationBar.bind(this));
    this.closeSubscribtion = this.notificationsBannerService.closed.subscribe(this.hideAll.bind(this));
    this.closeErrorsSubscribtion = this.notificationsBannerService.closeErrors.subscribe(this.hideAllErrorNotifications.bind(this));
    router.events.subscribe((val) => {
      // see also 
      if(val instanceof NavigationEnd){
        this.hideAll()
      }
    });
  }
  ngOnDestroy() {
    this.subscription.unsubscribe;
  }

  addNotificationBar(notificationBar: NotificationBarModel) {
    let newNotificationBar = Object.assign({}, this.defaultNotification, notificationBar);

    this.placeholders = notificationBar.placeholders;
    this.additionalMessage = notificationBar.additionalMessage;

    if (this.config && this.config.messages) {
      newNotificationBar.message = this.config.messages[notificationBar.message] || notificationBar.message;
    }

    this.checkNotifications(notificationBar);
    this.notifications.push(newNotificationBar);
    document.getElementById('header-main').scrollIntoView(false);

    if (newNotificationBar.autoHide) {
      window.setTimeout(() => {
        this.hideNotification(newNotificationBar);
      }, newNotificationBar.hideDelay);
    }
  }

  hideNotification(notificationBar: NotificationBarModel) {
    let index = this.notifications.indexOf(notificationBar);
    this.notifications.splice(index, 1);
    this.notificationsBannerService.closeNotification()
  }

  hideOnHover(notificationBar: NotificationBarModel) {
    if (notificationBar.hideOnHover) {
      this.hideNotification(notificationBar);
    }
  }

  onAction(notificationBar: NotificationBarModel, index: number) {
    if (notificationBar.openInNewTab) {
      window.open(notificationBar.url, '_blank');
    } else {
      this.router.navigate([notificationBar.url]);
      this.hideNotification(notificationBar);
      this.closed.emit(notificationBar);
    }
  }

  onClose(notificationBar: NotificationBarModel, index: number) {
    notificationBar.id = index;
    this.hideNotification(notificationBar);
    this.closed.emit(notificationBar);
  }

  onNotificationAction(event: NotificationBarModel) {
  }

  hideAll(){
    this.notifications = []
  }
  hideAllErrorNotifications(){
    this.notifications.forEach((notification, index) => {
      if(notification.type === 'error'){
        this.notifications.splice(index, 1)
      }
    })
  }

  checkNotifications(notificationBar: NotificationBarModel) {
    const notificationsLength = this.notifications.filter(n => n.type === notificationBar.type).length;
    const maxNotificationsLength = 1;

    if (notificationsLength >= maxNotificationsLength) {
      const notificationIndex = this.notifications.findIndex(n => n.type === notificationBar.type);
      this.notifications.splice(notificationIndex, 1);
      this.closed.emit(notificationBar);
    }
  }

  // to show notfication call notifications banner service from any other component:
  // this.notificationsBannerService.show({ message: 'Error', type: NotificationBarType.Error });
}
