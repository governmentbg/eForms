import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from 'src/app/core/services/payment.service';

@Component({
  selector: 'app-hidden-borica-form',
  templateUrl: './hidden-borica-form.component.html',
  styleUrls: ['./hidden-borica-form.component.scss']
})
export class HiddenBoricaFormComponent implements OnInit, AfterViewInit {
  boricaFormHTML: SafeHtml

  constructor(
    private domSanitizer: DomSanitizer,
    private router: Router
  ) {
    let boricaForm = localStorage.getItem('boricaForm');
    if (!boricaForm) {
      this.router.navigate(['home']);
    }

    this.boricaFormHTML = this.domSanitizer.bypassSecurityTrustHtml(boricaForm);
  }

  ngOnInit(): void { }

  ngAfterViewInit(): void {
    document.forms[0].submit();
  }
}
