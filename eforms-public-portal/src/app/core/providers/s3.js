import { Formio } from 'formiojs';
import XHR from './xhr';
import { environment } from 'src/environments/environment';

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }
var _nativePromiseOnly = _interopRequireDefault(require("native-promise-only"));
function getBusinessKey(dir) {
    let dirArray = dir.split('/')
    return dirArray[0]
};
function getFormAlias(dir) {
    let dirArray = dir.split('/')
    dirArray.shift()
    return dirArray.join('/')
};

function getFormAliasFromKey(dir){
    let dirArray = dir.split('_')
    return dirArray.join('/')
}
function getDocumentParam(url = null) {
    if (url) {
        const urlParts = url.split('/');
        if (urlParts.length == 4) {
            return '&documentId=' + urlParts[1].replaceAll('_', '/');
        }
    }
    let context = JSON.parse(localStorage.getItem('formContext'))
    if (context && context.parrentForm) {
        return '&documentId=' + context.parrentForm
    } else {
        return ''
    }
}
function getDocumentId() {
    let context = JSON.parse(localStorage.getItem('formContext'))
    if (context && context.parrentForm) {
        return context.parrentForm
    } else {
        return ''
    }
}
function getApplicant() {
    let applicant = JSON.parse(localStorage.getItem('deepLink')).selectedProfile
    if (applicant){
        return '&applicant=' + applicant.identifier
    }
    return ''
};
function getFormDataSubmissionKey(formPath) {
    if (formPath.includes("?")) {
      formPath = formPath.substring(0, formPath.indexOf("?"));
    }
    let formPathArray = formPath.split("/");
    formPathArray.forEach(function (part, index) {
      this[index] = this[index].replace(/-([a-z])/g, function (g) {
        return g[1].toUpperCase();
      });
    }, formPathArray);
    return formPathArray.join("_");
}

var S3 = /** @class */ (function () {
    function S3(formio) {
        this.formio = formio;
    }
    S3.prototype.uploadFile = function (file, fileName, dir, progressCallback, url, options, fileKey, groupPermissions, groupId) {
        return XHR.upload(this.formio, 's3', function (xhr, response) {
            var fd = new FormData();
            for (var _i = 0, _a = Object.keys(response.data); _i < _a.length; _i++) {
                var key = _a[_i];
                fd.append(key, response.data[key]);
            }
            fd.append('file', file);
            let businessKey = getBusinessKey(dir)
            let formId = getFormAlias(dir)
            let documentId = getDocumentParam()
            xhr.open('POST', `${environment.apiUrl}/project/${environment.formioBaseProject}/file?businessKey=${businessKey}&formId=${formId}&filename=${file.name}${documentId}`);
            xhr.setRequestHeader('Authorization',  `Bearer ${JSON.parse(localStorage.getItem('deepLink')).accessToken}`);
            return fd;
        }, file, fileName, dir, progressCallback, groupPermissions, groupId).then(function (response) {
            let businessKey = getBusinessKey(dir)
            let formId = getFormDataSubmissionKey(getFormAlias(dir))
            let documentId = getFormDataSubmissionKey(getDocumentId())
            return {
                storage: 's3',
                name: file.originalName ? file.originalName : file.name,
                originalName: file.originalName ? file.originalName : file.name,
                bucket: environment.formioBaseProject,
                key: `${businessKey}/${formId}`,
                url: `${businessKey}/${documentId? documentId + '/' : ''}${formId}/${file.name}`,
                acl: 'public-read',
                size: file.size,
                type: file.type,
            };
        });
    };
    S3.prototype.downloadFile = async function (file) {
        var data = await new Promise((resolve, reject) => {
        let businessKey = getBusinessKey(file.key)
        let formId = getFormAlias(file.key)
        let applicant = getApplicant()
        let documentId = getDocumentParam(file.url)
        let url = `${environment.apiUrl}/project/${environment.formioBaseProject}/file?businessKey=${businessKey}&formId=${formId}&filename=${file.name}${documentId}${applicant}`
        fetch(url, {
            headers: {
                'authorization': `Bearer ${JSON.parse(localStorage.getItem('deepLink')).accessToken}`
            }
        }).then (response => {
            response.blob().then(blob => {
                const reader = new FileReader()
                reader.onloadend = () => {
                    resolve(reader.result)
                }
                reader.onerror = reject
                reader.readAsDataURL(blob)
            })
        })
        })
        let result = JSON.parse(JSON.stringify(file))
        result.url = data
        result.storage = 'base64';
        return _nativePromiseOnly.default.resolve(result);
    };
    S3.prototype.deleteFile = function deleteFile(fileInfo) {
        return new _nativePromiseOnly.default(function (resolve, reject) {
            var xhr = new XMLHttpRequest();
            let businessKey = getBusinessKey(fileInfo.key)
            let formId = getFormAliasFromKey(getFormAlias(fileInfo.key))
            let applicant = getApplicant()            
            let documentId = getDocumentParam()
            let url = `${environment.apiUrl}/project/${environment.formioBaseProject}/file?businessKey=${businessKey}&formId=${formId}&filename=${fileInfo.name}${documentId}${applicant}`
    
            xhr.open('DELETE',url, true);
            xhr.setRequestHeader('Authorization',  `Bearer ${JSON.parse(localStorage.getItem('deepLink')).accessToken}`);
      
            xhr.onload = function () {
              if (xhr.status >= 200 && xhr.status < 300) {
                resolve('File deleted');
              } else {
                reject(xhr.response || 'Unable to delete file');
              }
            };
    
            xhr.send(null);
          });
    };
    S3.title = 'AWS S3';
    return S3;
}());
export default S3;
