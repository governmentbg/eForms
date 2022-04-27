import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { BehaviorSubject, Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';
import { StartProcessRequest, ProcessInfo, ProcessResponse, LocalProccessVariable } from '../types/camunda-process';
import { DeepLinkService } from './deep-link.service';

@Injectable({
  providedIn: 'root'
})
export class CamundaProcessService {
  subject: BehaviorSubject<ProcessInfo>;
  private processInfo: ProcessInfo;

  constructor(
      private http: HttpClient,
      private router: Router,
      public oidcSecurityService: OidcSecurityService,
      public deepLinkService: DeepLinkService
    ) {
      this.subject = new BehaviorSubject<ProcessInfo>(this.processInfo);
   }

  public subscribe(callback: (model: ProcessInfo) => void): Subscription {
    return this.subject.subscribe(callback);
  }

  getProcess(id: any) {
    return this.http.get<ProcessResponse>(`/api/processes/${id}/map`);
  }

  getCurrentTask(id: any) {
    return this.http.get<ProcessResponse>(`/api/process/${id}/current-task`);
  }

  getProcessForAdmin(id: any) {
    return this.http.get<ProcessResponse>(`/api/admin/processes/${id}/map`);
  }

  startProcess(easId:string, requestBody: StartProcessRequest) {
    return this.http
      .post<ProcessInfo>(`/api/projects/${environment.formioBaseProject}/start-process/${easId}`, requestBody)
      .subscribe((response) => {
        this.processInfo = response;
        this.subject.next(response);
        this.deepLinkService.deleteParam("easId");
        this.router.navigate(['my-services', 'process', this.processInfo.id]);
      });
  }
  startMetadataProcess(requestBody: StartProcessRequest) {
    return this.http
    .post<ProcessInfo>(`/api/admin/start-process/Metadata_process`, requestBody, {
      headers: {
        'Cache-Control': 'no-cache'
      }})
    .subscribe((response) => {
      this.processInfo = response;
      this.subject.next(response);
      this.deepLinkService.deleteParam("easId");
      this.router.navigate(['administration-of-services', 'process', this.processInfo.id]);
    });
  }
  setLocalVariableInProcess(taskId:string, variable:any){
    let filteredVariable = JSON.parse(JSON.stringify(variable))
    filteredVariable = this.clean(filteredVariable)
    return this.http.post<any>(`/api/tasks/${taskId}/localVariables`, filteredVariable)
  }
  getLocalVariableFromProcess(taskId:string, variableName:string){
    return this.http.get(`/api/tasks/${taskId}/localVariables/${variableName}`)
  }
  
  getVariableFromProcess(taskId:string, variableName:string){
    return this.http.get(`/api/tasks/${taskId}/variables/${variableName}`)
  }
  clean(obj) {
    obj = JSON.parse(JSON.stringify(obj))
    for (var propName in obj) {
      if (obj[propName] === null || obj[propName] === undefined || obj[propName] === '') {
        delete obj[propName];
      }
      if (typeof obj[propName] === 'object'){
        obj[propName] = this.clean(obj[propName])
      }
    }
    return obj
  }
}
