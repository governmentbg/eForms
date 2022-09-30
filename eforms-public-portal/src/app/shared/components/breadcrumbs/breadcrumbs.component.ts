import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Breadcrumb, BreadcrumbsService } from '@exalif/ngx-breadcrumbs';
@Component({
  selector: 'app-breadcrumbs',
  templateUrl: './breadcrumbs.component.html',
  styleUrls: ['./breadcrumbs.component.scss']
})
export class BreadcrumbsComponent implements OnInit {
  @Input() isOnlineObservable;
  public crumbs$: Observable<Breadcrumb[]> = this.breadcrumbsService.getCrumbs();
  isOnline = true;

  constructor(private breadcrumbsService: BreadcrumbsService) {}

  ngOnInit(): void {
    this.checkForInternetConnection();  
  }

  private checkForInternetConnection(): void {
    if (this.isOnlineObservable) {
      this.isOnlineObservable.subscribe((isOnline) => {
        this.isOnline = isOnline
      });
    }
  }

}
