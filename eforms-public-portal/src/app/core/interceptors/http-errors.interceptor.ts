import { Injectable } from '@angular/core';
import { Injector } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { NotificationsBannerService } from '../services/notifications-banner.service';
import { NotificationBarType } from '../../shared/components/notifications-banner/notification-banner.model';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { environment } from 'src/environments/environment';
import Utils from 'src/app/shared/utilities/utils';

@Injectable()
export class HttpErrorsInterceptor implements HttpInterceptor {

  constructor(
    private notificationsBannerService: NotificationsBannerService,
    private router: Router,
    private readonly injector: Injector
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
      .pipe(
        retry(0),
        catchError((error: HttpErrorResponse) => {

          if (error?.status === 401 || error?.error?.status === 401 || error?.error === 'invalid_grant') {
            localStorage.setItem('INVALID_GRANT_MESSAGE', 'true');
            window.location.href = window.location.origin + '/login';
            return throwError(error?.error);
          }
          if (error.url.startsWith(environment.stsServer) && (error?.error?.status === 400 || error?.status === 400)) {
            localStorage.setItem('INVALID_GRANT_MESSAGE', 'true');
            window.location.href = window.location.origin + '/login';
            return throwError(error?.error);
          }

          if(error?.error?.message?.includes('NOT_SATISFIED_ASSURANCE_LEVEL')){
            return throwError(error?.error);
          }
          if (error?.status === 403) {
            if (error?.error?.message === 'ERROR.GATEWAY.NOT_ALLOWED_PROFILE_TYPE') {
              this.router.navigate(['dashboard'], {
                state: {
                  customError: (window.location.href.includes('my-services/process') ? 'ERROR.GATEWAY.NOT_ALLOWED_PROFILE_TYPE_STARTED' : ''),
                }
              });

              return throwError(error?.error);
            }

            if (error.url.match(/(\/current-task)/)) {
              return throwError(error?.error);
            }
            
            if (error?.error?.message.includes('EDELIVERY.SERVICE_NOT_AVAILABLE')) {
              return throwError(error?.error);
            }
            if(error.url.match(/(\/start-process)/)){
                if(error?.error?.message.includes('PROFILE_NOT_FOUND')) {
                    this.notificationsBannerService.show({message: "SERVICES.ERRORS.USER_PROFILE_NOT_FOUND", type: Utils.getNotificationTypeFromMessage(error?.error?.message), actionText: "HERE", url: `https://${environment.edeliveryURL}`, openInNewTab: true })
                    return throwError(error?.error);
                } if(error?.error?.message.includes('NOT_AUTHORIZED')) {
                  this.notificationsBannerService.show({message: "SERVICES.ERRORS.USER_PROFILE_NOT_AUTHORIZED", type: NotificationBarType.Error})
                  return throwError(error?.error);
                }
            }
            this.router.navigate(['403']);
          }

          if (error.url.match(/(\/localVariables)/) && error?.error?.status === 404) {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/current-task)/) && error?.error?.status === 404 && error?.error?.message.includes("RESOURCE_NOT_FOUND")) {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/eas)/) && error?.error?.status === 400 && error?.error?.message.includes("SERVICE_NOT_ACTIVE")) {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/eas)/) && error?.error?.status === 404 && error?.error?.message.includes("RESOURCE_NOT_FOUND")) {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/eas)/) && error?.error?.status === 404 && error?.error?.message.includes("USER_PROFILE_NOT_FOUND")) {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/Vpos)/)) {
            return throwError(error);
          }
          if (error.url.includes('api/borica/cert/identity')) {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/file)/) && request.method === 'GET') {
            return throwError(error?.error);
          }
          if(error.url.match(/(\/complete)/) && (error?.error?.status === 500 || error?.status === 500)) {
            if (error?.error?.message) {
              if (error?.error?.message.includes('Име на банката, в която е сметката на доставчика на ЕАУ is required') || error?.error?.message.includes('IBAN код на сметката на доставчика на ЕАУ is required')) {
                this.notificationsBannerService.show({message: "ERRORS.PAYMENT_NOT_AVAILABLE", type: NotificationBarType.Error})
                return throwError(error?.error);
              }
            }
            if (error?.message?.includes('eDelivery') || error?.error?.message?.includes('eDelivery') ) {
              this.notificationsBannerService.show({message: "SERVICES.ERRORS.EDELIVERY_NOT_AVAILABLE", type: NotificationBarType.Error});
              return throwError(error?.error);
            }
            if (error?.error?.status === 500 && error?.error?.message.includes("MISSING_SIGNEES_PROFILES")) {
              let postfix = error?.error.data.length === 1 ? '1' : 'N';
              let placeholders = {pin: error?.error.data.join()};
              this.notificationsBannerService.hideAll();
              this.notificationsBannerService.show({message: "ERRORS.MISSING_SIGNEES_PROFILES_" + postfix, type: NotificationBarType.Error, placeholders: placeholders});
              return throwError(error?.error);
            }
          }
          if (error.url.match(/(\/user-profile\/update)/) && error?.error?.status === 500 && error?.error?.message === '') {
            this.notificationsBannerService.hideAll();
            this.notificationsBannerService.show({message: "ERRORS.NO_USER_PROFILE_INFO", actionText: "REGISTRATION_LINK", url: `https://${environment.egovBaseURL}/wps/portal/egov/registratsia`, openInNewTab: true, type: NotificationBarType.Error});
            return throwError(error?.error);
          }
          if (error.url.match(/(\/user-profile)/) && error?.error?.status === 404 && error?.error?.message.includes("USER_PROFILE_NOT_FOUND")) {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/user-profile)/) && error?.error?.status === 400 && error?.error?.message.includes("MISSING_USER_PIN")) {
            return throwError(error?.error);
          }
          if (error.url.match('nexu.js')) {
            return throwError(error?.error);
          }

          if(error?.status > 0 || error?.error?.status > 0) {

            let errorMessage = "ERRORS.500";

            if(this.hasTranslation(this.formatError(error))) {

              errorMessage = this.formatError(error);
              this.notificationsBannerService.hideAll();
              this.notificationsBannerService.show({message: errorMessage, type: Utils.getNotificationTypeFromMessage(errorMessage), additionalMessage: error?.error.data})

            } else {
              this.notificationsBannerService.show({message: errorMessage, type: NotificationBarType.Error})
            }

            return throwError(error?.error);
          }

          return throwError(error?.error);
        }),
      )
  }

  formatError(errorObj) {
    if (typeof errorObj === 'string') {
      return errorObj;
    } else {
      if (typeof errorObj.error === 'string') {
        return errorObj.error
      }
    }

    return errorObj.error?.message
  }

  hasTranslation(key: string): boolean {
    try {
      // Inject TranslateService here to prevent circular dependency when intercepting i18n.json loading
      const translateService = this.injector.get(TranslateService);
      const translation = translateService.instant(key);
      return translation !== key && translation !== '';
    } catch {
      return false;
    }
  }
}

export const httpErrorsProvider = {
  // use fake backend in place of Http service for backend-less development
  provide: HTTP_INTERCEPTORS,
  useClass: HttpErrorsInterceptor,
  multi: true
};
