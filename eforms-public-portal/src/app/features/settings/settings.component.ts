import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  selectedFirst = '1';
  selectedSecond = '3';

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  back() {
    this.router.navigate(['home']);
  }

  closeSettings() {
    this.router.navigate(['home']);
  }

}
