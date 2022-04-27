
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-forbidden',
  templateUrl: './forbidden.component.html',
  styleUrls: ['./forbidden.component.scss']
})
export class ForbiddenComponent implements OnInit {

  errorMsg: string;
  constructor() { }

  ngOnInit(): void {
    this.errorMsg = "FORBIDDEN";
  }

}
