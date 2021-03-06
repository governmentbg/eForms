import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {
  @Input() error;
  @Input() showBackButton;

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  redirectToHome() {
    this.router.navigate(['home']);
  }

  goBack() {
    history.back();
  }
}