import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { FormControl, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { ActivatedRoute, Router } from "@angular/router";
import { FormioComponent } from "@formio/angular";
import { LangChangeEvent, TranslateService } from "@ngx-translate/core";
import { OidcSecurityService } from "angular-auth-oidc-client";
import { Formio } from "formiojs";
import * as moment from "moment";
import { Observable, Subscription } from "rxjs";
import { pairwise, startWith } from "rxjs/operators";
import { CamundaProcessService } from "src/app/core/services/camunda-process.service";
import { DAEFService } from "src/app/core/services/daef-service.service";
import { DeepLinkService } from "src/app/core/services/deep-link.service";
import { FormIoService } from "src/app/core/services/form-io.service";
import { NotificationsBannerService } from "src/app/core/services/notifications-banner.service";
import { UserProfileService } from "src/app/core/services/user-profile.service";
import { documentRequirement } from "src/app/core/types/document-requirement";
import { FormIoRenderOptions } from "src/app/core/types/form-io-render-options";
import { panelClasses } from "src/app/core/types/panel-classes";
import { DialogComponent } from "src/app/shared/components/dialog/dialog.component";
import Utils from "src/app/shared/utilities/utils";
import { environment } from "src/environments/environment";
import { NotificationBarType } from "../../../shared/components/notifications-banner/notification-banner.model";


@Component({
  selector: "app-io-form-builder",
  templateUrl: "./io-form-builder.component.html",
  styleUrls: ["./io-form-builder.component.scss"],
})
export class IoFormBuilderComponent implements OnInit, OnDestroy {
  /**
   * Wrapper Subscription for services. To be destroyed on ngDestroy so component properly unsubscribes from all services
   * @private
   */
  private wrapperSubscription = new Subscription();
  /**
   * JSON of form that will be loaded
   */
  @Input() formSrc;
  /**
 * Id of form that will be loaded
 */
  @Input() formId;
  /**
 * Event for when next step button is clicked in parent component
 */
  @Input() nextEvent: Observable<void>;
  /**
 * Event for when previous step button is clicked in parent component
 */
  @Input() prevEvent: Observable<void>;
  /**
 * ID of the current step in the Camunda process
 */
  @Input() taskId;
  /**
 * Unique number of the current service instance
 */
  @Input() ornNumber;
  /**
 * Boolean indicating if user can go to the previous step in the process
 */
  @Input() canGoToPrevious : boolean;
  /**
 * Boolean indicating if the initial fetching of the form submition should be skipped
 */
  @Input() skipGetFormSubmition = false;
  /**
 * Observable to monitor if the user has internet access
 */
  @Input() isOnlineObservable;
  /**
   * Event emitter for should user be prompted before leave
  */
  @Output() shouldPromptBeforeLeaveEvent = new EventEmitter<boolean>();
    /**
   * Event emitter on successful submition of the form
   */
  @Output() nextSuccessEvent = new EventEmitter();
  /**
 * Event emitter on a failed submition of the form
 */
  @Output() nextErrorEvent = new EventEmitter<any>();
  /**
 * Event emitter that toggles the visibility of the next step button in parent component
 */
  @Output() shouldShowNextButton = new EventEmitter();
  /**
 * Event emitter that toggles the style of the next step button in parent component
 */
  @Output() shouldAddDisabledStyle = new EventEmitter<boolean>();
  /**
 * Event that toggles the disabled property of the next step button in parent component
 */
  @Output() shouldDisableButton = new EventEmitter<boolean>();
  /**
   * Event emitter that to force the parent component to refresh the process map parameters
   */
  @Output() onRefreshMapParameters = new EventEmitter<boolean>();
  /**
   * Event emitter that toggles the forced visibility of the next step button's label in parent component
   */
  @Output() onForceIsFinalFlagLabel = new EventEmitter<boolean>();

  /**
   * Event listner for next step event
   */
  nextSubscription;
  /**
  * Event listner for previous step event
  */
  prevSubscription;
  /**
   * Subscription wrapper for initial fetching of query params
  */
  initSubscription;
  /**
   * Subscription wrapper for fetching of user profile
  */
  userSubscription;
  /**
   * Active form component instance
  */
  form: FormioComponent;
  window: any;
  /**
   * Event emitter for changes in form JSON or data
  */
  refreshForm;
  /**
   * DataSources to remove before submit. Done manually due to FormIo bug
  */
  formPersistentData = [];
  /**
   * ID of the current service
  */
  arId: string;
  /**
   * Boolean indicating if the current form has finished loading
  */
  formLoaded = false;
  /**
   * Nested forms array in consolidating form
  */
  nestedForms = [];
  /**
   * ID of the currently active nested form
  */
  nestedFormId: string;
  /**
   * Boolean indicating if nested form submision is fetched on initial load or tab change
  */
  initalLoad = true;
  /**
   * Count of custom pannels in a form
  */
  indexedPanelsCount: number = 0;
  /**
   * The instance of the currently active nested form
  */
  consolidatingForm
  /**
   * Boolean for is the form in readonly mode
  */
  readonly = false
  /**
   * Boolean indicating if nested form is currently being loaded
  */
  initialLoadNested = true
  /**
   * Data fetched from a previous form submition
  */
  formSubmition: any = { data: {} }
  /**
   * Boolean indicating if the form was submited before
  */
  hasFormSubmition = false;
  /**
   * Subscription for autosave
   * @private
  */
  private intervalID;
  /**
 * Shows if service has initiliazing documents
 */
  hasInitDocument = false
  /**
   * List of all initializing documents
   */
  initDocuments: [];
  /**
   * id of the currently selected initiliazing document
  */
  selectedInitDocument
  /**
   * Has the initializing document value came from Camunda
  */
  wasInitDocumentPreselected = false
  /**
  * Has user return from preview page
  */
  isAfterCorrection = false
  /**
  * Initiliazing document selected on previous step and saved in Camunda
  */
  initDocumentFromQueryParam
  /**
  * Should user be prompted before initializing document value was changed
  */
  shouldPromptBeforeInitDocumentChange = true
  /**
  * Does the user have access to internet
  */
  isOnline = true

  formIoRenderOptions: FormIoRenderOptions;

  constructor(
    private formIoService: FormIoService,
    public oidcSecurityService: OidcSecurityService,
    private notificationsBannerService: NotificationsBannerService,
    private translateService: TranslateService,
    private userProfileService: UserProfileService,
    private daefService: DAEFService,
    public dialog: MatDialog,
    private deepLinkService: DeepLinkService,
    private router: Router,
    private camundaProcessService: CamundaProcessService,
    private route: ActivatedRoute
  ) {}
  /**
   * Is the form consolidating or simple
  */
  public getIsFormConsolidating() {
    return this.nestedForms.length > 0 || this.hasInitDocument
  }
  /**
   * Get form data depending on if the form is simple or nested
  */
  get getSimpleOrNestedFormData() : object{

    let data = this.nestedFormId ? this.form.formio.data.consolidatingForm.data : this.form.formio.data;
    return data;
  }
  /**
   * Get form id depending on if the form is simple or nested
  */
  get getSimpleOrNestedFormId() : string{
    let formId = this.nestedFormId ? this.nestedFormId : this.formId;
    return formId;
  }

  async ngOnInit() {
    this.wrapperSubscription.add(
      this.isOnlineObservable.subscribe((isOnline) => {
        this.updateOnlineStatus(isOnline);
    }));

    this.initSubscription = this.wrapperSubscription.add(this.route.queryParams.subscribe(params => {

      if (params.easId) {
        this.daefService.setService(params.easId);
      } else {
        const easId = this.deepLinkService.getParam("easId")
        if (easId) {
          this.daefService.setService(easId);
        }
      }

      this.arId = this.daefService.getServiceId();
      this.refreshForm = new EventEmitter();
      this.window = window;
      let baseUrl = `${environment.apiUrl}/project/${environment.formioBaseProject}`;
      Formio.setBaseUrl(baseUrl);
      //remove form context from storege so no mixups happen with nested forms
      localStorage.removeItem("formContext");
      this.userSubscription = this.wrapperSubscription.add(this.userProfileService.subscribe(async userProfile => {
        if (!userProfile) {
          return;
        }
        this.formSrc.context = {
          apiUrl: environment.apiUrl,
          clientId: environment.clientId,
          formioBaseProject: environment.formioBaseProject,
          businessKey: this.ornNumber.substring(0, 11),
          accessToken: `Bearer ${this.oidcSecurityService.getToken()}`,
          selectedProfile: this.userProfileService.selectedProfile,
          arId: this.arId,
          formAlias: this.getFormAlias(),
          personIdentifier: userProfile?.personIdentifier,
          currentRoute: this.router.url,
          taskId: this.taskId
        };
        if (this.formId.includes("?")) {
          let querryParams = this.formId.split("?").pop().split("&");
          querryParams.forEach((param) => {
            let paramKeyAndValue = param.split("=");
            if (paramKeyAndValue[1] === 'true' || paramKeyAndValue[1] === 'false') {
              paramKeyAndValue[1] = paramKeyAndValue[1] === 'true'
            }
            this.formSrc.context[paramKeyAndValue[0]] = paramKeyAndValue[1];
            if (paramKeyAndValue[0] === 'readOnly' && paramKeyAndValue[1] === true) {
              this.readonly = true
            }
            if (paramKeyAndValue[0] === 'initDocument' && paramKeyAndValue[1] !== '') {
              this.isAfterCorrection = true
              this.wasInitDocumentPreselected = true
              this.initDocumentFromQueryParam = paramKeyAndValue[1]
            }
          });
        }
        this.formatFormComponentsJSON();
        this.nextSubscription = this.wrapperSubscription.add(
          this.nextEvent.subscribe((isOnline) => this.goToNextFormStep())
        );
        this.prevSubscription = this.wrapperSubscription.add(
          this.prevEvent.subscribe(() => this.goToPrevFormStep())
        );
        if (this.getIsFormConsolidating()) {
          //check if all nested forms were clicked and are valid
          await this.handleInitialNestedFormLoad();
        }
        if (!this.getIsFormConsolidating() && !this.skipGetFormSubmition && !this.route.snapshot.data?.isMetadataProcess) {
          try {
            let variable = await this.camundaProcessService.getLocalVariableFromProcess(this.taskId, "submissionData_" + Utils.getFormDataSubmissionKey(this.formId)).toPromise();
            let formSubmition = variable["value"]["data"]
            formSubmition.isDataPrefilledFromBackend = true
            // Set the data from the server or the one saved in local storage in offline mode
            this.formSubmition.data = this.getMostUpToDateValues(formSubmition);
            this.formSubmition.data.hasFormSubmition = true;
          } catch {
            let formSubmition = await this.formIoService.getFormSubmitionByBuissnessKey(this.getFormAlias(), this.ornNumber).toPromise()
            if (formSubmition.length) {
              formSubmition[0].data.dontOverrideUserData = true
              if (this.readonly) {
                formSubmition[0].data.isDataPrefilledFromBackend = true
              }
              //Set the data from the server or the one saved in local storage in offline mode
              this.formSubmition.data = this.getMostUpToDateValues(formSubmition[0].data);
              this.formSubmition.data.hasFormSubmition = true;
            } else {
              this.formSubmition.data.hasFormSubmition = false;
            }
          }
        }
        if(this.hasInitDocument){
          //subscibe to lister for init document change
          this.selectedInitDocument.valueChanges.pipe(startWith(null), pairwise()).subscribe(async ([oldVal]) => {
            if(oldVal) {
              if(this.shouldPromptBeforeInitDocumentChange === true) {
                const dialogRef = this.dialog.open(DialogComponent, {
                  data: { title: 'IMPORTANT', body: 'CONFIRM_FORM_INIT_DOCUMENT_CNANGE', canProceed: true },
                  width: '40em',
                  panelClass: 'full-screen-modal'
                });

                dialogRef.afterClosed().subscribe(async (isConfirmed) => {
                  if (isConfirmed) {
                    await this.handleInitDocumentChange()
                  } else {
                    this.shouldPromptBeforeInitDocumentChange = false
                    this.selectedInitDocument.setValue(oldVal)
                  }
                })
              } else {
                this.shouldPromptBeforeInitDocumentChange = true
              }
            } else {
              await this.handleInitDocumentChange()
            }
          })
          try {
            let formSubmition = await this.camundaProcessService.getLocalVariableFromProcess(this.taskId, "initDocumentSelected").toPromise()
            if(formSubmition) {
              this.wasInitDocumentPreselected = true
              this.selectedInitDocument.setValue(formSubmition['value']['data']['initDocumentSelected'])
            } else {
              this.selectedInitDocument.setValue(this.initDocumentFromQueryParam)
            }
          } catch {
            this.selectedInitDocument.setValue(this.initDocumentFromQueryParam)
          }
        }
        
        this.formIoRenderOptions = {
          language: this.translateService.currentLang,
          i18n: this.translateService.translations,
          prefix: 'BGN ',
          suffix: '',
          decimalSeparator: '.',
          thousandsSeparator: ' '
        }

        //if it's a consolidating form, don't load renderer, but preview page first
        this.formLoaded = !this.getIsFormConsolidating() && !!userProfile
      }))

      let parsedParams = Utils.parseQueryParamsToObject(this.formId);
      if (parsedParams['signingCompleted'] === 'false') {
        let showWaitingMessage = localStorage.getItem('showWaitingMessage_' + this.ornNumber);
        if (showWaitingMessage) {
          this.notificationsBannerService.show({message: "WAITING_FOR_SIGNEES", type: NotificationBarType.Warn })
        }
        localStorage.setItem('showWaitingMessage_' + this.ornNumber, 'true');
      } else if(parsedParams['signingCompleted'] === 'true') {
        localStorage.removeItem('showWaitingMessage_' + this.ornNumber);
      }

      // check for error message variable from ePayment
      if(parsedParams['ePaymentFailureMessageVariable']) {
        
        try {
          let error = decodeURIComponent(parsedParams['ePaymentFailureMessageVariable'])
                        .replace(/[+]/g, ' ');
  
          error = JSON.parse(error);

          const translation = this.translateService.instant(error['message']);
          const hasTranslation = translation !== error['message'] && translation !== '';
          
          if(hasTranslation && error['message'].match(/((WARN)|(ERROR)|(INFO))\.[a-zA-Z-_]+\.[a-zA-Z-_]+/)){

            this.notificationsBannerService.show({message: error['message'], type: Utils.getNotificationTypeFromMessage(error['message']), additionalMessage: error['data']})
          } else {
            this.notificationsBannerService.show({message: "ERRORS.500", type: NotificationBarType.Error})
          }
        } catch (error) {
          this.notificationsBannerService.show({message: "ERRORS.500", type: NotificationBarType.Error})
          console.error(error);
        }
      }
    }));

    // call autosave function
    this.autosaveFormData();
  }

  ngOnDestroy() {
    this.wrapperSubscription.unsubscribe();
    clearInterval(this.intervalID);
  }

  /**
   * Change form language
   * @param currentLang selected language
   * @param translations translation files
  */

  private changeFormIoLanguage(currentLang: string, translations: any) {
    for (const key in translations) {
      if (Object.prototype.hasOwnProperty.call(translations, key)) {
        if (key.includes('FORMIO.')) {
          const newKey = key.replace(/FORMIO./g, '');
          translations[newKey] = translations[key];
        }
      }
    }
    this.form.formio.addLanguage(currentLang, translations);
    this.form.formio.language = currentLang;
    this.form.formio.redraw();
  }

  /**
   * Autosave form data
  */
  private autosaveFormData() {

    let autosaveTimeoutInMilliseconds = environment.autosaveEveryNMinutes * 60 * 1000;

    this.intervalID = setInterval(async () => {

      if (this.formLoaded) {

        let formData = this.getSimpleOrNestedFormData
        let formId = this.getSimpleOrNestedFormId
        await this.setLocalVariable(formData, formId, true);

      }
    }, autosaveTimeoutInMilliseconds);
  }

  /*
    Form Event Handlers
  */
  /**
   * Event handler for when the formio component has finished loading
   * @param form the formio form instance
  */
  async handleFormReady(form: FormioComponent) {
    this.form = form;
    if(this.hasInitDocument){
      this.form.formio.data.initDocument = this.selectedInitDocument.value
    }
    this.changeFormIoLanguage(this.translateService.currentLang, this.translateService.translations[this.translateService.currentLang]);

    this.wrapperSubscription.add(
      this.translateService.onLangChange.subscribe((langChanged: LangChangeEvent) => {
        this.changeFormIoLanguage(langChanged.lang, langChanged.translations);
      })
    );
    if (!this.nestedFormId && !this.skipGetFormSubmition) {
      this.indexedPanelsCount = 0
      this.findModifyIndexedPanels(this.form.formio.components, false, this.formSubmition.data.hasFormSubmition)
    }
  }

  /**
   * Event handler for when formio form change
   * @param form the formio form instance
  */
  async handleFormChange(form: any) {
    //save uploaded file to camunda so it would be prefilled on page refresh
    if (form?.changed?.instance?.component?.type === 'file' && !this.readonly) {
      let formData = this.getSimpleOrNestedFormData
      let formId = this.getSimpleOrNestedFormId
      await this.setLocalVariable(formData, formId, false)
    }
    if(this.consolidatingForm && this.consolidatingForm.subForm){
      if(!this.consolidatingForm.renderOptions) {
        this.consolidatingForm.renderOptions = this.formIoRenderOptions
        this.consolidatingForm.options = this.consolidatingForm.options ? {...this.consolidatingForm.options, ... this.formIoRenderOptions } : this.formIoRenderOptions
        this.consolidatingForm.subForm.options = {...this.consolidatingForm.subForm.options, ... this.formIoRenderOptions }
        this.consolidatingForm.redraw();

      }
    }
    //when nested form has loaded, update it's panels
    if( this.form?.formio.data.redrawNestedForm  === true) {
      this.form.formio.data.redrawNestedForm = false
      this.indexedPanelsCount = 0
      // validate all indexed panels
      let nestedForm = this.nestedForms.find(n => n.id === this.nestedFormId)
      this.findModifyIndexedPanels(
        this.consolidatingForm.subForm.components,
        nestedForm.shouldValidateForm
      );
    }
  }

  /**
   * Event handler for when formio form rerenders
  */
  async handleFormRender() {
    if (this.form) {
      this.consolidatingForm = this.form.formio.getComponent("consolidatingForm");
      if (this.consolidatingForm && this.initialLoadNested) {
        this.initialLoadNested = false
        await this.loadNestedForm(this.nestedFormId);
        this.shouldShowNextButton.emit(true)
      }
    }
  }

  /**
   * Event handler for custom events fired by form
   * @param event
  */
  async handleCustomEvent(event) {
    if (event.detail?.type === 'custom-event-error') {
      let errorMessage = (event.detail.message ? event.detail.message : 'ERRORS.500');
      this.notificationsBannerService.show({
        message: errorMessage,
        type: NotificationBarType.Error,
      });
    }
    if (event.type === "loadForm" || event.type === "loadFormOptional") {
      if (event.component.attributes.formKey != this.nestedFormId) {
        await this.loadNestedForm(event.component.attributes.formKey, true);
      }
    }
    if(event?.detail?.type === 'document-required' && !this.readonly){
      let nestedForm = this.nestedForms.find(n => n.id === event.detail.formId)
      nestedForm.isValid = !this.isDocumentRequired(event.detail.isDocumentRequired )
      if(event.detail.isDocumentRequired !== nestedForm.isDocumentRequired){
        nestedForm.isDocumentRequired = event.detail.isDocumentRequired
        nestedForm.validityChanged = true
      }
      if (event.detail.isDocumentRequired !== documentRequirement.userChoiceRequired
        && event.detail.isDocumentRequired !== documentRequirement.userChoiceNonRequired &&
        event.detail.isDocumentRequired !== '') {
        this.redrawTabByFormKey(this.form.formio.components, nestedForm )
      }
    }
    if (event.type === 'open-all') {
      this.openAllIndexedPanel();
    }
    if (event.type === 'collapse-all') {
      this.collapseAllIndexedPanel();
      document
        .getElementById('app-service-header')
        .scrollIntoView();
    }
    if (event.type === 'refreshNavigation') {
      // refresh the map on this custom event
      this.onRefreshMapParameters.emit();
    }
    if (event.type === 'custom-event-hide-errors' || event.detail?.type === 'custom-event-hide-errors') {
      // hide all errors
      this.notificationsBannerService.hideAll();
    }
    if (event.detail?.type === 'force-is-final-step-button-label') {
      // check event.detail?.shouldForce
      let shouldForce = (event.detail?.shouldForce === true)? true : false;
      this.onForceIsFinalFlagLabel.emit(shouldForce);
    }
  }

  /*
    Form Submition
  */
  /**
   * Validate all forms and try to go to next step
  */
  async goToNextFormStep() {
    let isApproved = this.canGoToPrevious ? 'approved' : null
    if (!this.readonly) {
      //save the submission we are currently on
      let areNestedFormsValid = true
      if(this.nestedFormId){
        let currentNestedForm = this.nestedForms.find(n => n.id === this.nestedFormId)
        // if requirmnet of current form depends from ratio button, but that button is not selected, validate only the button
        if((currentNestedForm.isDocumentRequired === documentRequirement.userChoiceRequired || currentNestedForm.isDocumentRequired === documentRequirement.userChoiceNonRequired)
          && this.form.formio.data.consolidatingForm.data.isDocumentRequiredFromUserChoice === '') {
          this.consolidatingForm.getComponent('isDocumentRequiredFromUserChoice').setPristine(false)
          this.consolidatingForm.getComponent('isDocumentRequiredFromUserChoice').checkValidity()
          currentNestedForm.isValid = false
          this.nextErrorEvent.emit(null);
          return ;
        } else {
          this.consolidatingForm.setPristine(false)
          currentNestedForm.isValid = this.consolidatingForm.checkValidity()
        }
        currentNestedForm.hasBeenActive = true
        currentNestedForm.shouldValidateForm = true
        areNestedFormsValid = currentNestedForm.isValid
        this.updatePanelButton(currentNestedForm, !currentNestedForm.isValid);
        await this.saveNestedFormSubmition(currentNestedForm.isValid)
        this.form.formio.data.consolidatingForm.data.dontOverrideUserData = true
      } else {
        this.form.formio.data.dontOverrideUserData = true
      }
      this.form.formio.setPristine(false)

      //validate that all nested form are valid exept current one
      for (let i = 0; i < this.nestedForms.length; i++) {
        if (!this.nestedForms[i].isValid && this.nestedForms[i].id != this.nestedFormId && areNestedFormsValid) {
          areNestedFormsValid = false
          this.updatePanelButton(this.nestedForms[i], true);
          this.consolidatingForm._data.consolidatingForm.data = []
          await this.setCurrentTabInNestedForm(this.nestedForms[i]);
        } else {
          if (this.nestedForms[i].id != this.nestedFormId) {
            this.updatePanelButton(this.nestedForms[i], false);
          }
        }
      }
      if (!areNestedFormsValid) {
        this.handleSubmitValidationFail()
        return
      }

      if (this.form.formio.checkValidity()) {
        this.confirmNextStep(isApproved);
      } else {
        this.handleSubmitValidationFail()
      }
    } else {
      this.confirmNextStep(isApproved)
    }
  }
  /**
   * Go to previous step
  */
  goToPrevFormStep() :void {
    this.submitForm('rejected');
  }
  /**
   * Handle validation fail in goToNextFormStep
  */
  handleSubmitValidationFail() :void {
    if (this.nestedFormId) {
      if (this.consolidatingForm.subForm?.components) {
        this.findModifyIndexedPanels(
          this.consolidatingForm.subForm.components,
          true
        );
      }
    } else {
      this.findModifyIndexedPanels(this.form.formio.components, true);
    }
    if (this.form.formio.errors[0]) {
      this.form.formio.setPristine(false)
      this.form.formio.checkValidity()
      if (this.form.formio.errors[0].component?.id) {
        document
          .getElementById(this.form.formio.errors[0].component.id)
          .scrollIntoView();
      }
    }
    this.nextErrorEvent.emit(null);
  }

  /**
   * If service has init document, prompt the user before moving to next step
   * @param isApproved if true go to next step, else go to previous step 
  */
  confirmNextStep(isApproved = null) {
    if(this.hasInitDocument && !this.isAfterCorrection && !this.readonly) {
      const dialogRef = this.dialog.open(DialogComponent, {
        data: { title: 'IMPORTANT', body: 'CONFIRM_FORM_INIT_DOCUMENT', canProceed: true },
        width: '40em',
        panelClass: 'full-screen-modal'
      });

      dialogRef.afterClosed().subscribe((isConfirmed) => {
        if (isConfirmed) {
          this.submitForm(isApproved);
        } else {
          this.nextErrorEvent.emit(null);
        }
      })
    } else {
      this.submitForm(isApproved)
    }
  }

  /**
   * Save current form submition and complete the task in Camunda
   * @param isApproved if true go to next step, else go to previous step 
  */
  async submitForm(isApproved = null) {
    let variables = {};
    if (!this.readonly) {
      //copy form submition
      let dataCopy = JSON.parse(JSON.stringify(this.form.formio.data))
      //delete consolidating form data. It shouldn't be sent with parent data
      if (dataCopy.consolidatingForm) {
        delete dataCopy.consolidatingForm;
      }

      let formDataSubmissionKey = Utils.getFormDataSubmissionKey(this.formId);
      let submissionDataName = "submissionData_" + formDataSubmissionKey;
      let data = {};
      //remove datascr with no persistence. Done manualy due to formio bug
      for (let key in dataCopy) {
        if (this.formPersistentData.includes(key) || ['isValid','shouldValidateForm', 'documentWasSubmitted', 'isDocumentRequired'].includes(key)) {
          data[key] = dataCopy[key];
        }
      }
      variables[submissionDataName] = {
        value: { data: data },
      };
    }
    if (isApproved) {
      let formDataSubmissionKey = Utils.getFormDataSubmissionKey(this.formId);
      variables["outcome_" + formDataSubmissionKey] = {
        data: {
          outcome: isApproved
        }
      }
    }
    let parsedParams = Utils.parseQueryParamsToObject(this.formId);
    // signingCompleted is a process variable that states if there is a signee that should be awaited to sign a document before continuing
    if(parsedParams['signingCompleted'] === 'false') {

      this.onRefreshMapParameters.emit();
    } else {

      this.wrapperSubscription.add(
        this.formIoService
          .postForm(this.taskId, {
            variables: variables,
          })
          .subscribe(
            (result) => {
              this.nextSuccessEvent.emit(null);
              this.notificationsBannerService.hideAll();
            },
            (error) => {
              this.nextErrorEvent.emit(true);
            }
          )
      );
    }

  }

  /*
    Custom Panels
  */
  /**
  * Update pannels
  * @param components components in which to search for pannels
  * @param shouldValidate
  * @param preValidated: are pannels pre validated. If true all panels will be green
 */
  findModifyIndexedPanels(components = [], shouldValidate = false, preValidated = false) :void {
    this.executeFuncForEachFormComponent(components, (component, params) => {
      if (
        component?.type === "panel" &&
        !!component.customClass &&
        component.customClass.includes(panelClasses.bipClass)
      ) {
        let nestedForm = this.nestedForms.find(n => n.id === this.nestedFormId)
        this.indexedPanelsCount++
        // validation is required
        let componentInstance = this.form.formio.getComponentById(
          component.id
        );

        let bareTitle = component.title
          .replace(panelClasses.bipVerifiedIconEnabled, "")
          .replace(panelClasses.bipVerifiedIconDisabled, "")
          .replace(panelClasses.bipRemainingIcon, "")
          .replace(panelClasses.checkIcon, "");
        //always make panels collapsible and with visible label
        component.collapsible = true;
        component.hideLabel = false;
        component.idle = !component.idle; // "idle" - custom property needed only to introduce a change in the object and trigger a rerender

        if (params[0] === true) {
          // retrieve component
          let collapsed = false
          let icon = panelClasses.bipRemainingIcon
          let title = ''
          if (nestedForm) {
            //on first open only the first panel is open
            if (!nestedForm.hasBeenActive) {
              collapsed = (this.indexedPanelsCount !== 1);
            } else {
              //if form has already been validated all panels should be green with only the first open
              if (nestedForm.isValid) {
                collapsed = (this.indexedPanelsCount !== 1);
                icon = (this.readonly === true) ? panelClasses.bipVerifiedIconDisabled : panelClasses.bipVerifiedIconEnabled;
                title = panelClasses.checkIcon
              } else {
                componentInstance.setPristine(false)
                //check validity of panel and if it's valid collapse it and make it green
                if (componentInstance.checkValidity()) {
                  icon = (this.readonly === true) ? panelClasses.bipVerifiedIconDisabled : panelClasses.bipVerifiedIconEnabled;
                  collapsed = true
                  title = panelClasses.checkIcon
                }
              }
            }
          } else {
            componentInstance.setPristine(false)
            if (componentInstance.checkValidity()) {
              collapsed = true
              icon = (this.readonly === true) ? panelClasses.bipVerifiedIconDisabled : panelClasses.bipVerifiedIconEnabled;
            }
          }
          component.title = icon + bareTitle + title;
          component.collapsed = collapsed;
        } else {
          // no validation required
          component.collapsed = (this.indexedPanelsCount !== 1);
          if (params[1]) {
            component.title = panelClasses.bipVerifiedIconEnabled + bareTitle + panelClasses.checkIcon;
          }
        }
        componentInstance._collapsed = component.collapsed
        componentInstance.redraw()
      }
    },[shouldValidate, preValidated] )
  }

  /**
  * Collapse or expand all pannels
  * @param components components in which to search for pannels
  * @param collapseValue
 */
  findSetCollapseIndexedPanels(components, collapseValue = false) :void {
    this.executeFuncForEachFormComponent(components, (component, params) => {
      if (
        component?.type === "panel" &&
        !!component.customClass &&
        component.customClass.includes(panelClasses.bipClass)
      ) {
        // find the component
        let componentInstance = this.form.formio.getComponentById(
          component.id
        );
        // set the collapsed value
        componentInstance._collapsed = params[0]
        componentInstance.redraw()
      }
    }, [collapseValue])

  }

  /**
  * Expand all pannels
  * @param components components in which to search for pannels
  * @param collapseValue
 */
  openAllIndexedPanel() :void{
    if (this.consolidatingForm) {
      this.form.formio.data.consolidatingForm.data.dontOverrideUserData = true
      this.findSetCollapseIndexedPanels(this.consolidatingForm.subForm.components, false);
    } else {
      this.form.formio.data.dontOverrideUserData = true
      this.findSetCollapseIndexedPanels(this.form.formio.components, false);
    }
  }

  /**
  * Collapse all pannels
  * @param components components in which to search for pannels
  * @param collapseValue
 */
  collapseAllIndexedPanel() :void{
    if (this.consolidatingForm) {
      this.form.formio.data.consolidatingForm.data.dontOverrideUserData = true
      this.findSetCollapseIndexedPanels(this.consolidatingForm.subForm.components, true);
    } else {
      this.form.formio.data.dontOverrideUserData = true
      this.findSetCollapseIndexedPanels(this.form.formio.components, true);
    }
  }

  /*
     Nested Forms
   */
  /**
   * After initial document select, load form renderer with selected nested form
   * @param id: alias of the nested form
  */
  handleInitialSelectNestedForm(id) : void{
    // when a nested form is selected from the preview screen, set it as the selected form and load the renderer
    if (id != this.nestedFormId) {
      this.nestedFormId = id;
      this.formLoaded = true;
    }
  }

  /**
   * Fetch the data of all nested forms and update thier statuses
   * @param withData set if newnested form should be validated after load
  */
  async handleInitialNestedFormLoad(withData = true) {
    // set form context to storage so it can be fetched in the plugin for formio
    localStorage.setItem("formContext", JSON.stringify(this.formSrc.context));

    if(this.readonly === true){
      //show next button if user is on preview page
      this.shouldShowNextButton.emit(true)
    } else {
      //hide next button while user is on preview page
      this.shouldShowNextButton.emit(false);
    }
    for (let i = 0; i < this.nestedForms.length; i++) {
      if (withData) {
        let formSubmition
        try {
          // get all local variables for the nested forms and update validity and button statuses
          if(this.readonly){
            formSubmition = await this.formIoService.getFormSubmitionByBuissnessKey(this.nestedForms[i].id, this.ornNumber).toPromise()
            if (formSubmition.length){
              formSubmition = formSubmition[0].data
            }
            this.nestedForms[i].isValid = true
            this.nestedForms[i].hasBeenActive = true
            this.nestedForms[i].isDocumentRequired = formSubmition['isDocumentRequired'] ? formSubmition['isDocumentRequired']  : 'required'
            this.nestedForms[i].documentWasSubmitted = formSubmition['documentWasSubmitted']
            this.nestedForms[i].shouldValidateForm = true
            this.updatePanelButton(this.nestedForms[i], false)
          } else {
            formSubmition = await this.camundaProcessService.getLocalVariableFromProcess(this.taskId, "submissionData_" + this.nestedForms[i].path).toPromise()
            let needsValidation = true
            if(formSubmition['value']['data']['isDocumentRequired']) {
              needsValidation = this.isDocumentRequired(formSubmition['value']['data']['isDocumentRequired'])
            }
            this.nestedForms[i].isValid = formSubmition['value']['data'].isValid || !needsValidation
            this.nestedForms[i].hasBeenActive = formSubmition['value']['data'].isValid !== undefined
            this.nestedForms[i].isDocumentRequired = formSubmition['value']['data']['isDocumentRequired']  ? formSubmition['value']['data']['isDocumentRequired']  : 'required'
            this.nestedForms[i].documentWasSubmitted = formSubmition['value']['data']['documentWasSubmitted']
            this.nestedForms[i].shouldValidateForm = formSubmition['value']['data']['shouldValidateForm']
            this.updatePanelButton(this.nestedForms[i], false)
          }
        } catch {
          formSubmition = await this.formIoService.getFormSubmitionByBuissnessKey(this.nestedForms[i].id, this.ornNumber).toPromise()
          if (formSubmition.length){
            formSubmition = formSubmition[0].data
            this.nestedForms[i].hasBeenActive = true
            this.nestedForms[i].isValid = true
            this.nestedForms[i].isDocumentRequired = formSubmition['isDocumentRequired'] ? formSubmition['isDocumentRequired']  : 'required'
            this.nestedForms[i].documentWasSubmitted = formSubmition['documentWasSubmitted']
            this.nestedForms[i].shouldValidateForm = true
          }
          this.updatePanelButton(this.nestedForms[i], false)
        }
      } else {
        this.updatePanelButton(this.nestedForms[i], false)
      }
    }
    if(this.nestedForms.length === 1 && this.hasInitDocument){
      await this.handleInitialSelectNestedForm(this.nestedForms[0].id)
    }

  }

  /**
   * Load new nested form. Save the submition of the current form, update buttons and validate new form if needed
   * @param id: alias of the new nested form
   * @param withValidation set if newnested form should be validated after load
  */
  async loadNestedForm(id: string, withValidation = false) {
    let nestedForm = this.nestedForms.find(
      (form) => form.id === this.nestedFormId
    );
    //set if new nested form should be validated after load
    if(withValidation) {
      nestedForm.shouldValidateForm = true
    }
    //find selected nested form and save it's submission only if it's not an inital load
    if (!this.initalLoad) {
      await this.saveNestedFormSubmition();
    } else {
      this.initalLoad = false;
    }
    // set the current button to inactive
    this.updatePanelButton(nestedForm, false);
    nestedForm = this.nestedForms.find((form) => form.id === id);
    await this.setCurrentTabInNestedForm(nestedForm);
  }

  /**
   * Save the submition of the current nested from to camunda. Update the requirment of other documents if required
   * @param shouldResetData reset form data after submition
  */
  async saveNestedFormSubmition(shouldResetData = true) {
    if (!this.readonly) {
      //validate nested form before submit
      this.consolidatingForm.setPristine(false)
      let nestedForm = this.nestedForms.find(form => form.id === this.nestedFormId)
      if (nestedForm) {
        nestedForm.isValid = this.consolidatingForm.checkValidity()
      }
      nestedForm.hasBeenActive = true
      this.consolidatingForm._data.consolidatingForm.data['isValid'] = nestedForm.isValid;
      this.consolidatingForm._data.consolidatingForm.data['shouldValidateForm'] = nestedForm.shouldValidateForm;
      await this.setLocalVariable(this.consolidatingForm._data.consolidatingForm.data, nestedForm.id, false )

      if (shouldResetData) {
        this.consolidatingForm._data.consolidatingForm.data = []
      }
      // if any change in the nested form data changes teh requirment of another document, save it's new requirment to Camunda also.
      for (let i = 0; i < this.nestedForms.length; i++) {
        if (this.nestedForms[i]?.validityChanged || !this.nestedForms[i]?.hasBeenActive) {
          if (this.nestedForms[i].id != this.nestedFormId) {
            let submissionDataName = "submissionData_" + this.nestedForms[i].path;
            let variables = {};
            variables['modifications'] = {};
            variables['modifications'][submissionDataName] = {}
            variables['modifications'][submissionDataName]['value'] = { data: {} };
            variables['modifications'][submissionDataName]['value']['data']['isDocumentRequired'] = this.nestedForms[i].isDocumentRequired;
            await this.camundaProcessService.setLocalVariableInProcess(this.taskId, variables).toPromise()
          }
          this.nestedForms[i].validityChanged = false
        }
      }
    }
  }

  /**
   * Save local variable in Camunda
   * @param formData data to be submitted
   * @param path alias of the form to which it will be saved
   * @param shouldHideLoader should hide spinner animation
  */
  async setLocalVariable(formData: any, path: string, shouldHideLoader = false) {
    this.formPersistentData = []
    this.addNestedInputsToPercisitendData(this.consolidatingForm?.formObj?.components);
    this.addNestedInputsToPercisitendData(this.formSrc.components);
    let submissionDataName = "submissionData_" + Utils.getFormDataSubmissionKey(path);
    let variables = {};
    variables['modifications'] = {};
    variables['modifications'][submissionDataName] = {}
    let dataCopy = JSON.parse(JSON.stringify(formData))
    if (dataCopy.submissionAutocompleteAttachment) {
      delete dataCopy.submissionAutocompleteAttachment;
    }
    if (dataCopy.submissionAutocompleteJson) {
      delete dataCopy.submissionAutocompleteJson;
    }
    if (dataCopy.CreateModelFromFieldsFactory) {
      delete dataCopy.CreateModelFromFieldsFactory;
    }
    if (dataCopy.consolidatingForm) {
      delete dataCopy.consolidatingForm;
    }
    let data = {};
    for (let key in dataCopy) {
      if (this.formPersistentData.includes(key) || ['isValid','shouldValidateForm', 'documentWasSubmitted', 'isDocumentRequired'].includes(key)) {
        data[key] = dataCopy[key];
      }
    }
    data = {...data, modified: moment.now()}
    variables['modifications'][submissionDataName]['value'] = { data: data };
    await this.camundaProcessService.setLocalVariableInProcess(this.taskId, variables, shouldHideLoader).toPromise()
  }

  /**
   * Update validity and visability of panel button in formSrc Json
   * @param nestedFormComponent nested form object with validity and requirment
   * @param isSelected should botton have the active class
  */
  updatePanelButton(nestedFormComponent, isSelected: boolean) : void {
    let pannelClass = "nested-pannel";
    pannelClass += isSelected ? " selected" : "";
    pannelClass += nestedFormComponent.isValid
      ? " valid-panel"
      : " invalid-panel";
    let icon = "";
    if (nestedFormComponent.hasBeenActive) {
      icon = nestedFormComponent.isValid
        ? "fa fa-check"
        : "fa fa-exclamation-triangle";
    }
    let newComponent = {
      label: nestedFormComponent.label,
      action: "event",
      showValidations: false,
      customClass: pannelClass,
      tableView: false,
      key: nestedFormComponent.componentKey,
      attributes: {
        formKey: nestedFormComponent.id,
        initDocument: this.selectedInitDocument?.value
      },
      type: "button",
      event: "loadForm",
      input: true,
      rightIcon: icon,
      hidden: nestedFormComponent.isDocumentRequired === documentRequirement.nonRequiredFromMain || (this.nestedForms.length === 1 && this.hasInitDocument)
    };
    this.replaceComponentByKey(
      this.formSrc.components,
      nestedFormComponent.componentKey,
      newComponent
    );
  }

  /**
   * Get the submission of a nested form and set it as active in the consolidating form
   * @param nestedForm
  */
  async setCurrentTabInNestedForm(nestedForm) {
    this.nestedFormId = nestedForm.id;
    //Update the form sorce in the select
    //get the submission of the newly selected nested form
    this.updateNestedFormSrc();

    let data: any = {
      consolidatingForm: {
        data: {}
      }
    }
    //Get the submission of the newly selected nested form from either Camunda or Formio
    if (!this.readonly) {
      try {
        let variable = await this.camundaProcessService.getLocalVariableFromProcess(this.taskId, "submissionData_" + nestedForm.path).toPromise();
        data.consolidatingForm.data = this.getMostUpToDateValues(variable["value"]["data"]);
        data.consolidatingForm.data.dontOverrideUserData = true
        data.consolidatingForm.data.hasFormSubmition = true
        data.redrawNestedForm = true;
        //update the panel button to active
        this.updatePanelButton(nestedForm, true);
      } catch {
        let variable = await this.formIoService.getFormSubmitionByBuissnessKey(nestedForm.id, this.ornNumber).toPromise()
        if (variable.length) {
          data.consolidatingForm.data = this.getMostUpToDateValues(variable[0].data);
          data.consolidatingForm.data.dontOverrideUserData = true
          data.consolidatingForm.data.hasFormSubmition = true
          data.redrawNestedForm = true;
        } else {
          const formOfflineValue = this.getMostUpToDateValues();
          if (formOfflineValue) {
            data.consolidatingForm.data = formOfflineValue;
          }
          data.consolidatingForm.data.hasFormSubmition = false
        }
        //update the panel button to active
        this.updatePanelButton(nestedForm, true);
      }
    } else {
      let variable = await this.formIoService.getFormSubmitionByBuissnessKey(nestedForm.id, this.ornNumber).toPromise()
      if (variable.length) {
        data.consolidatingForm.data = this.getMostUpToDateValues(variable[0].data);
        data.consolidatingForm.data.isDataPrefilledFromBackend = true
        data.consolidatingForm.data.hasFormSubmition = true
        data.redrawNestedForm = true;
      } else {
        const formOfflineValue = this.getMostUpToDateValues();
        if (formOfflineValue) {
          data.consolidatingForm.data = formOfflineValue;
        }
        data.consolidatingForm.data.hasFormSubmition = false
      }
      //update the panel button to active
      this.updatePanelButton(nestedForm, true);
    }

    //reset data before tab switch
    if (this.consolidatingForm._data.consolidatingForm) {
      this.consolidatingForm._data.consolidatingForm.data = []
      this.form.formio.data.consolidatingForm.data = []
      this.form.formio.data.consolidatingForm.data = data
    }
    if(this.hasInitDocument){
      data.initDocument = this.selectedInitDocument.value
    }

    this.refreshForm.emit({
      form: this.formSrc,
      submission: {
        data: data,
      },
    });
  }

  /**
   * Change form alias in the nested form component in the consolidating form
  */
  updateNestedFormSrc() : void{
    this.executeFuncForEachFormComponent(this.formSrc.components,(component) => {
      if (component.type === "form") {
        component.path = this.nestedFormId;
        component.form = null;
        // update the form context of the nested form with the correct alias
        let context = JSON.parse(localStorage.getItem("formContext"));
        let nestedForm = this.nestedForms.find(n => n.id === this.nestedFormId)
        context.isDocumentRequired = nestedForm.isDocumentRequired
        context.formAlias = this.getFormAlias(this.nestedFormId);
        context.parrentForm = this.getFormAlias();
        localStorage.setItem("formContext", JSON.stringify(context));
      }
    })
  }

  /**
   * Redraw tabs in a nested form without redrawing the whole form. Change button icon and visability
   * @param components
   * @param nestedForm nested form object with validity and document requirment
  */
  redrawTabByFormKey(components, nestedForm) : void{
    this.executeFuncForEachFormComponent(components,(component, params) => {
      if (component?.attributes?.formKey === params[0].id && component.id) {
        let button = this.form.formio.getComponentById(component.id)
        let icon = "";
        if (params[0].isDocumentRequired === documentRequirement.userChoiceRequired){
          this.consolidatingForm.setPristine(false)
          params[0].isValid = this.consolidatingForm.checkValidity()
        }
        icon = params[0].isValid
          ? "fa fa-check"
          : "fa fa-exclamation-triangle";
        button.component.rightIcon = icon
        button.component.hidden = params[0].isDocumentRequired === documentRequirement.nonRequiredFromMain
        this.replaceComponentByKey(
          this.formSrc.components,
          button.component.key,
          button.component
        )
        // due to bug in formio, if you want to hide button and it's parrent column, you have to redraw the columns component also
        if(params[0].isDocumentRequired === documentRequirement.nonRequiredFromMain || params[0].isDocumentRequired === documentRequirement.requiredFromMain){
          let columns = button.parent
          columns.component.columns.forEach(column => {
            if( column.components[0].key === button.component.key) {
              column.currentWidth = params[0].isDocumentRequired === documentRequirement.nonRequiredFromMain ? 0 : column.width
            }
          })
          columns.redraw()
        } else {
          button.redraw()
        }
      }
    }, [nestedForm])
  }
  /**
   * On init document change, delete all local variables, reset the form and seve new selected document
  */
  async handleInitDocumentChange() {
    if (this.selectedInitDocument.value) {
      let variables = {
        modifications: {
          initDocumentSelected: {
            value: {
              data : {
                initDocumentSelected : this.selectedInitDocument.value
              }
            }
          }
        }
      }
      this.form = null;
      this.formLoaded = false
      this.initialLoadNested = true
      this.initalLoad = true
      this.nestedFormId = ''
      this.nestedForms = []
      this.consolidatingForm = null
      this.formSubmition ={data: {hasFormSubmition : false}}
      this.formatFormComponentsJSON();
      if(!this.wasInitDocumentPreselected){
        await this.camundaProcessService.deleteAllLocalVariables(this.taskId).toPromise()
        await this.camundaProcessService.setLocalVariableInProcess(this.taskId, variables).toPromise()
        this.shouldPromptBeforeLeaveEvent.next(false);
        window.location.reload();
      }
      await this.handleInitialNestedFormLoad(this.wasInitDocumentPreselected);
      this.wasInitDocumentPreselected = false
    }
  }
  /*
    Helper functions
  */
  /**
   * Recursively cycle through a form or form element and execute a function on every component and it's children
   * @param components
   * @param callback function that is beeing called on each component
   * @param params params that can be sent to the callback function
  */
  executeFuncForEachFormComponent(components, callback : Function, params = []) : void{
    if(components) {
      components.forEach((parentComponent) => {
        let component = !!parentComponent.component && parentComponent.component.type !== 'columns'
          ? parentComponent.component
          : parentComponent;
        callback(component, params)

        if (component.components) {
          this.executeFuncForEachFormComponent(component.components, callback, params);
        }
        if (component.columns) {
          component.columns.forEach((column) => {
            this.executeFuncForEachFormComponent(column.components, callback, params);
          });
        }
        if (component.rows) {
          if (component.rows.length) {
            component.rows.forEach((row) => {
              row.forEach((element) => {
                this.executeFuncForEachFormComponent(element.components, callback, params);
              });
            });
          }
        }
      });
    }
  }

  /**
   * Initial formating of the form JSON.
  */
  formatFormComponentsJSON() {
    this.executeFuncForEachFormComponent(this.formSrc.components,(component) => {
      if (this.readonly) {
        if (!(component.type === 'form' || component?.attributes?.collapseAllParentComponent || component?.event === 'open-all' || component?.event === 'collapse-all')) {
          component['disabled'] = true;
        }
      }
      if(component.key === 'initDocument' && !this.selectedInitDocument?.value){
        this.hasInitDocument = true
        this.initDocuments = component.data.values
        this.selectedInitDocument = new FormControl('', Validators.required);
        this.nestedForms = []
      }
      if (component.event) {
        if(component.event === 'loadForm' && (!this.hasInitDocument || component.attributes.initDocument === this.selectedInitDocument.value)){
          let isValid = false
          component.attributes.isDocumentRequired = component.attributes.isDocumentRequired ? component.attributes.isDocumentRequired : 'required'
          isValid = !this.isDocumentRequired(component.attributes.isDocumentRequired)
          component['disabled'] = false;
          //push all nested forms in new object for easier use later
          this.nestedForms.push({
            id: component.attributes.formKey,
            isValid: isValid,
            label: component.label,
            hasBeenActive: false,
            componentKey: component.key,
            path: Utils.getFormDataSubmissionKey(component.attributes.formKey),
            isDocumentRequired: component.attributes.isDocumentRequired,
            shouldValidateForm: false,
            documentWasSubmitted : false,
          });
        }
      }
      // Initial manipulation of a panel with bullet-indexed-panel class
      if (
        component.type === "panel" &&
        !!component.customClass &&
        component.customClass.includes(panelClasses.bipClass)
      ) {
        this.indexedPanelsCount++
        // All panels here are required to be collapsable here
        if (component.collapsible === false) {
          component.collapsible = true;
        }
        // Manipulate hidden title panels
        if (component.hideLabel === true) {
          component.hideLabel = false; // show title
          component.title = ""; // empty the title, deleted title property defaults to "Panel"
        }
        component.collapsed = this.indexedPanelsCount !== 1
        component.title = (this.readonly ? panelClasses.bipVerifiedIconDisabled : panelClasses.bipRemainingIcon) + component.title;
      }
      if (component.type === "form") {
        component.form = null;
        component.path = null;
      }
      if (component.input === true) {
        if (component.persistent !== false) {
          this.formPersistentData.push(component.key.split('.')[0]);
        }
      }
    })
  }

  /**
   * Add nested inputs to percisitend data to be sent to Camunda
  */
  addNestedInputsToPercisitendData(components) {
    this.executeFuncForEachFormComponent(components,(component) => {
      if (component.input === true) {
        if (component.persistent !== false) {
          this.formPersistentData.push(component.key.split('.')[0]);
        }
      }
    })
  }

  /**
   * Find a component by key and replace it with different components
   * @param components where to search for the component
   * @param key key of the target component
   * @param value new component
  */
  replaceComponentByKey(components, key, value) : void {
    this.executeFuncForEachFormComponent(components,(component, params) => {
      if (component.key === params[0]) {
        Object.assign(component, params[1])
      }
    }, [key, value])
  }

  /**
   * Check if a nested document is required
  */
  isDocumentRequired(isDocumentRequired : string) : boolean{
    return isDocumentRequired === documentRequirement.required
      || isDocumentRequired === documentRequirement.requiredFromMain
      || isDocumentRequired === documentRequirement.userChoiceRequired
      || !isDocumentRequired
  }

  /**
   * Get form alias (api path) without query params
   * @param id id of the nested form. If no id is provided the function will return the id of teh consolidating form
  */
  getFormAlias(id: string = null) : string{
    let formId = id ? id : this.formId;
    return formId.split("?")[0];
  }
  /**
   * Disable or eneble all fields in a form when users internet access changes
   * @param isOnline does the user have internet access
  */
  async updateOnlineStatus(isOnline) {
    this.isOnline = isOnline
    if (isOnline) {
      this.refreshForm.emit({
        form: this.formSrc
      });

      let mostUpToDateValue = this.getMostUpToDateValues();

      let formData = mostUpToDateValue
      let formId = this.getSimpleOrNestedFormId

      await this.setLocalVariable(formData, formId, true);
    } else {
      if (this.formLoaded) {
        this.saveFormValueForLaterUse();
        this.disableFieldsInOfflineMode();
      }
    }
  }

  /**
   * Save form data in local storage when we are offline every 5 min
   * When the user is back online we are going to use this value to prefill the data
   * @private
   */
  private saveFormValueForLaterUse() {
    let localeStorageKey = this.genLocalStorageKeyOfflineForm();
    const data = {
      modified: new Date().getTime(),
      formValue: this.getSimpleOrNestedFormData
    };

    localStorage.setItem(localeStorageKey, JSON.stringify(data))
  }

  /**
   * Check if we are in consolidating form or normal
   * one and pass the proper components to be disalbed
   * @private
   */
  private disableFieldsInOfflineMode(): void {
    // this is for consolidating forms
    let disabledFormSrc = JSON.parse(JSON.stringify(this.formSrc));
    this.executeFuncForEachFormComponent(disabledFormSrc.components, (component) => {
      component.disabled = true
      component.logic = []
    })

    this.refreshForm.emit({
      form: disabledFormSrc
    });
  }

  private genLocalStorageKeyOfflineForm(): string {
    return 'offlineFormValue' + this.ornNumber + this.formId + (this.nestedFormId || '');
  }

  private getFormValueFromLocaleStorage(): { formValue: any, modified: number } | null {
    try {
      return JSON.parse(localStorage.getItem(this.genLocalStorageKeyOfflineForm()));
    } catch (err) {
      console.error(err);
      return null;
    }
  }

  /**
   * Checks if the value from the server or the one in local storage is the most recent one
   * @param dataFromServer
   * @private
   */
  private getMostUpToDateValues(dataFromServer?: any): any {
    const dateFromLocaleStorage = this.getFormValueFromLocaleStorage();

    if (!dateFromLocaleStorage) {
      return dataFromServer;
    }

    const { formValue, modified } = dateFromLocaleStorage;
    if (!dataFromServer && formValue) {
      return formValue;
    }
    const lastFormModification = dataFromServer?.modified;

    if (lastFormModification >= modified) {
      localStorage.removeItem(this.genLocalStorageKeyOfflineForm())
      return dataFromServer;
    }

    return formValue;
  }
}