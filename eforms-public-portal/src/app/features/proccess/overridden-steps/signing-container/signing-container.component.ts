import { AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import * as moment from 'moment';
import { Observable, Subscription, throwError } from 'rxjs';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { LoaderService } from 'src/app/core/services/loader.service';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { SignService } from 'src/app/core/services/sign.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { DAEFDocument, DAEFDocumentError } from 'src/app/core/types/daef-document';
import { DataToSign } from 'src/app/core/types/data-to-sign';
import { DocumentFile } from 'src/app/core/types/document-file';
import { DocumentFileSubmission } from 'src/app/core/types/document-file-submission';
import { documentStatuses } from 'src/app/core/types/document-statuses';
import { signMethods } from 'src/app/core/types/sign-methods';
import { SignSubmission } from 'src/app/core/types/sign-submission';
import { SignatureOptionsEnum } from 'src/app/core/types/signature-options';
import { DownloadNexuModalComponent } from 'src/app/features/download-nexu-modal/download-nexu-modal.component';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import Utils from 'src/app/shared/utilities/utils';
import { environment } from 'src/environments/environment';
import { DaefDocumentComponent } from './daef-document/daef-document.component';

declare const window;

@Component({
  selector: 'app-signing-container',
  templateUrl: './signing-container.component.html',
  styleUrls: ['./signing-container.component.scss']
})
export class SigningContainerComponent implements OnInit, OnDestroy {
  @ViewChildren(DaefDocumentComponent) daefDocumentComponents: QueryList<DaefDocumentComponent>;

  @Input() ornNumber: string;
  @Input() formId: string;
  @Input() taskId: string;
  @Input() nextStep: Observable<any>;
  @Input() processId: string;
  @Input() isOnlineObservable: Observable<boolean>;
  @Input() shouldPromptBeforeLeave: boolean;

  @Output() nextSuccessEvent: EventEmitter<any>;
  @Output() redirectEvent: EventEmitter<any>;
  @Output() onDocumentStatusCheck: EventEmitter<any>;
  @Output() nextErrorEvent: EventEmitter<any>;
  @Output() onlineStatusChange: EventEmitter<any>;
  /**
   * Event emitter that toggles the visibility of the next step button in parent component
   */
  @Output() shouldShowNextButton = new EventEmitter();
  /**
   * Event emitter that toggles the forced visibility of the next step button's label in parent component
   */
  @Output() onForceIsFinalFlagLabel = new EventEmitter<boolean>();

  isOnline: boolean = true;
  private signedFiles: string[];
  private jsonTimeStamp;
  private signCallbackIds: {
    callbackId: string,
    fileName: string
  }[];
  private nextStepSubscription: Subscription;

  eDeliveryFilesPackageStatus;
  daefDocuments: DAEFDocument[];
  showCheckSignButton: boolean = false;
  areAllDocumentsSigned: boolean = false;
  isSigning: boolean = false;
  hasDocumentSigneesError: boolean = false;
  isInCurrentTask: boolean = false;
  isInAdmin: boolean;
  hideSuccessNotificationBanner: boolean = true;
  private intervalID;
  autosaveData: any;
  signMethodControl: FormControl;
  parsedParams
  evrotrustTransactionId: string;
  evrotrustIsGroupSigning: boolean;
  hideSelect: boolean = false;
  sentDocuments = new Set<DAEFDocument>();

  constructor(
    private daefService: DAEFService,
    private formIoService: FormIoService,
    private signService: SignService,
    private notificationsBannerService: NotificationsBannerService,
    private camundaProcessService: CamundaProcessService,
    private translateService: TranslateService,
    private userProfileService: UserProfileService,
    private router: Router,
    private loaderService: LoaderService,
    private dialog: MatDialog
  ) {
    this.daefDocuments = [];
    this.areAllDocumentsSigned = false;
    this.showCheckSignButton = false;
    this.signCallbackIds = [];
    this.signedFiles = [];
    this.nextSuccessEvent = new EventEmitter<any>();
    this.redirectEvent = new EventEmitter<any>();
    this.nextErrorEvent = new EventEmitter<any>();
    this.onDocumentStatusCheck = new EventEmitter<any>();
    this.isInCurrentTask = this.router.url.includes('current-task');
    this.isInAdmin = this.router.url.includes('admin-services');
    this.signMethodControl = new FormControl('', [Validators.required]);
    this.onlineStatusChange = new EventEmitter<boolean>();
  }

  private get userIdentifier(): string {
    return this.userProfileService.currentUser.personIdentifier.split('-')[1];
  }

  async ngOnInit(): Promise<void> {
    let fetchJSONResult
    
    try {
      //set a unique timestamp for json files. If user tries to sign the same files again, use the same timestamp in order to avoid multiple .p7s file uploaded in MinIo 
      fetchJSONResult = await this.camundaProcessService.getLocalVariableFromProcess(this.taskId, "jsonTimeStamp").toPromise();
      this.jsonTimeStamp = fetchJSONResult['value'].jsonTimeStamp
    } catch (error) {

      this.jsonTimeStamp = moment().unix()

      await this.storeLocalVariable('jsonTimeStamp', this.jsonTimeStamp);
    }

    // This observable gets emitted when the user clicks on the "Next Step" button.
    this.nextStepSubscription = this.nextStep.subscribe(() => {

      // checking for document with insufficient number of signees
      let docsRequiringSignees = this.daefDocuments.filter(daefDocument =>
        daefDocument.requiredSignatures === SignatureOptionsEnum.requestorAndSigneesSignature.value ||
        daefDocument.requiredSignatures === SignatureOptionsEnum.signeesSignature.value
      );

      let insufficientSigneesDocs = docsRequiringSignees.filter(daefDocument => {

        let signeesIdentifiersList = daefDocument.signeesList.map(signee => signee.signeeIdentifier)

        if (daefDocument.requiredSignatures === SignatureOptionsEnum.requestorAndSigneesSignature.value && signeesIdentifiersList.includes(this.userIdentifier) && signeesIdentifiersList.length === 1) {

          daefDocument.errorMessage = 'ADD_AT_LEAST_ONE_SIGNEE_ERROR' // Different error conditions might require different error messages in the future
          daefDocument.hasErrorBorder = true
          return true

        } else if (daefDocument.requiredSignatures === SignatureOptionsEnum.signeesSignature.value && signeesIdentifiersList.length === 0) {

          daefDocument.errorMessage = 'ADD_AT_LEAST_ONE_SIGNEE_ERROR' // Different error conditions might require different error messages in the future
          daefDocument.hasErrorBorder = true
          return true

        } else {
          return false
        }
      })
      // show a notification error for document with insufficient number of signees
      if (insufficientSigneesDocs.length) {
        this.notificationsBannerService.show({
          message: "ERRORS.PLEASE_ADD_ENOUGH_SIGNEES_ERROR",
          type: NotificationBarType.Error,
        });

        this.nextErrorEvent.emit(null);

        return;
      }


      // checking if all documents are signed
      if (!this.areDocumentsSignedOrSigningByOthers()) {
        this.notificationsBannerService.show({
          message: "ERRORS.PLEASE_SIGN_ALL_DOCUMENTS",
          type: NotificationBarType.Error,
        });

        this.nextErrorEvent.emit(null);

        return;
      }

      let submissionData = this.prepareSubmissionData();

      let variables = { variables: {} };
      variables['variables'][`submissionData_${Utils.getFormDataSubmissionKey(this.formId)}`] = {
        "value": {
          "data": submissionData
        }
      };

      this.formIoService
        .postForm(this.taskId, variables)
        .subscribe(
          (result) => {

            // if there is no payment and no singees to wait upon, jump to my-services
            if (this.parsedParams['termTaxHasPayment'] === 'false' && submissionData.signeesList.length === 0) {
              
              this.notificationsBannerService.hideAll();

            } else {
              if (this.isInCurrentTask) {
                localStorage.setItem('SIGN_SUCCESS_MESSAGE', 'true');
              }
              
              this.notificationsBannerService.hideAll();
            }
            this.nextSuccessEvent.emit(null);

          },
          (error) => {
            this.nextErrorEvent.emit(true);
          }
        )
    });

    await this.signService.getNexuJS();

    this.parsedParams = Utils.parseQueryParamsToObject(this.formId);
    this.eDeliveryFilesPackageStatus = this.parsedParams['eDeliveryFilesPackageStatus'];
    
    if (this.eDeliveryFilesPackageStatus === 'FINISHED') {

      try {
        this.autosaveData = await this.camundaProcessService.getLocalVariableFromProcess(this.taskId, "autosave_documentsDataArray").toPromise();
        
      } catch (error) {
        console.log(error)
      }

      this.autosaveData = this.autosaveData?.value?.autosave_documentsDataArray;

      let autosave_documentsDataArray = JSON.parse(localStorage.getItem('autosave_documentsDataArray'));
      let autosaveDataTimestamp = this.autosaveData ? this.autosaveData.timestamp : 0;
      if (autosave_documentsDataArray && autosave_documentsDataArray[this.taskId] && autosave_documentsDataArray[this.taskId].timestamp > autosaveDataTimestamp) {
        this.autosaveData = autosave_documentsDataArray[this.taskId];
      }

      if (this.autosaveData) {
        this.signedFiles = this.autosaveData.signed_files;
        this.signMethodControl.setValue(this.autosaveData.sign_method);
      }

      this.daefService.getDeliveryFilesPackage(this.processId, this.ornNumber, this.isInAdmin).subscribe(async (daefDocuments: DAEFDocument[]) => {

        /**
         * If we are in the CurrentTaskComponent, which occurs when the requestor has added one or more different people as signees,
         * we remove each document from the array if he is not a part of that document's signee list, taken from the first submission
        */
        if (this.isInCurrentTask) {
          const response = await this.formIoService.getFormSubmitionByBuissnessKey(this.formId.split('?')[0], this.ornNumber, 1, '+modified', false).toPromise();

          for (let index = daefDocuments.length - 1; index >= 0; index--) {
            const daefDocument = daefDocuments[index];

            let submissionDocument = response[0].data.documentsForSigningList.find(doc => doc.documentCode === daefDocument.fileCode);

            if (!submissionDocument.signeesList.find(signee => signee.signeeIdentifier === this.userIdentifier)) {
              daefDocuments.splice(index, 1);
            }
          }
        }

        // get camunda autosave variable regarding the documents - signeesList, requiredSignatures
        if (this.autosaveData && this.autosaveData.documents.length > 0) {

          daefDocuments.forEach((currentDaefDocument) => {

            let autosavedDocumentCopy = this.autosaveData.documents.find(doc => doc.fileTitle === currentDaefDocument.fileTitle)

            if (autosavedDocumentCopy) {
              currentDaefDocument.signeesList = autosavedDocumentCopy.signeesList;
              currentDaefDocument.requiredSignatures = autosavedDocumentCopy.requiredSignatures;
            }
          });
        }

        this.daefDocuments = daefDocuments;

        this.isOnlineObservable.subscribe((isOnline) => {
          this.isOnline = isOnline;
          if (!this.isOnline) {
            this.signMethodControl.disable();
          } else if (!this.hasDocumentSigneesError) {
            this.signMethodControl.enable();
          }
          
          this.storeAutosaveLocalVariable();
        });
      });

      //SHOW next button if eDeliveryFilesPackageStatus is FINISHED
      this.shouldShowNextButton.emit(true);

      // call autosave
      this.autosaveFormData()

    } else {
      //hide next button if eDeliveryFilesPackageStatus is different than FINISHED
      this.shouldShowNextButton.emit(false);
    }
  }

  private autosaveFormData() {

    let autosaveTimeoutInMilliseconds = environment.autosaveEveryNMinutes * 60 * 1000;

    this.intervalID = setInterval(async () => {


      await this.storeAutosaveLocalVariable();


    }, autosaveTimeoutInMilliseconds);

  }

  ngOnDestroy(): void {
    if (this.nextStepSubscription) {
      this.nextStepSubscription.unsubscribe();
    }

    clearInterval(this.intervalID); // clear the autosave interval
  }

  handleRetry() {
    this.onDocumentStatusCheck.emit();
  }

  checkIfFinalStep() {
    // check whether to change the next button label to FINISH_PROCESS
    let allDocsPickedRequestorSignature = !this.daefDocuments.filter(daefDocument => daefDocument.requiredSignatures !== SignatureOptionsEnum.requestorSignature.value).length;
    let shouldForceIsFinalStepFlagLabel = this.parsedParams['termTaxHasPayment'] === 'false' && allDocsPickedRequestorSignature;
    this.onForceIsFinalFlagLabel.emit(shouldForceIsFinalStepFlagLabel);
  }

  /**
   * The callback function for each change in the signees form
   */
  handleOnDocumentSigneesError(daefDocumentEvent) {

    // periodic check at every signees form change event
    this.checkIfFinalStep()

    // clean up the error message if the document has no errors
    if (daefDocumentEvent.hasDocumentSigneesError === false) {
      delete daefDocumentEvent.errorMessage
      delete daefDocumentEvent.hasErrorBorder
    }

    // propagated signees error to disable the panels
    this.hasDocumentSigneesError = !!this.daefDocuments.filter((daefDocument: DAEFDocument) => daefDocument.hasDocumentSigneesError === true).length
    this.areAllDocumentsSigned = this.areDocumentsSignedOrSigningByOthers();
    this.hideSelect = this.willAllDocumentsBeSignedByOthers();

    if (this.hasDocumentSigneesError) {
      this.signMethodControl.disable();
    } else if (this.daefDocuments.filter(daefDocument => daefDocument.status.label === documentStatuses.awaiting_sign.label).length === this.daefDocuments.length) { // Don't enable the select if the documents are signed or if they're being signed
      this.signMethodControl.enable();
      this.isSigning = false;
    }
  }

  /**
   * Callback function from child component, 
   * signaling the signees have initiated/loaded.
   */
  handleOnDocumentSigneesLoaded() {
    // initial check after signees have initiated/loaded
    this.checkIfFinalStep()
  }

  /**
   * Callback function from child component, 
   * signaling that a signee has been added/deleted.
   */
  async handleOnDocumentSigneesManipulated() {
    // autosave here
    await this.storeAutosaveLocalVariable();
    
  }

  /**
   * The callback function for the sign button
   */
  async handleSignAll(): Promise<void> {
    this.signMethodControl.enable();
    this.signMethodControl.markAsTouched();

    if (this.signMethodControl.valid && this.doesHaveDocumentsForSigning()) {
      this.signMethodControl.disable();
      this.isSigning = true;
      this.hideSuccessNotificationBanner = false;

      let signMethodValue = this.signMethodControl.value;
      this.notificationsBannerService.hideAllErrors();

      switch (signMethodValue) {
        case signMethods.kep.value:
          this.startPhysicalSignatureSign();
          break;

        case signMethods.borica.value:
          this.signWithBorica();
          break;

        case signMethods.evrotrust.value:
          this.signWithEvrotrust();
          break;
      }
    }
  }

  /**
   * Makes a request to Evrotrust or Borica accordingly and if the status shows that the file is signed, it makes a new request to get the base64 string and then it sends it to Min.Io
   */
  async handleDocumentStatusCheck(): Promise<void> {
    switch (this.signMethodControl.value) {
      case signMethods.evrotrust.value: {
        const signedStatus = await this.signService.checkSignedDataEvrotrust(this.evrotrustTransactionId, this.evrotrustIsGroupSigning).toPromise();

        if (signedStatus.status === 2) {
          const signDataResponse = await this.signService.getSignedDataEvrotrust(this.evrotrustTransactionId, this.evrotrustIsGroupSigning).toPromise();

          await this.executeFuncForEachFile(async (file, daefDocument) => {
            let signData = signDataResponse.find(signData => file.filename === signData.fileName);

            if (signData) {
              await this.uploadFileToMinIoAfterSign(file, signData.content, signData.contentType, daefDocument);
            }
          });
        } else if (signedStatus.status === 3) {
          this.sentDocuments.forEach((deafDocument: DAEFDocument) => this.handleDocumentError({ error: 'Cancelled sign', daefDocument: deafDocument }));
        }
      }
        break;

      case signMethods.borica.value: {
        await this.executeFuncForEachFile(async (file, daefDocument) => {
          if (!file.isSigned) {
            let signCallbackId = this.signCallbackIds.find(signCallback => signCallback.fileName === file.filename);

            if (signCallbackId) {
              const signedStatus = await this.signService.checkSignedDataBorica(signCallbackId.callbackId).toPromise();

              if (signedStatus.responseCode === 'COMPLETED') {
                const signData = await this.signService.getSignedDataBorica(signedStatus.data.signatures[0].signature).toPromise();

                await this.uploadFileToMinIoAfterSign(file, signData.content, signData.contentType, daefDocument);
              }
            }
          }
        });
      }
        break;
    }

    // Call the autosave function which stores the signed files so they don't get signed again
    await this.storeAutosaveLocalVariable();
  }

  /**
   * A callback function which gets executed when each of the documents files gets successfully signed.
   * 
   * @param {DAEFDocument} daefDocument - The condition that gets checked before populating the array
   */
  async handleDocumentSigned(daefDocument: DAEFDocument) {
    daefDocument.status = documentStatuses.signed;

    if (this.areDocumentsSignedOrSigningByOthers()) {
      this.areAllDocumentsSigned = true;
      this.showCheckSignButton = false;
      this.isSigning = false;
      this.signMethodControl.disable();

      if (!this.hideSuccessNotificationBanner) {
        this.notificationsBannerService.show({
          message: "SUCCESSFUL_SIGN",
          type: NotificationBarType.Success,
        });
      }
    }
  }

  /**
   * A callback function which gets executed on a file error
   * 
   * @param {DAEFDocumentError} event - An object containing the Daef document and the error itself
   */
  async handleDocumentError(event: DAEFDocumentError) {
    // This error is thrown when we try to check the file status while it's still being signed
    if (event.error === '{"reason":"443 unknown status: [Document not found]"}') {
      return;
    }

    event.daefDocument.status = documentStatuses.error;
    this.signMethodControl.enable();
    this.isSigning = false;
  }

  /**
   * Makes a request to Borica to sign the file if the file isn't already signed or sent for signing.
   * After this gets executed for each file, if there were no errors, it shows the check sign button
   */
  private async signWithBorica() {
    await this.executeFuncForEachFile(async (file: DocumentFile, daefDocument) => {
      if (!file.isSigned && !this.signCallbackIds.find(callback => callback.fileName === file.filename)) {
        let signBody = {
          contents: [
            {
              confirmText:
                `${this.translateService.instant("DOCUMENT_FOR_SIGNING")} ${file.filename}`,
              contentFormat: "BINARY_BASE64",
              mediaType: file.contentType,
              data: file.content.split(',')[1],
              fileName: file.filename,
              padesVisualSignature: true,
              signaturePosition: {
                imageHeight: 20,
                imageWidth: 100,
                imageXAxis: 20,
                imageYAxis: 20,
                pageNumber: 1,
              },
            },
          ],
        };

        const response = await this.signService.signDataBorica(this.userIdentifier, signBody).toPromise();
        if (response.responseCode === 'ACCEPTED') {
          this.signCallbackIds.push({
            callbackId: response.data.callbackId,
            fileName: file.filename
          });
        }
      }
    });

    await this.storeAutosaveLocalVariable();

    if (!this.daefDocuments.filter(daefDocument => daefDocument.status === documentStatuses.error).length) {
      this.showCheckSignButton = true;
    }
  }

  /**
   * Makes a request to Evrotrust to sign the file if the file isn't already signed or sent for signing.
   * After this gets executed for each file, if there were no errors, it shows the check sign button
   */
  private async signWithEvrotrust() {
    let documentsToSignByEvrotrust = [];
    this.sentDocuments = new Set<DAEFDocument>();

    await this.executeFuncForEachFile(async (file, daefDocument) => {
      if (!file.isSigned) {
        documentsToSignByEvrotrust.push({
          content: (file.content).split(',')[1],
          fileName: file.filename,
          contentType: file.contentType,
        });

        this.sentDocuments.add(daefDocument);
      }
    });

    let dateExpire = new Date();
    dateExpire.setDate(dateExpire.getDate() + 1);

    let signBody = {
      dateExpire: dateExpire,
      documents: documentsToSignByEvrotrust,
      userIdentifiers: [
        this.userIdentifier,
      ],
    };

    try {
      const response = await this.signService.signDataEvrotrust(signBody).toPromise();
      this.evrotrustTransactionId = response.response.transactions[0].transactionID
      this.evrotrustIsGroupSigning = response.groupSigning;

      await this.storeAutosaveLocalVariable();

      if (!this.daefDocuments.filter(daefDocument => daefDocument.status === documentStatuses.error).length) {
        this.showCheckSignButton = true;
      }
    } catch (error) {
      this.sentDocuments.forEach((daefDocument) => this.handleDocumentError({ error: error, daefDocument: daefDocument }))

      throw error;
    }
  }

  /**
   * Begins the process of signing a file with NexU by obtaining the certificate and then calls the prepareFilesForSign function
   */
  private async startPhysicalSignatureSign(): Promise<void> {
    if (this.signService.nexULoaded) {
      window.nexu_get_certificates(
        (certificate) => {
          if (certificate.response !== null) {
            certificate = certificate.response;

            this.prepareFilesForSign(certificate)
          } else {
            this.notificationsBannerService.show({
              message: "ERRORS.500",
              type: NotificationBarType.Error,
            });
          }
        },
        () => this.showNexuError()
      );
    } else {
      this.signService.nexuJS().subscribe((result) => {
        var se = document.createElement('script');
        se.type = "text/javascript";
        se.text = result;
        document.getElementsByTagName('head')[0].appendChild(se);
        this.signService.nexULoaded = true
        this.startPhysicalSignatureSign();
      }, (error) => {

        this.isSigning = false;
        this.signMethodControl.enable();

        this.dialog.open(DownloadNexuModalComponent, {
          data: { title: "IMPORTANT", body: "CANCEL_SERVICE", canProceed: true },
        });

      });
    }
  }

  /**
   * Obtains the necessary data to sign a file with NexU and then calls the actual sign function.
   * 
   * @param {any} certificate - The certificate obtained by NexU
   * 
   * @returns A promise
   */
  private async prepareFilesForSign(certificate): Promise<void> {
    // The NexU library uses JQuery AJAX which doesn't go through the loader interceptor, so we have to manually hide the loader and show the loading spinner to prevent it from flickering
    this.loaderService.isLoading.next(true);

    await this.executeFuncForEachFile(async (file, daefDocument) => {
      if (!file.isSigned) {
        let payloadForSign = await this.getDocumentToSign(certificate, file);
        let dataToSignResponse = await this.signService.digestData(payloadForSign, true).toPromise();


        // nexu_sign_with_token_infos doesn't return a Promise and therefore we can't use await
        // In order to ensure that each file is passing through one by one correctly, we wrap it in a Promise to use await
        await new Promise((resolve, reject) => {
          window.nexu_sign_with_token_infos(
            certificate.tokenId.id,
            certificate.keyId,
            dataToSignResponse.dataToSign,
            "SHA256",
            async (signatureData) => {
              const response = await this.signWithPhysicalSignature(signatureData, payloadForSign, file, daefDocument);
              resolve(response);
            },
            () => {
              this.showNexuError()
              reject();
            }
          );
        });
      }
    });

    // Call the autosave function which stores the signed files so they don't get signed again. In this case it also hides the loader.
    await this.storeAutosaveLocalVariable(false);
  }

  /**
   * Makes a request to NexU to sign the file and then uploads it to Min.Io.
   * The endpoint changes depending on if we want to sign a base64 string or digested data.
   * This is used after obtaining and preparing all the necessary data.
   * 
   * @param {DocumentFile} signatureData - The signature data obtained from NexU
   * @param {any} payloadForSign - The data we prepared
   * @param {any} file - The current file
   * @param {DocumentFile} daefDocument - The current document
   * 
   * @returns A promise
   */
  private async signWithPhysicalSignature(signatureData, payloadForSign: DataToSign, file: DocumentFile, daefDocument: DAEFDocument): Promise<void> {
    payloadForSign.signatureValue = signatureData.response.signatureValue;
    const response = await this.signService.signData(payloadForSign, true).toPromise();
    await this.uploadFileToMinIoAfterSign(file, response.bytes, response.mimeType.mimeTypeString, daefDocument, true);
  }

  /**
   * Prepares a file for upload by changing it's name to a new one, parses the content to a blob and initializing a new instance of the File class.
   * This data is then sent to Min.Io together with the business key(ornNumber) and the id of the form.
   * After the file gets uploaded, we mutate some of the files paramteres so we can then download the newly signed file.
   * 
   * @param {DocumentFile} signedFile - The current signed file that needs to be uploaded.
   * @param {any} content - The current file's content
   * @param {any} mimeType - The current file's mime type
   * @param {DocumentFile} daefDocument - The current document
   * @param {boolean} hideLoader - Dictates whether or not we show the loader.
   * 
   * @returns A promise
   */
  private async uploadFileToMinIoAfterSign(signedFile: DocumentFile, content, mimeType, daefDocument, hideLoader: boolean = false): Promise<void> {
    const fileName = this.signService.getFileNameAfterSign(signedFile, this.jsonTimeStamp);

    const byteString = window.atob(content);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const int8Array = new Uint8Array(arrayBuffer);
    for (let i = 0; i < byteString.length; i++) {
      int8Array[i] = byteString.charCodeAt(i);
    }
    const fileBlob = new Blob([int8Array], { type: mimeType });

    const file = new File([fileBlob], fileName, {
      type: mimeType,
    });

    try {
      await this.signService.uploadFileToMinIo(file, this.ornNumber, this.formId.split('?')[0], hideLoader).toPromise();

      // if (signedFile.fileExtension.toLowerCase() !== 'json') {
      signedFile.location = `${this.ornNumber}/${Utils.getFormDataSubmissionKey(this.formId)}/${fileName}`
      signedFile.contentType = mimeType;
      signedFile.filename = fileName;
      signedFile.storage = "s3";
      signedFile.bucket = environment.formioBaseProject;
      signedFile.fileExtension = fileName.split('.').pop().toUpperCase();

      this.signedFiles.push(signedFile.filename);

      signedFile.isSigned = true;
    } catch (error) {
      daefDocument.status = documentStatuses.error;
      this.isSigning = false;
      this.signMethodControl.enable();
    }
  }

  /**
   * Prepares the data needed to be sent to NexU so it can sign the file.
   * 
   * @param {any} certificate - NexU's certificate that you need to obtain prior to using this.
   * @param {DocumentFile} file - The current file
   * 
   * @returns A promise with an object with the required data for NexU
   */
  private async getDocumentToSign(certificate: any, file: DocumentFile): Promise<DataToSign> {
    let signingDate = new Date();

    var payloadForSign: DataToSign;

    if (
      this.signService.shouldFileBeSignedWithAttachedSignatures(
        file.filename
      )
    ) {
      payloadForSign = {
        signingCertificate: certificate.certificate,
        certificateChain: certificate.certificateChain,
        encryptionAlgorithm: certificate.encryptionAlgorithm,
        documentToSign: file.content.split(",")[1],
        documentName: file.filename,
        signingDate: signingDate,
      };
    } else {
      let digest = await this.getDigest(file);
      payloadForSign = {
        signingCertificate: certificate.certificate,
        certificateChain: certificate.certificateChain,
        encryptionAlgorithm: certificate.encryptionAlgorithm,
        digestToSign: digest,
        documentName: file.filename,
        signingDate: signingDate,
      };
    }

    return payloadForSign;
  }

  /**
   * This is used in order to digest a given file's base64 content and return it back as base64
   * Mainly used for sending JSON files to NexU
   * 
   * @param {DocumentFile} file
   * 
   * @returns A promise with the base64 string of the digested data
   */
  private async getDigest(file: DocumentFile): Promise<string> {
    //convert file to bytes and get digest
    const data = atob(file.content.split(",")[1]);
    const byteNumbers = new Array(data.length);
    for (let i = 0; i < data.length; i++) {
      byteNumbers[i] = data.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    let hash = await crypto.subtle.digest("SHA-256", byteArray);
    //convert bytes back to base64
    let bytes = new Uint8Array(hash);
    let len = bytes.byteLength;
    let binary = "";
    for (var i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return btoa(binary);
  }

  /**
   * Takes in a function which is then passed down to another function in each Daef document component where it gets executed for each file, passing back the current file and document as an argument.
   * This is mainly used if you want to perform an action on each file.
   * 
   * @param {function} callback
   * @returns Promise
   */
  private async executeFuncForEachFile(callback): Promise<void> {
    for (let index = 0; index < this.daefDocumentComponents.length; index++) {
      const daefDocumentComponent = this.daefDocumentComponents.get(index);

      await daefDocumentComponent.handleSignFunction(callback);
    }
  }

  /**
   * Shows a notification banner displaying a NexU error.
   * Mainly used when NexU responds with an error.
   */
  private showNexuError(): void {
    this.notificationsBannerService.show({
      message: "ERRORS.NEXU_ERROR",
      type: NotificationBarType.Error,
    });

    this.signMethodControl.enable();
    this.isSigning = false;
  }

  /**
   * Stores a local variable in the current process' task.
   * 
   * @param {string} key
   * @param {any} value
   * @param {boolean} hideLoader - Dictates whether or not we show the loader.
   * @returns Promise
   */
  private async storeLocalVariable(key: string, value: any, hideLoader: boolean = false): Promise<void> {
    let variables = {};
    variables['modifications'] = {};
    variables['modifications'][key] = { value: {} };
    variables['modifications'][key]['value'][key] = value;
    await this.camundaProcessService.setLocalVariableInProcess(this.taskId, variables, hideLoader).toPromise();
  }

  /**
   * Takes in a given array of files and prepares them for submission.
   * 
   * @param {DocumentFile[]} files - The document files that need to be transformed.
   * @param {string} fileExtension - The extension you want to filter the files by.
   * @returns The transformed files ready for submission
   */
  private prepareDocumentFilesForSubmission(files: DocumentFile[], fileExtension?: string): DocumentFileSubmission[] {
    let submissionFiles: DocumentFileSubmission[] = [];

    if (fileExtension) {
      files = files.filter(file => file.fileExtension.toLowerCase() === fileExtension.toLowerCase());
    }

    for (let index = 0; index < files.length; index++) {
      const element = files[index];

      let documentFileSubmission: DocumentFileSubmission = {
        bucket: environment.formioBaseProject,
        url: `${this.ornNumber}/${Utils.getFormDataSubmissionKey(this.formId)}/${element.filename}`,
        key: `${this.ornNumber}/${Utils.getFormDataSubmissionKey(this.formId)}`,
        name: element.filename,
        originalName: element.filename,
        size: element.size,
        storage: 's3',
        type: element.contentType
      };

      submissionFiles.push(documentFileSubmission);
    }

    /**
     * For the formio submission, we need to both send the p7s file related to the json file we get, as well as the json itself.
     * This mocks the json file and pushes it into the array together with the p7s file if the extension is p7s and if the p7s file exists in our current files
     */
    if (fileExtension === 'p7s' && submissionFiles.length) {
      let jsonFilename = submissionFiles[0].name.replace(`-json-${this.jsonTimeStamp}.p7s`, '.json');
      submissionFiles.push({
        bucket: environment.formioBaseProject,
        key: `${this.ornNumber}/${Utils.getFormDataSubmissionKey(this.formId)}`,
        name: `${jsonFilename}`,
        originalName: `${jsonFilename}`,
        size: 0,
        storage: "s3",
        type: "application/json",
        url: `${this.ornNumber}/${Utils.getFormDataSubmissionKey(this.formId)}/${jsonFilename}`
      });
    }

    return submissionFiles;
  }

  /**
   * Check if all the documents are signed or will be signed by others and there are signees attached to those documents.
   * This is mainly used for validation before we make the submission.
   *
   * @returns A boolean
   */
  private areDocumentsSignedOrSigningByOthers(): boolean {
    let signedOrSigningByOthers = this.daefDocuments.filter(daefDocument =>
      daefDocument.status.label === documentStatuses.signed.label
      || (daefDocument.requiredSignatures === SignatureOptionsEnum.signeesSignature.value && daefDocument.signeesList.length)
    );

    return signedOrSigningByOthers.length === this.daefDocuments.length;
  }

  /**
   * Check if all the documents will be signed by others and there are signees attached to those documents.
   * This is mainly used to hide the sign method select.
   *
   * @returns A boolean
   */
  private willAllDocumentsBeSignedByOthers(): boolean {
    let signedByOthers = this.daefDocuments.filter(daefDocument => 
      (daefDocument.requiredSignatures === SignatureOptionsEnum.signeesSignature.value && daefDocument.signeesList.length)
    );

    return signedByOthers.length === this.daefDocuments.length;
  }

  /**
   * Check if there are any documents that you're a signee of.
   * This is used to check if we should start signing the files.
   *
   * @returns A boolean
   */
  private doesHaveDocumentsForSigning(): boolean {
    return !!this.daefDocuments.find(daefDocument => daefDocument.signeesList.map(signee => signee.signeeIdentifier).includes(this.userIdentifier));
  }

  private async storeAutosaveLocalVariable(hideLoader: boolean = true) {
    let documentsDataArray = {
      documents: [],
      signed_files: this.signedFiles,
      timestamp: moment().unix(),
      sign_method: this.signMethodControl.value
    };

    this.daefDocuments.forEach((daefDoc) => {
      documentsDataArray.documents.push({
        fileTitle: daefDoc.fileTitle,
        requiredSignatures: daefDoc.requiredSignatures,
        signeesList: daefDoc.signeesList
      })
    })

    this.autosaveData = documentsDataArray;
    
    // autosave the document's data
    if (this.isOnline) {
      await this.storeLocalVariable('autosave_documentsDataArray', documentsDataArray, hideLoader);
    } else {
      let autosave_documentsDataArray = {};
      autosave_documentsDataArray[this.taskId] = documentsDataArray;
      localStorage.setItem('autosave_documentsDataArray', JSON.stringify(autosave_documentsDataArray));
    }
  }

  /**
   * Prepares the documents for signing, the requestor value, a unique list of signees and the file attachments for the submission.
   *
   * @returns An object ready to be sent as a submission
   */
  private prepareSubmissionData(): SignSubmission {
    let allUniqueSignees = [];
    let docsForSigningList = [];
    let mapping = {};
    let requestorSignee = 'requestorSignature';
    let fileAttachments = [];

    for (let index = 0; index < this.daefDocuments.length; index++) {
      const daefDocument = this.daefDocuments[index];

      docsForSigningList.push({
        "documentName": daefDocument.fileTitle,
        "formAlias": daefDocument.formAlias,
        "documentCode": daefDocument.fileCode,
        "isConsolidatingForm": daefDocument.consolidating,
        "requiredSignatures": daefDocument.requiredSignatures,
        "signeesList": daefDocument.signeesList
      });

      // Prepare a unique list of signees where the current user is not present
      for (let index = 0; index < daefDocument.signeesList.length; index++) {
        const signee = daefDocument.signeesList[index];
        mapping[signee.signeeIdentifier] = signee.signeeIdentifier;
      }
      delete mapping[this.userIdentifier];

      allUniqueSignees = Object.values(mapping).map(identifier => {
        return { identifier: identifier };
      });

      /**
       * If the requiredSignatures is different than the default value, which is requestorSignature (you're the only signee), 
       * we set the variable to requestorAndSigneesSignature (the signess are you and other people) in order to ensure that the next step is going to be the awaiting sign form
      */
      if (requestorSignee !== daefDocument.requiredSignatures && allUniqueSignees.length > 0) {
        requestorSignee = SignatureOptionsEnum.requestorAndSigneesSignature.value;
      }

      fileAttachments.push({
        fileAttachmentJson: this.prepareDocumentFilesForSubmission(daefDocument.files, 'p7s'),
        fileAttachmentPDF: this.prepareDocumentFilesForSubmission(daefDocument.files, 'pdf'),
        fileAttachmentXML: this.prepareDocumentFilesForSubmission(daefDocument.files, 'xml')
      })
    }

    return {
      "businessKey": this.daefDocuments[0].businessKey,
      "requestor": this.userProfileService.currentUser.personIdentifier,
      "documentsForSigningList": docsForSigningList,
      "requiredSignaturesSelect": requestorSignee,
      "signeesList": allUniqueSignees,
      "documentsForSigningDataGrid": fileAttachments
    }
  }
}