import { Component, HostListener, Input, OnInit } from '@angular/core';
import { DAEFService } from 'src/app/core/services/daef-service.service'
import { DeepLinkService } from 'src/app/core/services/deep-link.service';

@Component({
  selector: 'app-service-header',
  templateUrl: './service-header.component.html',
  styleUrls: ['./service-header.component.scss']
})
export class ServiceHeaderComponent implements OnInit {
  service;
  serviceId;
  isMobile: boolean;
  public innerWidth: any;

  constructor(private daefService: DAEFService) { }
  
  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerWidth = window.innerWidth;
    this.isMobile = this.innerWidth < 481;
  }
  ngOnInit(): void {
    this.serviceId = this.daefService.subject.value.serviceId
    this.daefService.getDAEFService(this.serviceId).subscribe((daefS) => {
      this.service = daefS.service.data;
    })
    this.isMobile = this.innerWidth < 481;
  }

  openEpdeauService() {
    const url = this.service.url;
    window.open(url, '_blank');
  }

}
