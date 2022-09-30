import { Component, OnInit } from '@angular/core';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private notificationsBannerService: NotificationsBannerService) { }

  ngOnInit(): void {
    if (localStorage.getItem('SIGN_SUCCESS_MESSAGE')) {
      this.notificationsBannerService.show({message: "SIGN_SUCCESS_MESSAGE", type: NotificationBarType.Success })
      localStorage.removeItem('SIGN_SUCCESS_MESSAGE')
    }
  }

}
