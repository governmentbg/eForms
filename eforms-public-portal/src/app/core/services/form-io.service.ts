import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { IoForm } from '../types/io-form';
import { DeepLinkService } from './deep-link.service';
import {Observable} from "rxjs";
import * as moment from 'moment';

@Injectable({
  providedIn: 'root'
})
export class FormIoService {

  constructor(private http: HttpClient,
    private deepLinkService: DeepLinkService) { }

  getForm(id: any, project: string) {
    id = id.replace('formio:/','');
    return this.http.get<IoForm>(`api/project/${project}/form/${id}`);
  }
  getFormByAlias(alias: any, project: string) {
    if (project === null) {
      project = environment.formioBaseProject;
    }
   return this.http.get<IoForm>(`api/project/${project}/${alias}`);
  }
  postForm(id: any, data:any) {
    let filteredData = JSON.parse(JSON.stringify(data))
    filteredData = this.clean(filteredData)
    return this.http.post<IoForm>(`/api/tasks/${id}/complete`, filteredData);
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
  

  getFormSubmitionByBuissnessKey(formPath: string, businessKey: string, limitValue = 1, sortValue = '-modified', withApplicant = true){
    let selectedProfile = this.deepLinkService.getParam('selectedProfile');
    let applicant = ''
    if(selectedProfile){
      if(withApplicant) {
        applicant = `&data.applicant=${selectedProfile.identifier}`
      } else {
        applicant = `&data.applicant__exists=false`
      }
      
    }
    return this.http.get<any>(`api/project/${environment.formioBaseProject}/${formPath}/submission?data.businessKey=${businessKey}${applicant}&limit=${limitValue}&sort=${sortValue}`);
  }

  getFormIoSubmissionsBySupplierId(formId: string, arId: string, serviceSupplierCode: string): Observable<any>{
    return this.http.get(`${environment.apiUrl}/project/${environment.formioBaseProject}/${formId}/submission?data.arId=${arId}&data.supplierEAS=${serviceSupplierCode}`);
  }

  getFormIoResource(formId : string, filter = '') {
    return this.http.get(`${environment.apiUrl}/project/${environment.formioBaseProject}/${formId}/submission?limit=999999${filter ? '&' + filter : ''}`);
  }
  getFormIoSubmissionById(formId : string, id : string) {
    return this.http.get(`${environment.apiUrl}/project/${environment.formioBaseProject}/${formId}/submission/${id}`);
  }

  getFormIoSupplier(code : string) {
    return this.http.get(`${environment.apiUrl}/project/${environment.formioBaseProject}/common/nom/service-supplier/submission?data.code=${code}&select=data`);
  }
  getFormIoSupplierByIdentifier(identifier : string) {
    return this.http.get(`${environment.apiUrl}/project/${environment.formioBaseProject}/common/nom/service-supplier/submission?data.eik=${identifier}&select=data`);
  }

  submitMetaData(data) {
    return this.http.post(`${environment.apiUrl}/admin/projects/${environment.formioBaseProject}/metadata/update`, data);
  }
  getEASforSupplier(statusCodes: Array<number>, fromIssueDate, toIssueDate = '', administrationUnitEDelivery = '', withLoader = false){
    let statusCode = statusCodes.join(',')
    let unit = administrationUnitEDelivery ? '&administrationUnitEDelivery=' + administrationUnitEDelivery : ''
    let fromIssueDateParam = ''
    if (fromIssueDate) {
      fromIssueDateParam = '&fromIssueDate='
      fromIssueDateParam += moment(fromIssueDate).format("YYYY-MM-DD");
      fromIssueDateParam += 'T00:00:00';
    }    
    let toIssueDateParam = ''
    if (toIssueDate) {
      toIssueDateParam = '&toIssueDate='
      toIssueDateParam += moment(toIssueDate).format("YYYY-MM-DD");
      toIssueDateParam += "T23:59:59";
    }
    let headers = {}
    if(!withLoader) {
      headers = {
        'Hide-Loader': ''
      }
    }
    return this.http.get(`${environment.apiUrl}/admin/projects/${environment.formioBaseProject}/eas/identifiers?caseStatus=${statusCode}${fromIssueDateParam}${toIssueDateParam}${unit}`, {headers: headers});
  }
}
