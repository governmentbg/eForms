import { APP_INITIALIZER, NgModule } from '@angular/core';
import { AuthModule, LogLevel, OidcConfigService } from 'angular-auth-oidc-client';
import { environment } from '../../../environments/environment';

export function configureAuth(oidcConfigService: OidcConfigService): () => Promise<any> {
    return () =>
        oidcConfigService.withConfig({
              stsServer: environment.stsServer,
              redirectUrl: window.location.origin + '/login',
              postLogoutRedirectUri: window.location.origin + '/login',
              clientId: environment.clientId,
              scope: 'openid profile email roles',
              responseType: 'code',
              silentRenew: true,
              useRefreshToken: true,
              renewTimeBeforeTokenExpiresInSeconds: 30,
              logLevel: environment.production ? LogLevel.None : LogLevel.Debug,
              startCheckSession: false,
              triggerAuthorizationResultEvent: true,
              historyCleanupOff: true,
          });
}

@NgModule({
    imports: [AuthModule.forRoot()],
    exports: [AuthModule],
    providers: [
        OidcConfigService,
        {
            provide: APP_INITIALIZER,
            useFactory: configureAuth,
            deps: [OidcConfigService],
            multi: true,
        },
    ],
})
export class AuthConfigModule {}
