import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { RedashService } from 'src/app/core/services/redash.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { FormIoService } from 'src/app/core/services/form-io.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {
  dashboards = [];
  publicUrl: any;
  dashboardTitle: string = '';
  suplierCode: string;

  constructor(
    private domSanitizer: DomSanitizer,
    private redashService: RedashService,
    private activatedRoute: ActivatedRoute,
    private oidcSecurityService: OidcSecurityService,
    private router: Router,
    private userProfileService: UserProfileService,
    private formioService: FormIoService
  ) { }

  ngOnInit(): void {
    this.formioService.getFormIoSupplierByIdentifier(this.userProfileService.selectedProfile.identifier).subscribe(supplier => {
      this.suplierCode = supplier[0].data.code
      this.activatedRoute.params.subscribe((params) => {
        this.getDashboard(params.dashboardId);
      });
    });
  }

  getDashboard(dashboardId) {
    this.redashService.getDashboard(dashboardId).subscribe((response) => {
      let url = new URL(response['public_url']);
      url.searchParams.set('auth_token', this.oidcSecurityService.getToken());
      let queryparams = `&p_dateRange=d_last_30_days&p_supplier=${this.suplierCode}&p_service=00000000`
      this.publicUrl = this.domSanitizer.bypassSecurityTrustResourceUrl(url.toString() + queryparams);
      this.dashboardTitle = response['name'];
    });
  }

  back() {
    this.router.navigate(['/admin-panel/report-panel/reports']);
  }
}
