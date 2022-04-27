import { Injectable } from '@angular/core';
import { get, isEmpty } from 'lodash';
import { BehaviorSubject, Subscription } from 'rxjs';
import { DeepLink } from '../types/deep-link';

@Injectable({
  providedIn: 'root'
})
export class DeepLinkService {
  protected subject: BehaviorSubject<DeepLink>;
  protected deepLink: DeepLink;

  constructor() {
    this.deepLink = { queryParams: {} };
    this.subject = new BehaviorSubject<DeepLink>(this.deepLink);
  }

  public subscribe(callback: (model: DeepLink) => void): Subscription {
    return this.subject.subscribe(callback);
  }

  public saveQueryParams(queryParams: string): void {
    let currentDeepLinkItems = JSON.parse(localStorage.getItem('deepLink')) ? JSON.parse(localStorage.getItem('deepLink')) : {}
    this.deepLink.queryParams = Object.assign(currentDeepLinkItems,JSON.parse(queryParams));
    localStorage.setItem('deepLink', JSON.stringify(this.deepLink.queryParams));
  }

  public setEasId (easId){
    this.deepLink.queryParams['easId'] = easId
  }
  
  public setParam(paramName: string, param: string):void {

    this.getQueryParams();
    let params = this.deepLink.queryParams;
    if (isEmpty(params)) {
      params = {};
    }
    params[paramName] = param;
    this.saveQueryParams(JSON.stringify(params));
  }
  public getParam(param: string) {
    this.getQueryParams()
    return get(this.deepLink.queryParams, param, '');
  }

  public deleteParam(param: string) {
    this.getQueryParams();
    let params = this.deepLink.queryParams;
    if (!isEmpty(params)) {
      delete params[param];
      localStorage.setItem('deepLink', JSON.stringify(this.deepLink.queryParams));
    }
  }

  public deleteAllParams(){
    this.deepLink.queryParams = {}
    localStorage.setItem('deepLink', JSON.stringify(this.deepLink.queryParams));
  }

  private getQueryParams(): void {
    this.deepLink.queryParams = JSON.parse(localStorage.getItem('deepLink'));
  }

}
