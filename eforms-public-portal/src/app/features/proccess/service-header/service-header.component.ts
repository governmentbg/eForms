import {Component, EventEmitter, HostListener, Input, OnInit, Output} from '@angular/core';
import { DAEFService } from 'src/app/core/services/daef-service.service'

@Component({
  selector: 'app-service-header',
  templateUrl: './service-header.component.html',
  styleUrls: ['./service-header.component.scss']
})
export class ServiceHeaderComponent implements OnInit {
  service;
  serviceId;
  isMobile: boolean;
  isOnline = true;
  public innerWidth: any;
  @Input() isFinal : boolean;
  @Input() isAdminProcessProp : boolean;
  @Output() cancelDeclarationEvent = new EventEmitter();
  @Input() isOnlineObservable;

  constructor(private daefService: DAEFService) { }
  
  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerWidth = window.innerWidth;
    this.isMobile = this.innerWidth < 481;
  }
  ngOnInit(): void {
    this.checkForInternetConnection();
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

  notifyCancelDeclaration() {
    this.cancelDeclarationEvent.emit(true)
  }

  private checkForInternetConnection(): void {
    if (this.isOnlineObservable) {
      this.isOnlineObservable.subscribe((isOnline) => {
        this.isOnline = isOnline
      });
    }
  }

}
