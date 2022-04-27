import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-mobile-menu',
  templateUrl: './mobile-menu.component.html',
  styleUrls: ['./mobile-menu.component.scss']
})
export class MobileMenuComponent implements OnInit {
  @Output() public sidenavClose = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }
  closeMenu() {
    this.sidenavClose.emit();
  }
}
