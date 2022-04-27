import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { IoForm } from '../types/io-form';
import { DeepLinkService } from './deep-link.service';

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

  getFormSubmitionByBuissnessKey(formPath: string, businessKey: string){
    let selectedProfile = this.deepLinkService.getParam('selectedProfile');
    let applicant = ''
    if(selectedProfile){
      applicant = `&data.applicant=${selectedProfile.identifier}`
    }
    return this.http.get<any>(`api/project/${environment.formioBaseProject}/${formPath}/submission?data.businessKey=${businessKey}${applicant}&sort=-modified&limit=1`);
  }
}
