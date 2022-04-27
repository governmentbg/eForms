import { Component, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { EventEmitter } from "@angular/core";
import { Formio } from "formiojs";
import { Observable, Subscription } from "rxjs";
import { FormIoService } from "src/app/core/services/form-io.service";
import { environment } from "src/environments/environment";
import * as bg from "../../../../assets/i18n/bg.json";
import { OidcSecurityService } from "angular-auth-oidc-client";
import { FileToSign } from "src/app/core/types/file-to-sign";
import { SignService } from "src/app/core/services/sign.service";
import { NotificationsBannerService } from "src/app/core/services/notifications-banner.service";
import { NotificationBarType } from "../../../shared/components/notifications-banner/notification-banner.model";
import { TranslateService } from "@ngx-translate/core";
import { UserProfileService } from "src/app/core/services/user-profile.service";
import { DAEFService } from "src/app/core/services/daef-service.service";
import { MatDialog } from "@angular/material/dialog";
import { DownloadNexuModalComponent } from "src/app/features/download-nexu-modal/download-nexu-modal.component";
import { HttpClient } from "@angular/common/http";
import { signMethods } from "src/app/core/types/sign-methods";
import { DeepLinkService } from "src/app/core/services/deep-link.service";
import { CamundaProcessService } from "src/app/core/services/camunda-process.service";
import { ActivatedRoute, Router } from "@angular/router";
import * as moment from "moment";

@Component({
  selector: "app-io-form-builder",
  templateUrl: "./io-form-builder.component.html",
  styleUrls: ["./io-form-builder.component.scss"],
})
export class IoFormBuilderComponent implements OnInit, OnDestroy {
  private wrapperSubscription = new Subscription();
  @Input() formSrc;
  @Input() formId;
  @Input() projectId;
  @Input() nextEvent: Observable<void>;
  @Input() prevEvent: Observable<void>;
  @Input() taskId;
  @Input() ornNumber;
  @Input() canGoToPrevious;
  @Input() skipGetFormSubmition = false;
  @Output() nextSuccessEvent = new EventEmitter();
  @Output() nextErrorEvent = new EventEmitter();
  @Output() shouldShowNextButton = new EventEmitter();
  nextSubscription;
  prevSubscription;
  form;
  errors = [];
  decisions = [];
  bgTranslation;
  isSigning = false;
  signedButtons = [];
  currentSignButton;
  totalDocumentsToBeSigned = 0;
  fileToSign: FileToSign;
  window: any;
  certificateData;
  payloadForSign;
  signingDate;
  refreshForm;
  hasCheckedForNexU = false;
  dataSrcToRemove = [];
  arId: string;
  signCallbackId = [];
  documentsToBeSigned: number;
  filesToSign = [];
  signMethods = [];
  formLoaded = false;
  nestedForms = [];
  nestedFormId: string;
  initialNestedFormSubmition = {};
  initalLoad = true;
  indexedPanelsCount: number = 0;
  private bipClass = "bullet-indexed-panel";
  private verifiedClass = "verified";
  private bipVerifiedIconEnabled = `<i class="fa ${this.bipClass}-icon ${this.verifiedClass} enabled"></i>`;
  private bipVerifiedIconDisabled = `<i class="fa ${this.bipClass}-icon ${this.verifiedClass} disabled"></i>`;
  private bipRemainingIcon = `<i class="fa ${this.bipClass}-icon remaining"></i>`;
  private checkIcon = '<i class="fa fa-check"></i>';
  formAfterNestedChange = false;
  consolidatingForm
  readonly = false
  initialLoadNested = true
  pendingDocumentsButtonIds = []
  private showErrors = false
  formSubmition :any  = {data : {}}
  hasFormSubmition = false;

  constructor(
    private formIoService: FormIoService,
    public oidcSecurityService: OidcSecurityService,
    private signService: SignService,
    private notificationsBannerService: NotificationsBannerService,
    private translateService: TranslateService,
    private userProfileService: UserProfileService,
    private daefService: DAEFService,
    public dialog: MatDialog,
    private http: HttpClient,
    private deepLinkService: DeepLinkService,
    private router: Router,
    private camundaProcessService: CamundaProcessService,
    private route: ActivatedRoute
  ) {}

  async ngOnInit() {
    this.route.queryParams.subscribe(params => {

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
      let baseUrl = `${environment.apiUrl}/project/${this.projectId}`;
      Formio.setBaseUrl(baseUrl);
      //remove form context from storege so no mixups happen with nested forms
      localStorage.removeItem("formContext");
      this.userProfileService.subscribe( async userProfile => {
        if(!userProfile) {
          return ;
        }
        this.formSrc.context = {
          apiUrl: environment.apiUrl,
          clientId: environment.clientId,
          projectId: this.projectId,
          formioBaseProject: environment.formioBaseProject,
          businessKey: this.ornNumber.substring(0, 11),
          accessToken: `Bearer ${this.oidcSecurityService.getToken()}`,
          selectedProfile: this.userProfileService.selectedProfile,
          arId: this.arId,
          formAlias: this.getFormAlias(),
          personIdentifier: userProfile?.personIdentifier,
          signMethods: this.signMethods,
          currentRoute: this.router.url
        };
        if (this.formId.includes("?")) {
          let querryParams = this.formId.split("?").pop().split("&");
          querryParams.forEach((param) => {
            let paramKeyAndValue = param.split("=");
            this.formSrc.context[paramKeyAndValue[0]] = paramKeyAndValue[1];
            if(paramKeyAndValue[0] === 'readOnly' && paramKeyAndValue[1] === 'true') {
              this.readonly = true
            }
          });
        }
        this.formatFormComponentsJSON(this.formSrc.components);
        this.nextSubscription = this.wrapperSubscription.add(
          this.nextEvent.subscribe(() => this.goToNextFormStep())
        );
        this.prevSubscription = this.wrapperSubscription.add(
          this.prevEvent.subscribe(() => this.goToPrevFormStep())
        );
        let fetchingSignMethods = false
        if (this.totalDocumentsToBeSigned > 0) {
          fetchingSignMethods = true
          let borica = {};
          try {
            borica = await this.signService.getSignMethodBorica(
              userProfile?.personIdentifier.split("-")[1]
            );
          } catch {
            borica["responseCode"] = "ERROR";
          }
          if (borica["responseCode"] === "OK") {
            this.signMethods.push(signMethods.borica);
          }
          let evrotrust = {};
          try {
            evrotrust = await this.signService.getSignMethodEvrotrust(userProfile?.personIdentifier.split('-')[1])
            fetchingSignMethods = false
          } catch {
            evrotrust["isReadyToSign"] = false;
            fetchingSignMethods = false
          }
          if (evrotrust["isReadyToSign"]) {
            this.signMethods.push(signMethods.evrotrust);
          }
        }
        if (this.nestedForms.length) {
          //check if all nested forms were clicked and are valid
          await this.handleInitialNestedFormLoad();
        }
        if(!this.nestedForms.length && !this.skipGetFormSubmition) {
          try {
            let variable = await this.camundaProcessService.getLocalVariableFromProcess(this.taskId, "submissionData_" + this.getFormDataSubmissionKey(this.formId)).toPromise();
            let formSubmition = variable["value"]["data"]
            formSubmition.isDataPrefilledFromBackend = true
            this.formSubmition.data = formSubmition
          } catch {
            let formSubmition = await this.formIoService.getFormSubmitionByBuissnessKey(this.getFormAlias(), this.ornNumber).toPromise()
            if(formSubmition.length){
            formSubmition[0].data.dontOverrideUserData = true
              if(this.readonly) {
              formSubmition[0].data.isDataPrefilledFromBackend = true
              }
              this.formSubmition.data = formSubmition[0].data
              this.formSubmition.data.hasFormSubmition = true;
            } else {
              this.formSubmition.data.hasFormSubmition = false;
            }
          }
        }

        //if it's a consolidating form, don't load renderer, but preview page first
        this.formLoaded = this.nestedForms.length == 0 && !fetchingSignMethods && !!userProfile
      })
    });

  }

  ngOnDestroy() {
    this.wrapperSubscription.unsubscribe();
  }

  async handleFormReady(form: any) {
    this.form = form;
    this.form.formio.addLanguage("bg", bg.FORMIO);
    this.form.formio.language = "bg";
    // load initialy selected nested form
    if (this.nestedFormId) {
      //after nested form is loaded, show next button again
      this.shouldShowNextButton.emit(true);
    }
    if(!this.nestedFormId && !this.skipGetFormSubmition) {
      this.indexedPanelsCount = 0
      this.findModifyIndexedPanels(this.form.formio.components, false, this.formSubmition.data.hasFormSubmition)
    }
  }

  async handleFormChange(form: any) {
    if(form?.changed?.instance?.component?.type === 'file' && !this.readonly) {
      let formData = this.nestedFormId ? this.form.formio.data.consolidatingForm.data : this.form.formio.data
      let formId = this.nestedFormId ? this.nestedFormId : this.formId 
      await this.setLocalVariable(formData, formId )
    }
    if(form.data?.redrawNestedForm) {
        this.formatFormComponentsJSON(this.consolidatingForm.formObj.components);
        this.indexedPanelsCount = 0
        // validate all indexed panels
        this.findModifyIndexedPanels(
          this.consolidatingForm.subForm.components,
          true
        );
        this.formAfterNestedChange = false;
        if(this.showErrors){
          this.form.formio.setPristine(false)
          this.form.formio.checkValidity()
          this.showErrors = false
          if(this.form.formio.errors[0]) {
            if(this.form.formio.errors[0].component?.id){
              document
              .getElementById(this.form.formio.errors[0].component.id)
              .scrollIntoView();
              this.findModifyIndexedPanels(this.form.formio.components, true);
            }
          }
        }
    }
    if (this.totalDocumentsToBeSigned > 0) {
      try {
        if (
          form.changed.component.key == "eSignMethod" &&
          form.changed.value.value == "kep"
        ) {
          this.http
            .get(`http://localhost:9795/nexu.js`, { responseType: "text" })
            .subscribe(
              (result) => {
                // Ignore if NexU is installed
              },
              (error) => {
                this.showDialog();
              }
            );
        }
      } catch (error) {
        // Ignore error if some of the keys doesn't exist
      }
    }
  }

  async handleFormRender(event) {
    //handle errors for non-nested forms after panel status was updated and new formSrc was emited
    if(this.showErrors && !this.nestedFormId){
      this.form.formio.setPristine(false)
      this.form.formio.checkValidity()
      this.showErrors = false
      if(this.form.formio.errors[0]) {
        if(this.form.formio.errors[0].component?.id){
          document
          .getElementById(this.form.formio.errors[0].component.id)
          .scrollIntoView();
          this.findModifyIndexedPanels(this.form.formio.components, true);
        }
      }
    }
    if(this.form){
      this.consolidatingForm = this.form.formio.getComponent("consolidatingForm");
      if (this.consolidatingForm && this.initialLoadNested) {
        this.initialLoadNested = false
        await this.loadNestedForm(this.nestedFormId);
        return ;
      } else if(this.initialLoadNested){
        this.initialLoadNested = false
      }

      // init the global counter to zero
      this.indexedPanelsCount = 0;
    }
  }

  findModifyIndexedPanels(components = [], shouldValidate = false, preValidated = false) {
    if (components) {
      let nestedForm = this.nestedForms.find(n => n.id === this.nestedFormId)

      components.forEach((parentComponent) => {
        let component = !!parentComponent.component
          ? parentComponent.component
          : parentComponent;

        if (
          component?.type === "panel" &&
          !!component.customClass &&
          component.customClass.includes(this.bipClass)
        ) {
          this.indexedPanelsCount++
          // validation is required
          let componentInstance = this.form.formio.getComponentById(
            component.id
          ); 
          if (shouldValidate === true) {
            // retrieve component
            let collapsed = false
            let icon = this.bipRemainingIcon
            let componentValidity = false
            let title = ''
            if(nestedForm) {
              //on first open only the first panel is open
              if(!nestedForm.hasBeenActive){
                collapsed = (this.indexedPanelsCount !== 1);
              } else {
                //if form has already been validated all panels should be green with only the first open
                if (nestedForm.isValid) {
                  collapsed = (this.indexedPanelsCount !== 1);
                  icon = (this.readonly === true)? this.bipVerifiedIconDisabled : this.bipVerifiedIconEnabled;
                  title = this.checkIcon
                } else {
                  componentInstance.setPristine(false)
                  componentValidity = componentInstance.checkValidity();
                  //check validity of panel and if it's valid collapse it and make it green
                  if(componentValidity) {
                    icon = (this.readonly === true)? this.bipVerifiedIconDisabled : this.bipVerifiedIconEnabled;
                    collapsed = true
                    title = this.checkIcon
                  }
                }
              }
            } else {
              componentInstance.setPristine(false)
              componentValidity = componentInstance.checkValidity();
              if (componentValidity) {
                collapsed = true
                icon = (this.readonly === true)? this.bipVerifiedIconDisabled : this.bipVerifiedIconEnabled;
              }
            }
            if (component.hideLabel === true) {
              component.hideLabel = false; // show title
              component.title = ""; // empty the title, deleted title property defaults to "Panel"
            }
            let bareTitle = component.title
            .replace(this.bipVerifiedIconEnabled, "")
            .replace(this.bipVerifiedIconDisabled, "")
            .replace(this.bipRemainingIcon, "")
            .replace(this.checkIcon, "");

            component.title = icon + bareTitle + title;
            component.collapsed = collapsed;
            component.collapsible = true;
            component.idle = !component.idle; // "idle" - custom property needed only to introduce a change in the object and trigger a rerender
          } else {
            // no validation required
            component.collapsible = true;
            component.collapsed = (this.indexedPanelsCount !== 1);
            component.idle = !component.idle; // "idle" - custom property needed only to introduce a change in the object and trigger a rerender
            if(preValidated) {
              let bareTitle = component.title
              .replace(this.bipVerifiedIconEnabled, "")
              .replace(this.bipVerifiedIconDisabled, "")
              .replace(this.bipRemainingIcon, "")
              .replace(this.checkIcon, "");

            component.title = this.bipVerifiedIconEnabled + bareTitle + this.checkIcon;
            }
          }
          componentInstance._collapsed = component.collapsed
          componentInstance.redraw()
          if (shouldValidate) {
            componentInstance.setPristine(false)
            componentInstance.checkValidity();
          }
        }
        // recursion down the rabbit hole
        if (component?.components) {
          this.findModifyIndexedPanels(component.components, shouldValidate, preValidated);
        }
        if (component.columns) {
          component.columns.forEach((column) => {
            this.findModifyIndexedPanels(column.components,shouldValidate, preValidated);
          });
        }
        if (component.rows) {
          if (component.rows.length) {
            component.rows.forEach((row) => {
              row.forEach((element) => {
                this.findModifyIndexedPanels(element.components,shouldValidate, preValidated);
              });
            });
          }
        }
      });
    }
  }

  findSetCollapseIndexedPanels(components, collapseValue = false){

    if(components){

      components.forEach((parentComponent) => {
        // formio component structure may vary when the type is "component" instead of "panel"
        let component = !!parentComponent.component
            ? parentComponent.component
            : parentComponent;

        if (
            component?.type === "panel" &&
            !!component.customClass &&
            component.customClass.includes(this.bipClass)
        ) {
          // find the component
          let componentInstance = this.form.formio.getComponentById(
              component.id
          );
          // set the collapsed value
          componentInstance._collapsed = collapseValue
          componentInstance.redraw()
        }
        if (component?.components) {
          this.findSetCollapseIndexedPanels(component.components, collapseValue);
        }
        if (component.columns) {
          component.columns.forEach((column) => {
            this.findSetCollapseIndexedPanels(column.components, collapseValue);
          });
        }
        if (component.rows) {
          if (component.rows.length) {
            component.rows.forEach((row) => {
              row.forEach((element) => {
                this.findSetCollapseIndexedPanels(element.components, collapseValue);
              });
            });
          }
        }
      });

    }

  }

  openAllIndexedPanel() {
    if (this.consolidatingForm) {
      this.form.formio.data.consolidatingForm.data.dontOverrideUserData = true
      this.findSetCollapseIndexedPanels(this.consolidatingForm.subForm.components, false);
    } else {
      this.form.formio.data.dontOverrideUserData = true
      this.findSetCollapseIndexedPanels(this.form.formio.components, false);
    }
  }

  collapseAllIndexedPanel() {
    if (this.consolidatingForm) {
      this.form.formio.data.consolidatingForm.data.dontOverrideUserData = true
      this.findSetCollapseIndexedPanels(this.consolidatingForm.subForm.components,true);
    } else {
      this.form.formio.data.dontOverrideUserData = true
      this.findSetCollapseIndexedPanels(this.form.formio.components, true);
    }
  }

  showDialog() {
    const dialogRef = this.dialog.open(DownloadNexuModalComponent, {
      data: { title: "IMPORTANT", body: "CANCEL_SERVICE", canProceed: true },
    });
    dialogRef.afterClosed().subscribe((result) => {});
  }

  async goToNextFormStep() {
    let isApproved = this.canGoToPrevious ? 'approved' : null
    if(!this.readonly) {   
        this.form.formio.setPristine(false)
        //save the submission we are currently on
        let areNestedFormsValid = true
        if(this.nestedFormId){
          let currentNestedForm = this.nestedForms.find(n => n.id === this.nestedFormId)
          this.consolidatingForm.setPristine(false)
          currentNestedForm.isValid = this.consolidatingForm.checkValidity()
          currentNestedForm.hasBeenActive = true
          areNestedFormsValid = currentNestedForm.isValid
          this.updatePanelButton(currentNestedForm, !currentNestedForm.isValid);
          await this.saveNestedFormSubmition(false)
          this.form.formio.data.consolidatingForm.data.dontOverrideUserData = true
        } else {
          this.form.formio.data.dontOverrideUserData = true
        }
        // always refresh the form
        this.errors = [];
        //validate that all document were signed
        if (document.getElementsByClassName('sign-button').length > 0
        || document.getElementsByClassName('waiting-for-status-check').length > 0
        
        ) {
        this.notificationsBannerService.show({
            message: "ERRORS.PLEASE_SIGN_ALL_DOCUMENTS",
            type: NotificationBarType.Error,
        });
        this.handleSubmitValidationFail()
        return;
        }

        //validate that all nested form are valid exept current one!!!
        for (let i = 0; i < this.nestedForms.length; i++) {
            if (!this.nestedForms[i].isValid && this.nestedForms[i].id != this.nestedFormId && areNestedFormsValid) {
            areNestedFormsValid = false
            this.updatePanelButton(this.nestedForms[i], true);
            this.consolidatingForm._data.consolidatingForm.data = []
            await this.setCurrentTabInNestedForm(this.nestedForms[i]);
            } else {
              if(this.nestedForms[i].id != this.nestedFormId){
                this.updatePanelButton(this.nestedForms[i], false);
              }
            }
        }
        if (!areNestedFormsValid) {
          this.handleSubmitValidationFail()
          return
        }

        //validate that attached filenames are unique
        if (
        this.form.formio.data &&
        !!this.form.formio.data.fileAttachmentMandatory &&
        !!this.form.formio.data.fileAttachmentMandatory[0] &&
        !!this.form.formio.data.fileAttachmentOptional &&
        !!this.form.formio.data.fileAttachmentOptional[0]
        ) {
            if (
                this.form.formio.data.fileAttachmentMandatory[0].originalName ===
                this.form.formio.data.fileAttachmentOptional[0].originalName
            ) {
                this.notificationsBannerService.show({
                message: "ERRORS.IDENTICAL_ATTACHED_DOCUMENTS",
                type: NotificationBarType.Error,
                });
            }
        }

        if (this.formSrc.display === "wizard") {
            for (let i = 0; i < this.form.formio.pages.length; i++) {
                if (!this.form.formio.pages[i].checkValidity()) {
                    this.form.formio.setPage(i);
                    this.handleSubmitValidationFail()
                    return ;
                }
            }
            this.submitForm(isApproved);
        } else {
            if (this.form.formio.checkValidity()) {
            this.submitForm(isApproved);
            } else {
            this.handleSubmitValidationFail()
            }
        }
    } else {
      this.submitForm(isApproved)
    }
  }

  goToPrevFormStep() {
    this.submitForm('rejected');
  }

  handleSubmitValidationFail(){
    this.showErrors = true
    if (this.nestedFormId) {
      if(this.consolidatingForm.subForm?.components) {
        this.findModifyIndexedPanels(
          this.consolidatingForm.subForm.components,
          true
        );
      }
    }
    else {
      this.findModifyIndexedPanels(this.form.formio.components, true);
    }
    if(this.form.formio.errors[0]) {
      if(this.form.formio.errors[0].component?.id){
        document
        .getElementById(this.form.formio.errors[0].component.id)
        .scrollIntoView();
      }
    }
    this.nextErrorEvent.emit(null);
  }

  async submitForm(isApproved = null) {
    let variables = {};
    if(!this.readonly) {
      let dataCopy = JSON.parse(JSON.stringify(this.form.formio.data))
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
      let formDataSubmissionKey = this.getFormDataSubmissionKey(this.formId);
      let submissionDataName = "submissionData_" + formDataSubmissionKey;
      let data = {};
      for (let key in dataCopy) {
        if (!this.dataSrcToRemove.includes(key)) {
          data[key] = dataCopy[key];
        }
      }
      variables[submissionDataName] = {
        value: { data: data },
      };
      if (this.decisions.length) {
        let desicionData = {
          value: {},
          type: "json",
        };
        this.decisions.forEach((desicion) => {
          desicionData.value[desicion] = this.form.formio.data[desicion];
        });
        variables["desicion_" + this.formId.replace("formio:/", "")] =
          desicionData;
      }
    }
    if(isApproved){
      let formDataSubmissionKey = this.getFormDataSubmissionKey(this.formId);
      variables["outcome_" + formDataSubmissionKey] = {
        data: {
          outcome: isApproved
        }
      }
    }
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
            this.nextErrorEvent.emit(null);
          }
        )
    );
  }

  formatFormComponentsJSON(components) {
    components.forEach((component) => {
      if(this.readonly){
        if(!(component.type === 'form' || component?.attributes?.collapseAllParentComponent || component?.event === 'open-all' || component?.event === 'collapse-all')){
          component['disabled'] = true;
        }
      }
      //Get the number of buttons with sign event. It's equal to the number of documents that need to be sign
      if (component.event) {
        if (component.event === "sign") {
          if (!this.hasCheckedForNexU) {
            this.signService.getNexuJS();
          }
          this.hasCheckedForNexU = true;
          this.totalDocumentsToBeSigned++;
        }
        if(component.event === 'loadForm' || component.event === 'loadFormOptional'){
          component['disabled'] = false;
          //push all nested forms in new object for easier use later
          this.nestedForms.push({
            id: component.attributes.formKey,
            isValid: component.event === "loadFormOptional",
            label: component.label,
            hasBeenActive: false,
            componentKey: component.key,
            path: this.getFormDataSubmissionKey(component.attributes.formKey),
          });
        }

      }

      // Initial manipulation of a panel with bullet-indexed-panel class
      if (
        component.type === "panel" &&
        !!component.customClass &&
        component.customClass.includes(this.bipClass)
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
        component.title = (this.readonly ? this.bipVerifiedIconDisabled : this.bipRemainingIcon) + component.title;
      }

      if (component.type === "form") {
        component.form = null;
      }

      if (component.input) {
        if (component.attributes) {
          if (component.attributes["decision"]) {
            this.decisions.push(component.key);
          }
        }
      }
      if (component.components) {
        this.formatFormComponentsJSON(component.components);
      }
      if (component.columns) {
        component.columns.forEach((column) => {
          this.formatFormComponentsJSON(column.components);
        });
      }
      if (component.rows) {
        if (component.rows.length) {
          component.rows.forEach((row) => {
            row.forEach((element) => {
              this.formatFormComponentsJSON(element.components);
            });
          });
        }
      }
      if (component.type === "datasource") {
        if (component.persistent === false) {
          this.dataSrcToRemove.push(component.key);
        }
      }
    });
  }

  async handleInitialNestedFormLoad() {
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
          this.updatePanelButton(this.nestedForms[i], false)
        } else {
          formSubmition = await this.camundaProcessService.getLocalVariableFromProcess(this.taskId, "submissionData_" + this.nestedForms[i].path).toPromise()
          this.nestedForms[i].isValid = formSubmition['value']['data'].isValid
          this.nestedForms[i].hasBeenActive = formSubmition['value']['data'].isValid !== undefined 
          this.updatePanelButton(this.nestedForms[i], false)
        }
      } catch {
        formSubmition = await this.formIoService.getFormSubmitionByBuissnessKey(this.nestedForms[i].id, this.ornNumber).toPromise()
        if (formSubmition.length){
          formSubmition = formSubmition[0].data
          this.nestedForms[i].hasBeenActive = true
          this.nestedForms[i].isValid = true
          this.updatePanelButton(this.nestedForms[i], false)
        }
      }
    }
  }

  handleSelectNestedForm(id) {
    // when a nested form is selected from the preview screen, set it as the selected form and load the renderer
    if (id != this.nestedFormId) {
      this.nestedFormId = id;
      this.formLoaded = true;
    }
  }

  async handleCustomEvent(event) {
    if (event.type === "sign" && !this.isSigning) {
      this.form.formio.setPristine(false)
      if (!this.form.formio.checkValidity()) {
        document
          .getElementById(this.form.formio.errors[0].component.id)
          .scrollIntoView();
        this.nextErrorEvent.emit(null);
        return;
      }
      this.isSigning = true;
      this.notificationsBannerService.hideAllErrors()
      this.currentSignButton = event.component;
      this.fileToSign = this.form.formio
        .getComponentById(event.component.id)
        .parent.components[0].getValue()[0];

      switch (true) {
        case this.form.formio.data.eSignMethod.value === "kep":
          if (this.signService.nexULoaded) {
            this.window.nexu_get_certificates(
              this.getDataToSign.bind(this),
              this.handleNextUError.bind(this)
            );
          } else {
            this.notificationsBannerService.show({
              message: "ERRORS.NEXU_MISSING",
              type: NotificationBarType.Error,
            });
          }
          break;

        case this.form.formio.data.eSignMethod.value === signMethods.borica:
          this.filesToSign.push(this.fileToSign);
          this.signDocumentBorica(this.fileToSign.url.split(",")[1]);
          break;

        case this.form.formio.data.eSignMethod.value === signMethods.evrotrust:
          this.filesToSign.push(this.fileToSign);
          this.signDocumentEvrotrust(this.fileToSign.url.split(",")[1]);
          break;

        default:
          this.isSigning = false;
      }
    }
    if (event.type === "checkSign" && !this.isSigning) {
      this.notificationsBannerService.hideAllErrors()
      if (this.signCallbackId.length > 0) {
        this.isSigning = true;
      }
      if (event.data.eSignMethod.value === signMethods.borica) {
        this.documentsToBeSigned = this.signCallbackId.length - 1;
        this.afterSign(signMethods.borica, "responseCode", "COMPLETED");
      }

      if (event.data.eSignMethod.value === signMethods.evrotrust) {
        this.documentsToBeSigned = this.signCallbackId.length - 1;
        this.afterSign(signMethods.evrotrust, "status", 2);
      }
    }
    if (event.type === "loadForm" || event.type === "loadFormOptional") {
      if (event.component.attributes.formKey != this.nestedFormId) {
        await this.loadNestedForm(event.component.attributes.formKey);
      }
    }
    if(event.type === 'open-all'){
      this.openAllIndexedPanel();
    }
    if(event.type === 'collapse-all'){
      this.collapseAllIndexedPanel();
      document
          .getElementById('app-service-header')
          .scrollIntoView();
    }
  }

  afterSign(signSupplier, signStatus, signValue) {
    this.fileToSign = this.filesToSign[this.documentsToBeSigned];
    this.signService
      .checkSignedData(
        signSupplier,
        this.signCallbackId[this.documentsToBeSigned]
      )
      .subscribe(
        (res) => {
          if (res[signStatus] === signValue) {
            if (signSupplier === signMethods.borica) {
              this.signCallbackId[this.documentsToBeSigned] =
                res.data.signatures[0].signature;
            }
            this.signService
              .getSignedData(
                signSupplier,
                this.signCallbackId[this.documentsToBeSigned]
              )
              .subscribe(
                (resultDownload) => {
                  resultDownload.name = resultDownload.fileName;
                  const fileName =
                    this.getDocumentNameAfterSign(resultDownload);
                  const fileBlob = this.dataURItoBlob(
                    resultDownload.content,
                    resultDownload.contentType
                  );
                  const file = new File([fileBlob], fileName, {
                    type: resultDownload.contentType,
                  });
                  this.signService
                    .uploadFileToMinIo(
                      file,
                      this.ornNumber,
                      this.getFormAlias()
                    )
                    .subscribe((resultUpload) => {
                      this.fileToSign.url = `${this.ornNumber}/${this.getFormDataSubmissionKey(this.formId)}/${fileName}`
                      this.fileToSign.type = resultDownload.contentType;
                      this.fileToSign.name = fileName;
                      this.fileToSign.originalName = fileName;
                      this.fileToSign.storage = "s3";
                      this.fileToSign.bucket = environment.formioBaseProject;
                      this.fileToSign.key = `${
                        this.ornNumber
                      }/${this.getFormDataSubmissionKey(this.formId)}`;

                      if (res[signStatus] === signValue) {
                        this.refreshForm.emit({
                          submission: {
                            data: this.form.formio.data,
                          },
                        });
                        for (
                          let index = 0;
                          index < this.signCallbackId.length;
                          index++
                        ) {
                          if (
                            this.signCallbackId[index] ===
                            this.signCallbackId[this.documentsToBeSigned]
                          ) {
                            this.signCallbackId.splice(index, 1);
                          }
                        }
                        for (
                          let index = 0;
                          index < this.signedButtons.length;
                          index++
                        ) {
                          if (
                            this.signedButtons[index] ===
                            this.signedButtons[this.documentsToBeSigned]
                          ) {
                            this.redrawSignButtonAfterSign(false,this.signedButtons[index].id)                            
                            this.totalDocumentsToBeSigned--;
                            this.signedButtons.splice(index, 1);
                          }
                        }
                        for (
                          let index = 0;
                          index < this.filesToSign.length;
                          index++
                        ) {
                          if (
                            this.filesToSign[index] ===
                            this.filesToSign[this.documentsToBeSigned]
                          ) {
                            this.filesToSign.splice(index, 1);
                          }
                        }
                      }
                      this.documentsToBeSigned--;
                      if (this.documentsToBeSigned >= 0) {
                        this.afterSign(signSupplier, signStatus, signValue);
                      }
                      if (this.documentsToBeSigned < 0) {
                        this.isSigning = false;
                      }
                    });
                },
                (error) => {
                  this.isSigning = false;
                  this.notificationsBannerService.show({
                    message: "ERRORS.SIGN_ERROR",
                    type: NotificationBarType.Error,
                  });
                }
              );
          } else {
            this.documentsToBeSigned--
            if (this.documentsToBeSigned >= 0) {
              this.afterSign(signSupplier, signStatus, signValue);
            }
            if (this.documentsToBeSigned < 0) {
              this.isSigning = false;
            }
          }
        },
        (error) => {
          this.isSigning = false;
          this.notificationsBannerService.show({
            message: "ERRORS.SIGN_ERROR",
            type: NotificationBarType.Error,
          });
        }
      );
  }

  async getDataToSign(certificateData) {
    if (certificateData.response == null) {
      this.notificationsBannerService.show({
        message: "ERRORS.500",
        type: NotificationBarType.Error,
      });
    } else {
      this.certificateData = certificateData.response;
      await this.getDocumentToSign();

      this.signService.digestData(this.payloadForSign).subscribe(
        (dataToSignResponse) => {
          this.window.nexu_sign_with_token_infos(
            this.certificateData.tokenId.id,
            this.certificateData.keyId,
            dataToSignResponse.dataToSign,
            "SHA256",
            this.signDocument.bind(this),
            this.handleNextUError.bind(this)
          );
        },
        (error) => {
          this.isSigning = false;
        }
      );
    }
  }

  signDocument(signatureData) {
    this.certificateData.signatureValue = signatureData.response.signatureValue;
    this.payloadForSign.signatureValue = this.certificateData.signatureValue;
    this.signService.signData(this.payloadForSign).subscribe(
      (result) => {
        const base64 = result.bytes;
        const fileName = this.getDocumentNameAfterSign(this.fileToSign);
        const fileBlob = this.dataURItoBlob(
          base64,
          result.mimeType.mimeTypeString
        );
        const file = new File([fileBlob], fileName, {
          type: result.mimeType.mimeTypeString,
        });

        this.signService
          .uploadFileToMinIo(file, this.ornNumber, this.getFormAlias())
          .subscribe((resultUpload) => {
            this.isSigning = false;
            this.fileToSign.url = `${this.ornNumber}/${this.getFormDataSubmissionKey(this.formId)}/${fileName}`
            this.fileToSign.type = result.mimeType.mimeTypeString;
            this.fileToSign.name = fileName;
            this.fileToSign.originalName = fileName;
            this.fileToSign.storage = "s3";
            this.fileToSign.bucket = environment.formioBaseProject;
            this.fileToSign.key = `${this.ornNumber}/${this.getFormDataSubmissionKey(this.formId)}`;
            this.refreshForm.emit({
              submission: {
                data: this.form.formio.data,
              },
            });

            if (
              this.signedButtons.findIndex(
                (b) => b.id === this.currentSignButton.id
              ) === -1
            ) {
              this.signedButtons.push(this.currentSignButton);
              
              this.redrawSignButtonAfterSign()
            }
            this.notificationsBannerService.show({
              message: "SUCCESSFUL_SIGN",
              type: NotificationBarType.Success,
            });
            this.totalDocumentsToBeSigned--
          });
      },
      (error) => {
        this.isSigning = false;
      }
    );
  }

  signDocumentBorica(signatureData) {
    let boricaSignBody = {
      contents: [
        {
          confirmText:
            this.translateService.instant("DOCUMENT_FOR_SIGNING") +
            this.fileToSign.name,
          contentFormat: "BINARY_BASE64",
          mediaType: this.fileToSign.type,
          data: signatureData,
          fileName: this.fileToSign.name,
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
    this.signService
      .signDataBorica(
        this.userProfileService.currentUser.personIdentifier.split("-")[1],
        boricaSignBody
      )
      .subscribe(
        (dataToSignResponse) => {
          if (dataToSignResponse.responseCode === "ACCEPTED") {
            this.isSigning = false;
            this.signCallbackId.push(dataToSignResponse.data.callbackId);
            if (
              this.signedButtons.findIndex(
                (b) => b.id === this.currentSignButton.id
              ) === -1
            ) {
              this.signedButtons.push(this.currentSignButton);
              this.form.formio.data.checkFileSign = true;
              this.refreshForm.emit({
                submission: {
                  data: this.form.formio.data,
                },
              });
              this.redrawSignButtonAfterSign(true)
            }
          }
        },
        (error) => {
          this.isSigning = false;
          this.notificationsBannerService.show({
            message: "ERRORS.SIGN_ERROR",
            type: NotificationBarType.Error,
          });
        }
      );
  }

  signDocumentEvrotrust(signatureData) {
    this.signingDate = new Date();
    let dateExpire = new Date();
    dateExpire.setDate(dateExpire.getDate() + 1);
    let evrotrustSignBody = {
      dateExpire: dateExpire,
      document: {
        content: signatureData,
        fileName: this.fileToSign.name,
        contentType: this.fileToSign.type,
      },
      userIdentifiers: [
        this.userProfileService.currentUser.personIdentifier.split("-")[1],
      ],
    };

    this.signService.signDataEvrotrust(evrotrustSignBody).subscribe(
      (dataToSignResponse) => {
        if (dataToSignResponse.transactions) {
          this.isSigning = false;
          this.signCallbackId.push(
            dataToSignResponse.transactions[0].transactionID
          );
          if (
            this.signedButtons.findIndex(
              (b) => b.id === this.currentSignButton.id
            ) === -1
          ) {
            this.signedButtons.push(this.currentSignButton);
            this.refreshForm.emit({
              submission: {
                data: this.form.formio.data,
              },
            });
            this.form.formio.data.checkFileSign = true;
            this.redrawSignButtonAfterSign(true)
          }
        }
      },
      (error) => {
        this.isSigning = false;
        this.notificationsBannerService.show({
          message: "ERRORS.SIGN_ERROR",
          type: NotificationBarType.Error,
        });
      }
    );
  }

  redrawSignButtonAfterSign(isWaitingForStatusCheck = false, id = null){
    let signButtonInstance = this.form.formio
    .getComponentById(id ? id : this.currentSignButton.id)
    signButtonInstance.component.label = this.translateService.instant(
      isWaitingForStatusCheck? "DOCUMENT_IS_SENT_FOR_SIGN" : "DOCUMENT_IS_SIGNED"
    )
    signButtonInstance.component.event='do-nothing'
    signButtonInstance.component.customClass='button-after-sign' + (isWaitingForStatusCheck ? ' waiting-for-status-check' : '')
    signButtonInstance.component.disabled=true
    signButtonInstance.redraw()
  }

  handleNextUError() {
    this.isSigning = false;
    this.notificationsBannerService.show({
      message: "ERRORS.NEXU_ERROR",
      type: NotificationBarType.Error,
    });
  }

  async getDiegest() {
    //convert file to bytes and get digest
    const data = atob(this.fileToSign.url.split(",")[1]);
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

  replaceComponentByKey(components, key, value) {
    for (let i = 0; i < components.length; i++) {
      if (components[i].key === key) {
        components[i] = value;
      }
      if (components[i].components) {
        this.replaceComponentByKey(components[i].components, key, value);
      }
      if (components[i].columns) {
        components[i].columns.forEach((column) => {
          this.replaceComponentByKey(column.components, key, value);
        });
      }
      if (components[i].rows) {
        if (components[i].rows.length) {
          components[i].rows.forEach((row) => {
            row.forEach((element) => {
              this.replaceComponentByKey(element.components, key, value);
            });
          });
        }
      }
    }
  }

  getFormDataSubmissionKey(formPath) {
    if (formPath.includes("?")) {
      formPath = formPath.substring(0, formPath.indexOf("?"));
    }
    let formPathArray = formPath.split("/");
    formPathArray.forEach(function (part, index) {
      this[index] = this[index].replace(/-([a-z])/g, function (g) {
        return g[1].toUpperCase();
      });
    }, formPathArray);
    return formPathArray.join("_");
  }

  getDocumentNameAfterSign(file) {
    if (this.signService.shouldFileBeSignedWithAttachedSignatures(file.originalName ? file.originalName : file.name)) {
      return file.originalName ? file.originalName : file.name;
    }
    else {
      let originalName = file.originalName ? file.originalName : file.name
      let nameArray = originalName.split('.')
      let fileExt = nameArray.pop()
      let name= nameArray.join('.')
      return `${name}-${fileExt}-${moment().unix()}.p7s`
    }
  }

  async getDocumentToSign() {
    this.signingDate = new Date();
    if (
      this.signService.shouldFileBeSignedWithAttachedSignatures(
        this.fileToSign.originalName
      )
    ) {
      this.payloadForSign = {
        signingCertificate: this.certificateData.certificate,
        certificateChain: this.certificateData.certificateChain,
        encryptionAlgorithm: this.certificateData.encryptionAlgorithm,
        documentToSign: this.fileToSign.url.split(",")[1],
        documentName: this.fileToSign.originalName,
        signingDate: this.signingDate,
      };
    } else {
      let digest = await this.getDiegest();
      this.payloadForSign = {
        signingCertificate: this.certificateData.certificate,
        certificateChain: this.certificateData.certificateChain,
        encryptionAlgorithm: this.certificateData.encryptionAlgorithm,
        digestToSign: digest,
        documentName: this.fileToSign.originalName,
        signingDate: this.signingDate,
      };
    }
  }

  dataURItoBlob(dataURI, type) {
    const byteString = window.atob(dataURI);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const int8Array = new Uint8Array(arrayBuffer);
    for (let i = 0; i < byteString.length; i++) {
      int8Array[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([int8Array], { type: type });
    return blob;
  }

  getFormAlias(id = null) {
    let formId = id ? id : this.formId;
    return formId.split("?")[0];
  }

  async loadNestedForm(id: string) {
    let nestedForm = this.nestedForms.find(
      (form) => form.id === this.nestedFormId
    );
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

  async saveNestedFormSubmition(shouldResetData = true) {
    if(!this.readonly) {
      this.consolidatingForm.setPristine(false)
      let nestedForm = this.nestedForms.find(form => form.id === this.nestedFormId)
      if(nestedForm){
        nestedForm.isValid = this.consolidatingForm.checkValidity()
      }
      nestedForm.hasBeenActive = true
      let submissionDataName = "submissionData_" + nestedForm.path;
      let variables = {};
      variables['modifications'] = {};
      variables['modifications'][submissionDataName] = {}
      let dataCopy = JSON.parse(JSON.stringify(this.consolidatingForm._data.consolidatingForm.data))
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
        if (!this.dataSrcToRemove.includes(key)) {
          data[key] = dataCopy[key];
        }
      }
      variables['modifications'][submissionDataName]['value'] = { data:  data};
      variables['modifications'][submissionDataName]['value']['data'] ['isValid'] = nestedForm.isValid;
      await this.camundaProcessService.setLocalVariableInProcess(this.taskId, variables).toPromise()
      if(shouldResetData) {
        this.consolidatingForm._data.consolidatingForm.data = []
      }
    }
  }

  async setLocalVariable(formData: any, path: string) {
    let submissionDataName = "submissionData_" + this.getFormDataSubmissionKey(path);
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
      if (!this.dataSrcToRemove.includes(key)) {
        data[key] = dataCopy[key];
      }
    }
    variables['modifications'][submissionDataName]['value'] = { data:  data};
    await this.camundaProcessService.setLocalVariableInProcess(this.taskId, variables).toPromise()
  }

  updatePanelButton(nestedFormComponent, isSelected: boolean) {
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
      },
      type: "button",
      event: "loadForm",
      input: true,
      rightIcon: icon,
    };
    this.replaceComponentByKey(
      this.formSrc.components,
      nestedFormComponent.componentKey,
      newComponent
    );
  }

  async setCurrentTabInNestedForm(nestedForm) {
    this.nestedFormId = nestedForm.id;
    //Update the form sorce in the select
    this.updateNestedFormSrc(this.formSrc.components);
    //get the submission of the newly selected nested form
    let data:any = {
      consolidatingForm: {
        data: {}
      }
    }

    if(!this.readonly) {
      try {
        let variable = await this.camundaProcessService.getLocalVariableFromProcess(this.taskId, "submissionData_" + nestedForm.path).toPromise();
        data.consolidatingForm.data = variable["value"]["data"]
        data.consolidatingForm.data.dontOverrideUserData = true
        data.consolidatingForm.data.hasFormSubmition = true
        data.redrawNestedForm = true;
        //update the panel button to active
        this.updatePanelButton(nestedForm, true);
        this.formAfterNestedChange = true;
      } catch {
        let variable = await this.formIoService.getFormSubmitionByBuissnessKey(nestedForm.id, this.ornNumber).toPromise()
        if(variable.length) {
          data.consolidatingForm.data = variable[0].data
          data.consolidatingForm.data.dontOverrideUserData = true
          data.consolidatingForm.data.hasFormSubmition = true
          data.redrawNestedForm = true;
        } else {
          data.consolidatingForm.data.hasFormSubmition = false
        }
        //update the panel button to active
        this.updatePanelButton(nestedForm, true);
        // emit the new json with updated buttons, nested form src and data
        this.formAfterNestedChange = true;
      }
    } else {
      let variable = await this.formIoService.getFormSubmitionByBuissnessKey(nestedForm.id, this.ornNumber).toPromise()
      if(variable.length) {
        data.consolidatingForm.data = variable[0].data
        data.consolidatingForm.data.isDataPrefilledFromBackend = true
        data.consolidatingForm.data.hasFormSubmition = true
        data.redrawNestedForm = true;
      } else {
        data.consolidatingForm.data.hasFormSubmition = false
      }
      //update the panel button to active
      this.updatePanelButton(nestedForm, true);
      // emit the new json with updated buttons, nested form src and data
      this.formAfterNestedChange = true;
    }

    this.refreshForm.emit({
      form: this.formSrc,
      submission: {
        data: data,
      },
    });
  }

  updateNestedFormSrc(components) {
    for (let i = 0; i < components.length; i++) {
      if (components[i].type === "form") {
        components[i].path = this.nestedFormId;
        components[i].form = null;
        // update the form context of the nested form with the correct alias
        let context = JSON.parse(localStorage.getItem("formContext"));
        context.formAlias = this.getFormAlias(this.nestedFormId);
        context.parrentForm = this.getFormAlias();
        localStorage.setItem("formContext", JSON.stringify(context));
        break;
      }
      if (components[i].components) {
        this.updateNestedFormSrc(components[i].components);
      }
      if (components[i].columns) {
        components[i].columns.forEach((column) => {
          this.updateNestedFormSrc(column.components);
        });
      }
      if (components[i].rows) {
        if (components[i].rows.length) {
          components[i].rows.forEach((row) => {
            row.forEach((element) => {
              this.updateNestedFormSrc(element.components);
            });
          });
        }
      }
    }
  }
}
