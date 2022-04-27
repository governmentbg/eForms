import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { environment } from 'src/environments/environment';
import { Formio } from 'formiojs';
import { SignService } from 'src/app/core/services/sign.service';

@Component({
  selector: 'app-download-nexu-modal',
  templateUrl: './download-nexu-modal.component.html',
  styleUrls: ['./download-nexu-modal.component.scss']
})
export class DownloadNexuModalComponent {

  form;
  formSrc;
  fileData;
  userOS = 'unknown';
  isDownloading = false;
  isDownloaded = false;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
                      private formioService: FormIoService,
                      private signService: SignService) {}

  ngOnInit(): void {
    this.setUserOS();

    this.formioService.getFormByAlias('common/component/nexu-installer', environment.formioBaseProject).subscribe(result => {
    this.formSrc = result;
    let baseUrl = `${environment.apiUrl}/projects/${environment.formioBaseProject}`
      Formio.setBaseUrl(baseUrl);
      this.formSrc.context = {
        classifier: '',
        formioBaseProject: environment.formioBaseProject,
        apiUrl: environment.apiUrl,
      }
    })
  }

  handleFormReady(form: any) {
    this.form = form;
  }

  reloadPage() {
    window.location.reload();
  }

  downloadInstaller(os: string) {
    this.isDownloading = true;
    this.signService.getNexuInstaller(os).subscribe((response) => {
      let installerData = response[0].data.uriInstaller[0];

      this.signService.downloadInstaller(installerData.key).subscribe((data) => {
        this.fileData = data
        let downloadURL = window.URL.createObjectURL(data);
        let link = document.createElement('a');
        link.href = downloadURL;
        link.download = installerData.originalName;
        link.click();
        this.isDownloading = false;
        this.isDownloaded = true;
      });
    })
  }

  setUserOS()
  {
    if (navigator.userAgent.indexOf("Win") != -1) {
      this.userOS = 'windows';
    }
    if (navigator.userAgent.indexOf("Mac") != -1) {
      this.userOS = 'macosx';
    }
    if (navigator.userAgent.indexOf("Linux") != -1) {
      this.userOS = 'linux';
    }
  }

}
