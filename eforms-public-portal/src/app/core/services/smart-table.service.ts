import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SmartTableService {

  constructor(private http: HttpClient) {}
  getData(apiUrl: string, classifier: string, sortString: string, page: number, pageSize: number, parameters: string) {
    let url = apiUrl;
    if(classifier) {
      url += classifier;
    }
    if(sortString) {
      url += sortString;
    }
    if(parameters) {
      url += parameters;
    }
    return this.http.get(`${apiUrl}${classifier}sort=${sortString}&page=${page + 1}&size=${pageSize}${parameters}`);
  }

  getNonPageableData(apiUrl:string) {
    return this.http.get(apiUrl);
  }
}
