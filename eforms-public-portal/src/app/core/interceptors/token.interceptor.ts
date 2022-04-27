import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(
    public oidcSecurityService: OidcSecurityService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if( request.url.match('api/public') ) {
      // Ignore Bearer
    } else if(!request.url.match('nexu.js') || request.url.match('nexu-installer')){
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.oidcSecurityService.getToken()}`
        }
      });
    }
    return next.handle(request);
  }
}

export const tokenProvider = {
  // use fake backend in place of Http service for backend-less development
  provide: HTTP_INTERCEPTORS,
  useClass: TokenInterceptor,
  multi: true
};
