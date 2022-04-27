import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  language: string;
  menuIcon = "menu";

  @Output() public sidenavToggle = new EventEmitter();
  @Input() public closeSidenavEvent: Observable<any>;

  constructor() { }

  ngOnInit(): void {
    this.language = 'Български';
    this.closeSidenavEvent.subscribe(() => {
      this.toggleMenu();
    })
  }

  openEgov() {
    const url = `https://${environment.egovBaseURL}/`;
    window.open(url, '_blank');
  }

  changeLanguage() {
    this.language === 'Български' ? this.language = 'English' : this.language = 'Български';
  }

  toggleMenu() {
    this.sidenavToggle.emit();
    this.menuIcon === "menu" ? this.menuIcon = 'close' : this.menuIcon = "menu";
  }

  handleOnCloseEvent() {
    this.toggleMenu();
  }
}
