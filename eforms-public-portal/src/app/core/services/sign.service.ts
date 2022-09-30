import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { DataToSign } from '../types/data-to-sign';
import { fileExtentionsWithAttachedSignatures } from '../types/file-extentions-with-attached-signatures';
import { signMethods } from '../types/sign-methods';

@Injectable({
  providedIn: 'root'
})
export class SignService {

  public nexULoaded = false;

  constructor(private http: HttpClient) { }

  public getNexuJS() {
    if (!this.nexULoaded) {
      this.nexuJS().subscribe(result => {
        var se = document.createElement('script');
        se.type = "text/javascript";
        se.text = result;
        document.getElementsByTagName('head')[0].appendChild(se);
        this.nexULoaded = true
      });
    }
  }

  public nexuJS() {
    return this.http.get(`http://localhost:9795/nexu.js`, { responseType: 'text' })
  }

  public digestData(data: DataToSign, hideLoader: boolean = false) {
    let method = this.shouldFileBeSignedWithAttachedSignatures(data.documentName) ? 'api/signature/document/data' : 'api/signature/digest/data'

    let customHeaders = {}
    if(hideLoader){
      customHeaders = {headers: {
        'Hide-Loader': ''
      }}
    }

    return this.http.post<any>(method, data, customHeaders);
  }

  public signData(data: DataToSign, hideLoader: boolean = false) {
    let method = this.shouldFileBeSignedWithAttachedSignatures(data.documentName) ? 'api/signature/document/sign' : 'api/signature/digest/sign'
    
    let customHeaders = {}
    if(hideLoader){
      customHeaders = {headers: {
        'Hide-Loader': ''
      }}
    }

    return this.http.post<any>(method, data, customHeaders)
  }

  public shouldFileBeSignedWithAttachedSignatures(fileName: string) {
    if (!fileName) {
      return false
    }
    let fileExtentionsAsArray = Object.values(fileExtentionsWithAttachedSignatures)
    let fileExt = fileName.split('.').pop()
    return fileExtentionsAsArray.includes(fileExt)
  }

  public uploadFileToMinIo(file, businessKey, formId, hideLoader: boolean = false) {
    const formData = new FormData();
    formData.append('file', file);
    
    let customHeaders = {}
    if(hideLoader){
      customHeaders = {headers: {
        'Hide-Loader': ''
      }}
    }

    return this.http.put<any>(`api/project/${environment.formioBaseProject}/file?businessKey=${businessKey}&formId=${formId}&filename=${file.name}`, formData, customHeaders)
  }

  public getSignMethodBorica(personIdentifier: string) {
    return this.http.get<any>(`/api/borica/cert/identity/EGN/${personIdentifier}`).toPromise();
  }
  public getSignMethodEvrotrust(personIdentifier: string) {
    return this.http.get<any>(`/api/evrotrust/user/check/${personIdentifier}`).toPromise();
  }
  public signDataBorica(personIdentifier: string, data: any) {
    return this.http.post<any>(`/api/borica/sign?rpToClientAuthorization=personalId:${personIdentifier}`, data)
  }
  public signDataEvrotrust(data: any) {
    return this.http.post<any>(`/api/evrotrust/document/sign`, data)
  }
  public signWithCloudSignature(personIdentifier: string, data: any, signMethod: string) {
    switch (signMethod) {
      case signMethods.evrotrust.value:
        return this.signDataEvrotrust(data);

      case signMethods.borica.value:
        return this.signDataBorica(personIdentifier, data);
      default:
        throw 'Unknown sign method';
    }
  }

  public checkSignedDataBorica(callbackId: string) {
    return this.http.get<any>(`/api/borica/sign/${callbackId}`);
  }
  public checkSignedDataEvrotrust(callbackId: string, isGroupSigning: boolean) {
    return this.http.get<any>(`/api/evrotrust/document/status/${callbackId}/${isGroupSigning}`);
  }
  public getSignedDataBorica(callbackId: string) {
    return this.http.get<any>(`/api/borica/sign/content/${callbackId}`);
  }
  public getSignedDataEvrotrust(callbackId: string, isGroupSigning: boolean) {
    return this.http.get<any>(`/api/evrotrust/document/download/${callbackId}/${isGroupSigning}`);
  }

  public getNexuInstaller(os: string) {
    return this.http.get<any>(`/api/project/${environment.formioBaseProject}/common/resource/nexu-installer-upload/submission`
      + `?data.installerKey=${os}&select=data&limit=1`);
  }

  downloadInstaller(installerPath: string) {
    const httpOptions = {
      responseType: 'blob' as 'json',
    };
    return this.http.get(`api/project/${environment.formioBaseProject}/${installerPath}`, httpOptions);
  }

  fetchFile(businessKey: string, formId: any, documentId: string, fileName: string, isAdmin: boolean) {
    return this.http.get(`api/${ isAdmin ? 'admin/' : '' }project/${environment.formioBaseProject}/file`
      + `?businessKey=${businessKey}${documentId}`
      + `&formId=${formId}`
      + `&filename=${fileName}`, { responseType: 'blob' }).pipe();
  }

  public fetchFileToBase64(ornNumber, documentId, formDataSubmissionKey, isConsolidatingForm, fileName, isAdmin): Promise<any> {
    return new Promise((resolve, reject) => {

      let documentIdParam = isConsolidatingForm ? `&documentId=${documentId}` : '';

      this.fetchFile(
        ornNumber,
        formDataSubmissionKey,
        documentIdParam,
        fileName,
        isAdmin).subscribe((response: Blob) => {
          const reader = new FileReader();
          reader.onloadend = () => {
            resolve({
              filename: fileName,
              content: reader.result as string,
              storage: 'base64',
              size: response.size,
              blob: response,
              contentType: response.type,
              key: `${ornNumber}/${formDataSubmissionKey}`,
              bucket: environment.formioBaseProject,
              fileExtension: fileName.split('.').pop()
            });
          };

          reader.readAsDataURL(response);
        }, (error) => {
          reject(error);
        });
    });
  }
  
  getFileNameAfterSign(file, jsonTimeStamp): string {
    if (this.shouldFileBeSignedWithAttachedSignatures(file.filename) || !jsonTimeStamp) {
      return file.filename;
    } else {
      let originalName = file.filename
      let nameArray = originalName.split('.')
      let fileExt = nameArray.pop()
      let name = nameArray.join('.')
      return `${name}-${fileExt}-${jsonTimeStamp}.p7s`
    }
  }
}