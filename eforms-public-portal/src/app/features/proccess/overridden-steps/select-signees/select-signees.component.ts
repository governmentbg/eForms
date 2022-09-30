import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, NgForm, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { DAEFDocument } from 'src/app/core/types/daef-document';
import { documentStatuses } from 'src/app/core/types/document-statuses';
import { SignatureOptionsEnum } from 'src/app/core/types/signature-options';
import { Signee } from 'src/app/core/types/signee';
import { AdvancedDialogComponent } from 'src/app/shared/components/advanced-dialog/advanced.dialog.component';
import { ValidatePinOrEgn } from 'src/app/shared/validators/validateEgn';


@Component({
  selector: 'app-select-signees',
  templateUrl: './select-signees.component.html',
  styleUrls: ['./select-signees.component.scss']
})
export class SelectSigneesComponent implements OnInit {
  private _isOnline: boolean;

  @Input() set isOnline (value: boolean) {
    this._isOnline = value;

    if (this._isOnline) {
      this.editSigneeFormGroup.enable();
    } else {
      this.editSigneeFormGroup.disable();
    }
  }

  @Input() daefDocument: DAEFDocument;
  @Output() onDocumentSigneesError: EventEmitter<boolean>;
  @Output() onDocumentSigneesLoaded: EventEmitter<any>;
  @Output() onDocumentSigneesManipulated: EventEmitter<boolean>;
  @ViewChild('editSigneeForm') editSigneeForm: NgForm;
  
  signatureOptions = SignatureOptionsEnum.getSignatureOptionsArray()
  disableSignatureOption = SignatureOptionsEnum.signeesSignature.value
  signatureOptionsEnumModel = SignatureOptionsEnum.getSignatureOptionsObjectModel()

  columns = [
      {
        columnDef: 'names',
        width: "30%",
        header: 'NAMES',
        cell: (element: Signee) => {

          const firstName = (element.signeeFirstName)? element.signeeFirstName : "";
          const middleName = (element.signeeMiddleName)? element.signeeMiddleName : "";
          const lastName = (element.signeeLastName)? element.signeeLastName : "";

          return `${firstName} ${middleName} ${lastName}`
        },
      },
      {
        columnDef: 'identifier',
        width: "10%",
        header: 'EGN_OR_IDENTIFIER',
        cell: (element: Signee) => `${element.signeeIdentifier}`,
      },
      {
        columnDef: 'signee-as',
        width: "20%",
        header: 'SIGNEE_AS',
        cell: (element: Signee) => `${(element.signeeAs)? element.signeeAs : ""}`,
      },
      {
        columnDef: 'signee-address',
        width: "20%",
        header: 'ADDRESS',
        cell: (element: Signee) => `${(element.signeeAddress)? element.signeeAddress : ""}`,
      },
      {
        action: true,
        width: "20%",
        columnDef: 'action'
      }
    ];
  displayedColumns = this.columns.map(c => c.columnDef);

  editSigneeFormGroup: FormGroup;
  editSigneeRowIndex = -1
  signeesManipulation : boolean = false;
  currentUserData
  atLeastOneSigneeRequired : boolean = false;
  signeeFormTemplate = {
      signeeIdentifier: ['',[Validators.required, this.UniqueSigneeIdentifierValidator().bind(this), this.RequestorNotAllowedInSigneesListValidator().bind(this), ValidatePinOrEgn()]],
      signeeFirstName: [''],
      signeeMiddleName: [''],
      signeeLastName: [''],
      signeeAs: [''],
      signeeAddress: ['']
    }
  daefDocumentStatuses;

  constructor(
    private userProfileService: UserProfileService,
    private formBuilder: FormBuilder,
    public  dialog: MatDialog
  ) { 
    this.editSigneeFormGroup = this.formBuilder.group(this.signeeFormTemplate)
    this.onDocumentSigneesError = new EventEmitter<boolean>();
    /**
     * Emitter to signal the father component that this document's signees have initiated/loaded.
     */
    this.onDocumentSigneesLoaded = new EventEmitter<any>();
    this.onDocumentSigneesManipulated = new EventEmitter<any>();
    
    this.daefDocumentStatuses = documentStatuses;
  }

  get isOnline() {
    return this._isOnline;
  }

