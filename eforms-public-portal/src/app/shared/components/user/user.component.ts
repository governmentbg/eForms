import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  @Input() userName: string;
  user: string;
  constructor() { }

  ngOnInit(): void {
    this.user = this.userName;
  }

}
