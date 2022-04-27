import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';
import { DAEFServiceResponse } from '../types/daefservice';
import { ServiceSubject } from '../types/serviceSubject';

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

  setService (serviceId: string){
    this.service = {
      serviceId: serviceId,
      projectId: environment.formioBaseProject
    }
    localStorage.setItem('service', JSON.stringify(this.service));

    this.subject.next(this.service)
  }

  getServiceId(){
    return this.service?.serviceId
  }

  getDAEFService(id: string) {
    return this.http.get<DAEFServiceResponse>(`/api/projects/${environment.formioBaseProject}/eas/${id}`);
  }

  getDAEFServiceAssuranceLevel(id: string) {
    return this.http.get<any>(`/api/public/projects/${environment.formioBaseProject}/eas/${id}`, {headers: {}});
  }

  getFilteredDAEFService(id: string, value: any) {
    return this.http.get<DAEFServiceResponse>(`/api/projects/${environment.formioBaseProject}/eas/${id}/suppliers?title=${value}`);
  }

  getFullDAEFService(id: string, code: string) {
    return this.http.get<DAEFServiceResponse>(`/api/projects/${environment.formioBaseProject}/eas/${id}/suppliers/${code}`);
  }

  getCase(id: string){
    return this.http.get<any>(`/api/projects/${environment.formioBaseProject}/cases/${id}`);
  }
  getCaseForAdmin(id: string){
    return this.http.get<any>(`/api/admin/projects/${environment.formioBaseProject}/cases/${id}`);
  }

  getSupplier(name: string){
    return this.http.get<any>(`/api/project/${environment.formioBaseProject}/common/nom/servicesupplier/submission?data.name=${name}&select=data&limit=1`);
  }
}