  ngOnInit(): void {

    // init the current user's data
    this.initCurrentUserData(this.userProfileService.currentUser)
    
    // default init condition for this component
    if(this.daefDocument.signeesList.length === 0 && this.daefDocument.requiredSignatures === undefined){
      // add current user as default signee
      this.daefDocument.signeesList.push(this.currentUserData)
      // set the default required signature
      this.daefDocument.requiredSignatures = SignatureOptionsEnum.requestorSignature.value
    } 
    else {
      // non default init condition
      // signal a change in this document's editing or error conditions
      this.onDocumentSigneesError.emit(this.hasSigneesError)
    }

    // signal that the signees have initiated/loaded
    this.onDocumentSigneesLoaded.emit()  
  }

  /**
   * Initialises the logged user's data
   * as a signee.
   * 
   * @param userProfile 
   */
  initCurrentUserData(userProfile) {
    
    let firstName = userProfile.personName.split(" ")[0];
    let middleName = userProfile.personName.split(" ")[1];
    let lastName = userProfile.personName.split(" ")[2];
    
    this.currentUserData = {
      signeeIdentifier: userProfile.personIdentifier.split("-")[1],
      signeeFirstName: firstName,
      signeeMiddleName: (middleName)? middleName: "",
      signeeLastName: (lastName)? lastName: "",
      signeeAs: (this.userProfileService.selectedProfile ? "SIGNING.REPRESENTING_PERSON" : 'SERVICES.REQUESTOR'),
      signeeAddress: userProfile.adrresslineCorrespondence
    }
  }

  /**
   * Handler function for actions when this document's
   * required signatures indication changes.
   * 
   * @param requiredSignatureSelected 
   */
  handleRequiredSignaturesChange(requiredSignatureSelected) {

    this.daefDocument.signeesList = []  // reset signees list
    
    if(requiredSignatureSelected !== SignatureOptionsEnum.signeesSignature.value) {
      // add current user as default signee
     this.daefDocument.signeesList.push(this.currentUserData)
    } 

    this.refreshSigneesTable()
    this.signeesManipulation = false  // reset the manipulation flag

    // signal a change in this document's editing or error conditions
    this.onDocumentSigneesError.emit(this.hasSigneesError)

  }

  /**
   * Getter for this document's data consistency state.
   * Error flag is returned if this document's data should (true) and
   * shouldn not (false) be used e.g. inconsistent/error state - editing a signee's data,
   * consistent/no error state - having at least one signee, etc.
   */
  get hasSigneesError() : boolean {

    if(this.signeesManipulation) {
      // edit mode/signee manipulation is considered error as to lock the data for father components
      return true;
    } else if(this.daefDocument.requiredSignatures === SignatureOptionsEnum.requestorSignature.value) {
      // the requestor is the signee, so no error
      return false;

    } else if(this.daefDocument.requiredSignatures === SignatureOptionsEnum.signeesSignature.value) {
      // must have at least one signee
      return !this.daefDocument.signeesList.length;

    } else if(this.daefDocument.requiredSignatures === SignatureOptionsEnum.requestorAndSigneesSignature.value) {
      // must have at least one signee other than the requestor
      return !(this.daefDocument.signeesList.length > 1);
    } 

    // no signees is always an error
    return !this.daefDocument.signeesList.length
  }

  /**
   * Enable/disable a signee's data editing.
   * 
   * @param signeeRowIndex
   */
  handleEditSignee(signeeRowIndex){

    this.signeesManipulation = true
    this.editSigneeRowIndex = signeeRowIndex
    // this.editSigneeFormGroup.setValue(this.daefDocument.signeesList[this.editSigneeRowIndex]);

    const signee = this.daefDocument.signeesList[this.editSigneeRowIndex];
    this.editSigneeFormGroup.setValue({
      signeeIdentifier: signee.signeeIdentifier,
      signeeFirstName: (signee.signeeFirstName) ? signee.signeeFirstName : "",
      signeeMiddleName: (signee.signeeMiddleName)? signee.signeeMiddleName: "",
      signeeLastName: (signee.signeeLastName)? signee.signeeLastName: "",
      signeeAs: (signee.signeeAs)? signee.signeeAs: "",
      signeeAddress: (signee.signeeAddress)? signee.signeeAddress : ""
    });   
    
    // signal a change in this document's editing or error conditions
    this.onDocumentSigneesError.emit(this.hasSigneesError)
  }

