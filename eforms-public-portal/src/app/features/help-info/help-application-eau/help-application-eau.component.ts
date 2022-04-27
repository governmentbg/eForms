import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { environment } from 'src/environments/environment';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { Formio } from 'formiojs';
import { UserProfileService } from 'src/app/core/services/user-profile.service';


@Component({
  selector: 'app-help-application-eau',
  templateUrl: './help-application-eau.component.html',
  styleUrls: ['./help-application-eau.component.scss']
})
export class HelpApplicationEauComponent implements OnInit {

  form;
  formSrc;

  constructor(private router: Router,
    public dialog: MatDialog,
    private formioService: FormIoService,
    private userProfileService: UserProfileService) { }

  ngOnInit(): void {
      this.formioService.getFormByAlias('system/help/information-application-eau', environment.formioBaseProject).subscribe(result => {      
      this.formSrc = result;
      let baseUrl = `${environment.apiUrl}/projects/${environment.formioBaseProject}`
      Formio.setBaseUrl(baseUrl);
      this.formSrc.context = {
        classifier: '',
        formioBaseProject: environment.formioBaseProject,
        apiUrl: environment.apiUrl,
        selectedProfile: this.userProfileService.selectedProfile
      }
      })
  }

  back() {
    this.router.navigate(['help-info']);
  }


  handleFormReady(form: any) {
    this.form = form;
  }


}
