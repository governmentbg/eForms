import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { DataToSign } from '../types/data-to-sign';
import { fileExtentionsWithAttachedSignatures } from '../types/file-extentions-with-attached-signatures';

@Injectable({
  providedIn: 'root'
})
export class SignService {

  public nexULoaded = false;

  constructor(private http: HttpClient) {}

   public getNexuJS () {
     if(!this.nexULoaded) {
      this.http.get(`http://localhost:9795/nexu.js`,{responseType: 'text'}).subscribe(result => {
        var se = document.createElement('script');
        se.type = "text/javascript";
        se.text = result;
        document.getElementsByTagName('head')[0].appendChild(se);
        this.nexULoaded = true
      });
     }
   }

   public digestData(data: DataToSign) {
    let method = this.shouldFileBeSignedWithAttachedSignatures(data.documentName) ? 'api/signature/document/data': 'api/signature/digest/data'
    return this.http.post<any>(method, data)
   }

   public signData(data: DataToSign) {
    let method = this.shouldFileBeSignedWithAttachedSignatures(data.documentName) ? 'api/signature/document/sign': 'api/signature/digest/sign'
    return this.http.post<any>(method, data)
   }

   public shouldFileBeSignedWithAttachedSignatures(fileName: string){
    if(!fileName){
      return false
    }
    let fileExtentionsAsArray = Object.values(fileExtentionsWithAttachedSignatures)
    let fileExt = fileName.split('.').pop()
    return fileExtentionsAsArray.includes(fileExt)
   }

   public uploadFileToMinIo(file, businessKey, formId ){
    const formData = new FormData();
    formData.append('file', file);
    let context = JSON.parse(localStorage.getItem('formContext'))
    let documentId = context?.parrentForm ? '&documentId=' + context.parrentForm : ''
    return this.http.put<any>(`api/project/${environment.formioBaseProject}/file?businessKey=${businessKey}&formId=${formId}&filename=${file.name}${documentId}`, formData)
   }

   public getSignMethodBorica(personIdentifier: string){
    return this.http.get<any>(`/api/borica/cert/identity/EGN/${personIdentifier}`).toPromise();
   }
   public getSignMethodEvrotrust(personIdentifier: string){
    return this.http.get<any>(`/api/evrotrust/user/check/${personIdentifier}`).toPromise();
   }
   public signDataBorica(personIdentifier: string, data: any) {
    return this.http.post<any>(`/api/borica/sign?rpToClientAuthorization=personalId:${personIdentifier}`, data)
   }
   public signDataEvrotrust(data: any) {
    return this.http.post<any>(`/api/evrotrust/document/sign`, data)
   }

   public checkSignedData(signSupplier: string, callbackId: any) {
      let apiUrl = `/api/borica/sign/${callbackId}`;
      if (signSupplier == 'evrotrust'){
        apiUrl = `/api/evrotrust/document/status/${callbackId}`
      }
      return this.http.get<any>(apiUrl)
   }
   public getSignedData(signSupplier: string, callbackId: any) {
      let apiUrl = `/api/borica/sign/content/${callbackId}`;
      if (signSupplier == 'evrotrust'){
        apiUrl = `/api/evrotrust/document/download/${callbackId}`
      }
      return this.http.get<any>(apiUrl)
   }

   public getNexuInstaller(os: string){
      return this.http.get<any>(`/api/project/${environment.formioBaseProject}/common/resource/nexu-installer-upload/submission?data.installerKey=${os}&select=data&limit=1`);
   }

  downloadInstaller(installerPath: string) {  
    const httpOptions = {
      responseType: 'blob' as 'json',
    };
    return this.http.get(`${environment.apiUrl}/project/${environment.formioBaseProject}/${installerPath}`, httpOptions);
  }

}