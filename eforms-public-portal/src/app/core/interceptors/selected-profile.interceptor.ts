import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { DeepLinkService } from 'src/app/core/services/deep-link.service'

@Injectable()
export class ProfileInterceptor implements HttpInterceptor {

  constructor(
    private deepLinkService: DeepLinkService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if(!request.url.match('nexu') && !request.url.includes('data.applicant')){
      let selectedProfile = this.deepLinkService.getParam('selectedProfile');
      if(selectedProfile) {
        request = request.clone({
          setParams: {
            applicant: selectedProfile.identifier
          }
        });
      }
    }
    return next.handle(request);
  }
}

export const profileProvider = {
  // use fake backend in place of Http service for backend-less development
  provide: HTTP_INTERCEPTORS,
  useClass: ProfileInterceptor,
  multi: true
};
