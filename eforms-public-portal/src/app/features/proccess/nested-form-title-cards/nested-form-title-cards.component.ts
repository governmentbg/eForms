import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-nested-form-title-cards',
  templateUrl: './nested-form-title-cards.component.html',
  styleUrls: ['./nested-form-title-cards.component.scss']
})
export class NestedFormTitleCardsComponent implements OnInit {
  @Input() nestedForms;
  @Input() readonly;
  @Output() selectNestedFormEvent = new EventEmitter();
  constructor() { }

  ngOnInit(): void {
  }

  selectNestedForm(form){
    this.selectNestedFormEvent.emit(form.id)
  }

}
