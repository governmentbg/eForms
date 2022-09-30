import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { SignService } from 'src/app/core/services/sign.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { DAEFDocument, DAEFDocumentError } from 'src/app/core/types/daef-document';
import { DocumentFile } from 'src/app/core/types/document-file';
import { documentStatuses } from 'src/app/core/types/document-statuses';
import Utils from 'src/app/shared/utilities/utils';
import { SelectSigneesComponent } from '../../select-signees/select-signees.component';

@Component({
  selector: 'app-daef-document',
  templateUrl: './daef-document.component.html',
  styleUrls: ['./daef-document.component.scss']
})
export class DaefDocumentComponent implements OnInit {
  @ViewChild(SelectSigneesComponent) selectSigneesComponent: SelectSigneesComponent;

  @Input() daefDocument: DAEFDocument;
  @Input() ornNumber: string;
  @Input() formId: string;
  @Input() signedFiles: string[];
  @Input() jsonTimeStamp;
  @Input() isOnline = true;
  @Input() isInAdmin: boolean;

  @Output() onDocumentSigned: EventEmitter<DAEFDocument>;
  @Output() onDocumentSigneesError: EventEmitter<DAEFDocument>;
  /**
   * Emitter to signal the father component that this document's signees have initiated/loaded.
   */
  @Output() onDocumentSigneesLoaded: EventEmitter<any>;
  @Output() onDocumentError: EventEmitter<DAEFDocumentError>;
  @Output() onDocumentSigneesManipulated: EventEmitter<any>;

  isDropdownDisabled: boolean = false;

  constructor(
    private signService: SignService,
    private userProfileService: UserProfileService,
    private router: Router
  ) {
    this.onDocumentSigned = new EventEmitter<DAEFDocument>();
    this.onDocumentSigneesError = new EventEmitter<DAEFDocument>();
    this.onDocumentSigneesLoaded = new EventEmitter<any>();
    this.onDocumentError = new EventEmitter<DAEFDocumentError>();
    this.onDocumentSigneesManipulated = new EventEmitter<any>();
  }

  ngOnInit(): void {
    this.setAdditionalFileProperties();
    this.isDropdownDisabled = this.router.url.includes('current-task') || this.isInAdmin;
    this.daefDocument.status = documentStatuses.awaiting_sign;
  }

  private async setAdditionalFileProperties() {
    for (let index = 0; index < this.daefDocument.files.length; index++) {
      const file = this.daefDocument.files[index];
      let fetchedFile;
      let splitLocation = file.location.split('/');
      try {
        fetchedFile = await this.fetchSignedFile(splitLocation[1], this.signService.getFileNameAfterSign(file, this.jsonTimeStamp));
      } catch (error) {
        fetchedFile = await this.fetchUnsignedFile(splitLocation[1], file.filename);

        /*
         * The JSON file can't actually be signed. Instead we generate a new .p7s file and upload it to Min.IO.
         * In order for us to fetch the JSON file as well from the folder in which we store the signed files, we must preemtively upload the JSON file.
        */
        if (fetchedFile.fileExtension === 'json') {
          const jsonFile = new File([file.blob], file.filename)
          this.signService.uploadFileToMinIo(jsonFile, this.ornNumber, this.formId.split('?')[0]).subscribe();
        }
      }

      if (fetchedFile) {
        file.filename = fetchedFile.filename;
        file.contentType = fetchedFile.contentType;
        file.size = fetchedFile.size;
        file.content = fetchedFile.content;
        file.fileExtension = fetchedFile.fileExtension.toUpperCase();
        file.storage = fetchedFile.storage;
        file.isSigned = this.isFileSigned(file.filename);
      }
    }

    if (!this.daefDocument.files.filter(file => !file.isSigned).length) {
      this.onDocumentSigned.emit(this.daefDocument);
    }
  }

  private async fetchSignedFile(documentId, fileName) {
    return await this.signService.fetchFileToBase64(
      this.daefDocument.businessKey,
      documentId, // documentId
      Utils.getFormDataSubmissionKey(this.formId),
      false,
      fileName,
      this.isInAdmin
    )
  }

  private async fetchUnsignedFile(documentId, fileName) {
    return await this.signService.fetchFileToBase64(
      this.daefDocument.businessKey,
      documentId, // documentId
      Utils.getFormDataSubmissionKey(this.daefDocument.formAlias),
      this.daefDocument.consolidating,
      fileName,
      this.isInAdmin
    )
  }

  private isFileSigned(fileName) {
    return this.signedFiles.findIndex(signedFileName => signedFileName === fileName) !== -1;
  }

  async handleFileDownload(event: Event, file: DocumentFile): Promise<void> {
    // Clicking on a download button causes the mat-expansion-panel to expand, so we use stopPropagation to prevent it from expanding.
    event.stopPropagation();

    if (file.storage === 'base64') {
      var a = document.createElement("a");
      a.href = file.content as string;
      a.download = file.filename;
      a.click();
    } else {
      let splitLocation = file.location.split('/');

      this.fetchSignedFile(splitLocation[1], file.filename).then((response) => {
        var a = document.createElement("a");
        a.href = response.content as string;
        a.download = response.filename;
        a.click();
      });
    }
  }

  async handleSignFunction(callback) {
    // We ignore the document if the current user is not in the signee list
    if (!this.daefDocument.signeesList.find(signee => signee.signeeIdentifier === this.userProfileService.currentUser.personIdentifier.split('-')[1])) {
      return;
    }

    this.daefDocument.status = documentStatuses.signing;
    this.daefDocument.percentage = 0;

    for (let fileIndex = 0; fileIndex < this.daefDocument.files.length; fileIndex++) {
      try {
        const file = this.daefDocument.files[fileIndex];
        await callback(file, this.daefDocument);
        this.daefDocument.percentage = (100 * (fileIndex + 1)) / this.daefDocument.files.length;
      } catch (error) {
        this.onDocumentError.emit({ daefDocument: this.daefDocument, error: error });
        console.error(error);
      }
    }

    // If there are no files in the document that aren't signed, we mark the document as signed
    if (!this.daefDocument.files.find(file => !file.isSigned)) {
      this.onDocumentSigned.emit(this.daefDocument);
    }
  }

  handleOnDocumentSigneesError(event) {
    this.daefDocument.hasDocumentSigneesError = event;
    this.onDocumentSigneesError.emit(this.daefDocument)
  }
  
  handleOnDocumentSigneesLoaded(event) {
    this.onDocumentSigneesLoaded.emit()
  }

  handleOnDocumentSigneesManipulated(event) {
    this.onDocumentSigneesManipulated.emit()
  }

}