  /**
   * Handles the deletion of a signee.
   * 
   * @param signeeRowIndex 
   */
  handleDeleteSignee(signeeRowIndex){

    // data object for the modal
    let deleteSigneeModalData = {
      headerTitle: "REMOVE_SIGNEE_MODAL_HEADER_TITLE",
      bodyIcon: "info",
      mainTitle: "REMOVE_SIGNEE_MODAL_MAIN_TITLE",
      bodyMessage : "",
      canGoBack: true,
      canGoBackMessage: "GO_BACK",
      canGoForward: true,
      canGoForwardMessage: "REMOVE"
    }
    
    let deleteSigneeModalRef = this.dialog.open(AdvancedDialogComponent, { data: deleteSigneeModalData });

    deleteSigneeModalRef.componentInstance.confirmed.subscribe(() => {

      this.daefDocument.signeesList.splice(signeeRowIndex,1)  // remove the signee from this document's signees list
    
      this.refreshSigneesTable()

      // signal a change in this document's editing or error conditions
      this.onDocumentSigneesError.emit(this.hasSigneesError)
      // signal that a signee has been deleted
      this.onDocumentSigneesManipulated.emit()
    })

  }

  /**
   * Updates the signees table in the view.
   */
  private refreshSigneesTable() {
    this.daefDocument.signeesList = [...this.daefDocument.signeesList]
  }

  /**
   * Saves the added or edited signee's data
   * in this document's signees list.
   */
  saveSigneeData() {

    if(this.editSigneeFormGroup.valid) {

      if(this.editSigneeRowIndex != -1) {
        // if the signee row index is different than the default value - you're in editing mode 
        this.daefDocument.signeesList[this.editSigneeRowIndex] = this.editSigneeFormGroup.value
        this.editSigneeRowIndex = -1
      } else {
        // othewise you're in adding/creating a signee, so push the formGroup in the signees list
        this.daefDocument.signeesList.push(this.editSigneeFormGroup.value)
      }

      this.refreshSigneesTable()
      this.signeesManipulation = false
      // signal a change in this document's editing or error conditions
      this.onDocumentSigneesError.emit(this.hasSigneesError)
      // signal that a signee has been added
      this.onDocumentSigneesManipulated.emit()

    } 
  }

  /**
   * Declines the addition of a signee,
   * or the editing of a signee's data.
   */
  declineSigneeData() {
    this.editSigneeRowIndex = -1;
    this.signeesManipulation = false
    // signal a change in this document's editing or error conditions
    this.onDocumentSigneesError.emit(this.hasSigneesError)
  }

  /**
   * Enables the addition of a signee.
   */
  addSignee() {
    this.signeesManipulation = true;
    this.editSigneeFormGroup = this.formBuilder.group(this.signeeFormTemplate)

    // signal a change in this document's editing or error conditions
    this.onDocumentSigneesError.emit(this.hasSigneesError)
  }

  /**
   * Validates the uniqueness of a signee (by identifier)
   * between the edited document's signee list
   * and the currently inserting/editing signee.
   * 
   * @returns 
   */
  UniqueSigneeIdentifierValidator(): ValidatorFn {  
    return (control: AbstractControl): { [key: string]: any } | null => {
  
      let signeeIndex = this.daefDocument?.signeesList.findIndex((signee) => signee.signeeIdentifier === control.value)

      if(signeeIndex === -1){   
        return null;
      } else if(this.editSigneeRowIndex != -1 && this.editSigneeRowIndex === signeeIndex) {
        return null
      } else {
        return {uniqueSigneeIdentifierError: control.value};
      }
    }
        
  }

  /**
   * Depending on the required signatures choice,
   * validates whether the requestor is allowed
   * in the current document's signees list, or not.
   * 
   * @returns 
   */
  RequestorNotAllowedInSigneesListValidator(): ValidatorFn {  
    return (control: AbstractControl): { [key: string]: any } | null => {
  
      if(this.daefDocument?.requiredSignatures === SignatureOptionsEnum.signeesSignature.value && 
        control.value === this.currentUserData.signeeIdentifier) {
        return {requestorNotAllowedInSigneesListError: control.value}
      } else {
        return null;
      }
    }
        
  } 
}



