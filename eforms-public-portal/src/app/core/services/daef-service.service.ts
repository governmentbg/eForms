import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { AutoTranslationResponse } from '../types/autoTranslationResponse';
import { DAEFDocument } from '../types/daef-document';
import { DAEFServiceResponse } from '../types/daefservice';
import { ServiceSubject } from '../types/serviceSubject';
import { TranslationResponse } from '../types/translation-response';

@Injectable({
  providedIn: 'root'
})
export class DAEFService {
  subject: BehaviorSubject<ServiceSubject>;
  private service: ServiceSubject;

  constructor(private http: HttpClient) {
    this.subject = new BehaviorSubject<ServiceSubject>(this.service);
    this.service = JSON.parse(localStorage.getItem('service'));
    this.subject.next(this.service)
  }

  public subscribe(callback: (model: ServiceSubject) => void): Subscription {
    return this.subject.subscribe(callback);
  }

  setService(serviceId: string) {
    this.service = {
      serviceId: serviceId,
      projectId: environment.formioBaseProject
    }
    localStorage.setItem('service', JSON.stringify(this.service));

    this.subject.next(this.service)
  }

  getServiceId() {
    return this.service?.serviceId
  }

  getDAEFService(id: string) {
    return this.http.get<DAEFServiceResponse>(`/api/projects/${environment.formioBaseProject}/eas/${id}`);
  }

  getDAEFServiceAssuranceLevel(id: string) {
    return this.http.get<any>(`/api/public/projects/${environment.formioBaseProject}/eas/${id}`, { headers: {} });
  }

  getFilteredDAEFService(id: string, value: any) {
    return this.http.get<DAEFServiceResponse>(`/api/projects/${environment.formioBaseProject}/eas/${id}/suppliers?title=${value}`);
  }

  getFullDAEFService(id: string, code: string) {
    return this.http.get<DAEFServiceResponse>(`/api/projects/${environment.formioBaseProject}/eas/${id}/suppliers/${code}`);
  }

  getCase(id: string) {
    return this.http.get<any>(`/api/projects/${environment.formioBaseProject}/cases/${id}`);
  }
  getCaseForAdmin(id: string) {
    return this.http.get<any>(`/api/admin/projects/${environment.formioBaseProject}/cases/${id}`);
  }
  getCaseDetails(id: string) {
    return this.http.get<any>(`/api/admin/projects/${environment.formioBaseProject}/cases/${id}/details`);
  }

  getSupplier(name: string) {
    return this.http.get<any>(`/api/project/${environment.formioBaseProject}/common/nom/servicesupplier/submission?data.name=${name}&select=data&limit=1`);
  }

  postTerminateProcess(businessKey: string) {
    let payload = { message: "TerminateProcessMessage" };

    return this.http.post<any>(`/api/projects/${environment.formioBaseProject}/terminate-process/${businessKey}`, payload);
  }

  postChangeTranslationStatus(targetLanguageCode: string, status: string = 'public', key: string = "") {
    let url = `/api/admin/projects/${environment.formioBaseProject}/translation/change-translations-status?status=${status}&languageCode=${targetLanguageCode}`;
    if (key) {
      url += `&key=${key}`
    }
    return this.http.post<any>(url, {});
  }

  getTranslationData(targetLanguageCode, key)
  {
    return this.http.get<TranslationResponse>(`/api/admin/projects/${environment.formioBaseProject}/translation/${targetLanguageCode}/${key}`)
  }

  postEditTranslation(targetLanguageCode: string, key: string, translation: string) {
    return this.http.post<any>(this.replaceAll(`/api/admin/projects/${environment.formioBaseProject}/translation?languageCode=${targetLanguageCode}&key=${key}&translation=${translation}`), {});
  }

  getAutoTranslationData(targetLanguageCode: string, key: string, status: string = "") {
    return this.http.get<AutoTranslationResponse>(`/api/admin/projects/${environment.formioBaseProject}/translation/check-translation-request/${targetLanguageCode}/${key}?status=${status}`)
  }

  postRequestAutoTranslation(targetLanguageCode: string, key: string) {
    return this.http.post<any>(`/api/admin/projects/${environment.formioBaseProject}/translation/issue-translation/${targetLanguageCode}/${key}`, {})
  }
  replaceAll(url){
    while(url.indexOf('{') !== -1 || url.indexOf('}') !== -1){
      url = url.replace('{','%7B').replace('}', '%7D')
    }
    return url
  }
  getDeliveryFilesPackage(processId: string, businessKey, isAdmin: boolean): Observable<DAEFDocument[]> {
    return this.http.get<any>(`/api/${isAdmin ? 'admin/' : ''}projects/${environment.formioBaseProject}/edelivery-files-package/process/${processId}?businessKey=${businessKey}`).pipe(
      map((response) => response.value),
      map((daefDocuments: any | any[]) => {
        for (let index = 0; index < daefDocuments.length; index++) {
          const currentDocument = daefDocuments[index];
          currentDocument.signeesList = [];
        }

        let signableDocuments = daefDocuments.filter(document => document.signable);
        
        return signableDocuments as DAEFDocument[];
      })
    )
  }
}