import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  closeableFooter = false;
  showSecondFooter = true;
  currentRoute;
  environment;

  constructor(private router: Router) {
    this.environment = environment;
   }

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = event.url;
      }
        if (this.currentRoute === '/login') {
          this.closeableFooter = true;
        }
      });
  }

  closeSecondFooter() {
    this.showSecondFooter = false;
  }
}
