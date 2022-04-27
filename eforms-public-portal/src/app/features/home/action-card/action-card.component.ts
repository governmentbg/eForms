import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-action-card',
  templateUrl: './action-card.component.html',
  styleUrls: ['./action-card.component.scss']
})
export class ActionCard implements OnInit {


  @Input() serviceLabel: string;
  @Input() serviceIcon: string;
  @Input() serviceLink: string;

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  public serviceSelected() {
    if(this.serviceLink.includes('https')){
      window.location.replace(this.serviceLink);
    } else {
      this.router.navigate([this.serviceLink])
    }
  }
}
