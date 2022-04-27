import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Breadcrumb, BreadcrumbsService } from '@exalif/ngx-breadcrumbs';
@Component({
  selector: 'app-breadcrumbs',
  templateUrl: './breadcrumbs.component.html',
  styleUrls: ['./breadcrumbs.component.scss']
})
export class BreadcrumbsComponent {
  public crumbs$: Observable<Breadcrumb[]> = this.breadcrumbsService.getCrumbs();

  constructor(private breadcrumbsService: BreadcrumbsService) { }

}
