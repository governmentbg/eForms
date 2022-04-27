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
          if (error?.status === 401) {
            this.notificationsBannerService.show({message: "ERRORS.INVALID_GRANT", type: NotificationBarType.Error});
            return throwError(error?.error);
          }
          if (error?.error === 'invalid_grant') {
            this.notificationsBannerService.show({message: "ERRORS.INVALID_GRANT", type: NotificationBarType.Error});
            return throwError(error?.error);
          }
          if(error?.error?.message === 'PAYMENT_IS_STILL_PENDING'){
            return throwError(error?.error);
          }
          if(error?.error?.message === 'NOT_SATISFIED_ASSURANCE_LEVEL'){
            return throwError(error?.error);
          }
          if (error?.status === 403) {
            if (error.url.match(/(\/current-task)/)) {
              return throwError(error?.error);
            }
            if(error.url.match(/(\/start-process)/)){
                if(error?.error?.message === 'PROFILE_NOT_FOUND'){
                    this.notificationsBannerService.show({message: "SERVICES.ERRORS.USER_PROFILE_NOT_FOUND", type: NotificationBarType.Error, actionText: "HERE", url: `https://${environment.edeliveryURL}`, openInNewTab: true })
                    return throwError(error?.error);
                }
                if(error?.error?.message === 'SERVICE_NOT_AVAILABLE'){
                    this.notificationsBannerService.show({message: "SERVICES.ERRORS.EDELIVERY_NOT_AVAILABLE", type: NotificationBarType.Error });
                    return throwError(error?.error);
                }
                if(error?.error?.message === 'NOT_AUTHORIZED'){
                    this.notificationsBannerService.show({message: "SERVICES.ERRORS.USER_PROFILE_NOT_AUTHORIZED", type: NotificationBarType.Error});
                    return throwError(error?.error);
                }
            }
            this.router.navigate(['403']);
          }
          if (error?.error?.status === 500 && error?.error?.message === "ORN_NOT_AVAILABLE") {
            this.notificationsBannerService.show({message: "ERRORS.ORN_NOT_AVAILABLE", type: NotificationBarType.Error});
            return throwError(error?.error);
          }
          if (error?.error?.status === 500 && error?.error?.message === "MISSING_SIGNEES_PROFILES") {
            let postfix = error?.error.data.length === 1 ? '1' : 'N';
            let placeholders = {pin: error?.error.data.join()};
            this.notificationsBannerService.hideAll();
            this.notificationsBannerService.show({message: "ERRORS.MISSING_SIGNEES_PROFILES_" + postfix, type: NotificationBarType.Error, placeholders: placeholders});
            return throwError(error?.error);
          }
          if (error.url.match(/(\/localVariables)/) && error?.error?.status === 404) {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/current-task)/) && error?.error?.status === 404 && error?.error?.message === "RESOURCE_NOT_FOUND") {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/eas)/) && error?.error?.status === 400 && error?.error?.message === "SERVICE_NOT_ACTIVE") {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/eas)/) && error?.error?.status === 404 && error?.error?.message === "RESOURCE_NOT_FOUND") {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/eas)/) && error?.error?.status === 404 && error?.error?.message === "USER_PROFILE_NOT_FOUND") {
            return throwError(error?.error);
          }
          if(error.url.match(/(\/eas)/)) {
            this.notificationsBannerService.show({message: "ERRORS.TASK_ACTION_NOT_FOUND", type: NotificationBarType.Error});
            return throwError(error?.error);
          }
          if(error.url.match(/(\/unclaim)/)) {
            this.notificationsBannerService.show({message: "ERRORS.TASK_ACTION_NOT_FOUND", type: NotificationBarType.Error});
            return throwError(error?.error);
          }      
          if(error.url.match(/(\/claim)/)) {
            this.notificationsBannerService.show({message: "ERRORS.TASK_ACTION_NOT_FOUND", type: NotificationBarType.Error});
            return throwError(error?.error);
          }
          if(error.url.match(/(admin\/user-profiles)/)) {
            this.notificationsBannerService.show({message: "ERRORS.TASK_ACTION_NOT_FOUND", type: NotificationBarType.Error});
            return throwError(error?.error);
          }   
          if(error.url.match(/(\/cases)/)) {
            this.notificationsBannerService.show({message: "ERRORS.TASK_ACTION_NOT_FOUND", type: NotificationBarType.Error});
            return throwError(error?.error);
          }
          if(error.url.match(/(\/map)/)) {
            this.notificationsBannerService.show({message: "ERRORS.500", type: NotificationBarType.Error})
            return throwError(error?.error);
          }
          if (error.url.match(/(\/borica)/)) {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/evrotrust)/)) {
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
          }
          if (error.url.match(/(\/user-profile\/update)/) && error?.error?.status === 500 && error?.error?.message === '') {
            this.notificationsBannerService.hideAll();
            this.notificationsBannerService.show({message: "ERRORS.NO_USER_PROFILE_INFO", type: NotificationBarType.Error});
            return throwError(error?.error);
          }
          if((error?.error?.status === 500 || error?.status === 500) && !this.hasTranslation("ERRORS." + this.formatError(error))) {
            this.notificationsBannerService.show({message: "ERRORS.500", type: NotificationBarType.Error})
            return throwError(error?.error);
          }
          if(error.url.match(/(signature\/digest\/data)/) || error.url.match(/(signature\/digest\/sign)/) ) {
            this.notificationsBannerService.show({message: "ERRORS.SIGN_SERVICE_IS_UNAVAILABLE", type: NotificationBarType.Error})
            return throwError(error?.error);
          }

          if (error.url.match(/(\/user-profile)/) && error?.error?.status === 404 && error?.error?.message === "USER_PROFILE_NOT_FOUND") {
            return throwError(error?.error);
          }
          if (error.url.match(/(\/user-profile)/) && error?.error?.status === 400 && error?.error?.message === "MISSING_USER_PIN") {
            return throwError(error?.error);
          }
          if (error.url.match('nexu.js')) {
            return throwError(error?.error);
          }
          if (error?.status > 0 || error?.error?.status > 0) {
            this.notificationsBannerService.show({message: "ERRORS." + this.formatError(error), type: NotificationBarType.Error})
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
