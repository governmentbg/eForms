import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-service-details-modal',
  templateUrl: './service-details-modal.component.html',
  styleUrls: ['./service-details-modal.component.scss']
})
export class ServiceDetailsModalComponent {

  formSrc;
  form;
  caseDocuments = [];
  groupedDocuments = { };
  groupedDocumentsLength: number;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    private formioService: FormIoService,
    private userProfileService: UserProfileService,
    private deafService: DAEFService,
    private http: HttpClient,
    private router: Router) {}

  ngOnInit(): void {
    if (this.data.data.deadlineTerm == 1) {
      this.data.data.deadlineUnit = this.data.data.deadlineUnit.slice(0, -1);
    }

    this.getCaseDocuments().subscribe((response) => {
      if (response.length > 0) {
        this.caseDocuments = response
        let urlParts = [];
        let caseDocumentNameSplit = [];
        this.router.url.includes('admin-services') 
        let downloadUrl = `api/${this.router.url.includes('admin-services') ? 'admin/' : '' }project/${environment.formioBaseProject}/file?businessKey=${this.data.data.businessKey}&formId=`;
        this.caseDocuments.forEach((caseDocumentData, index) => {
          urlParts = caseDocumentData.url.split('/');
          caseDocumentData.downloadUrl = downloadUrl;
          if (urlParts.length == 3) {
            caseDocumentData.downloadUrl += urlParts[1];
          } else {
            caseDocumentData.downloadUrl += urlParts[2];
            caseDocumentData.downloadUrl += '&documentId=' + urlParts[1].replaceAll('_', '/');
          }
          caseDocumentData.downloadUrl += `&filename=${caseDocumentData.name}`

          caseDocumentNameSplit = caseDocumentData.name.split('.');
          const key = caseDocumentNameSplit[0];

          if (!this.groupedDocuments.hasOwnProperty(key)) {
            this.groupedDocuments[key] = [];
          }

          caseDocumentData.fileExtension = caseDocumentNameSplit.pop().toUpperCase();

          this.groupedDocuments[key].push(caseDocumentData);

          this.caseDocuments[index] = caseDocumentData
        })
      }

      this.groupedDocumentsLength = Object.keys(this.groupedDocuments).length;
    });
  }

  handleFormReady(form: any) {
    this.form = form;
  }

  getCaseDocuments() {
    let endpointURL = `api/project/${environment.formioBaseProject}/cases/files?businessKey=${this.data.data.businessKey}`;
    if (this.userProfileService.selectedProfile) {
      endpointURL += `&applicant=${this.userProfileService.selectedProfile.identifier}`;
    }
    return this.http.get<any>(endpointURL);
  }

  downloadCaseDocument(caseDocument) {
    const httpOptions = {
      responseType: 'blob' as 'json',
    };
    this.http.get(caseDocument.downloadUrl, httpOptions).subscribe((response: any) => {
      let downloadURL = window.URL.createObjectURL(response);
      let link = document.createElement('a');
      link.href = downloadURL;
      link.download = caseDocument.originalName;
      link.click();
    })
  }

  bytesToSize(bytes: number) {
    let sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    if (bytes == 0) return '0 Byte';
    let i = Math.floor(Math.log(bytes) / Math.log(1024));
    return Math.round(bytes / Math.pow(1024, i)).toFixed(2) + ' ' + sizes[i];
  }

}
