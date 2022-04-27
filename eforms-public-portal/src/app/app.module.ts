import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { FeaturesModule } from './features/features.module';
import { fakeBackendProvider } from './core/interceptors/backend-mock.interceptor';
import { httpErrorsProvider } from './core/interceptors/http-errors.interceptor';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthConfigModule } from './core/auth/auth-config.module';
import { tokenProvider } from './core/interceptors/token.interceptor';
import { profileProvider } from './core/interceptors/selected-profile.interceptor';
import { loaderInterceptorProvider } from './core/interceptors/loader.interceptor';
import { MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material/dialog';
import { MaterialModule } from 'src/app/material/material.module'

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    FeaturesModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AuthConfigModule,
    MaterialModule,
  ],
  providers: [httpErrorsProvider, tokenProvider, profileProvider, loaderInterceptorProvider, {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: true}}],
  bootstrap: [AppComponent]
})
export class AppModule { }
