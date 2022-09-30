import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { documentRequirement } from 'src/app/core/types/document-requirement';

@Component({
  selector: 'app-nested-form-title-cards',
  templateUrl: './nested-form-title-cards.component.html',
  styleUrls: ['./nested-form-title-cards.component.scss']
})
export class NestedFormTitleCardsComponent implements OnInit {
  @Input() nestedForms;
  @Input() readonly : boolean;
  @Input() hasInitDocument : boolean;
  @Input() isOnline : boolean;
  @Output() selectNestedFormEvent = new EventEmitter();
  constructor() { }

  ngOnInit(): void {
  }

  selectNestedForm(form){
    if(this.isOnline) {
      this.selectNestedFormEvent.emit(form.id)
    }
  }
  public isDocumentRequired(isDocumentRequired){
    return isDocumentRequired === documentRequirement.required 
    || isDocumentRequired === documentRequirement.requiredFromMain 
    || isDocumentRequired === documentRequirement.userChoiceRequired
    || !isDocumentRequired
  }

}
