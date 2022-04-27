import { Formio, Utils } from 'formiojs';
var XHR = {
    trim: function (text) {
        return Utils._.trim(text, '/');
    },
    path: function (items) {
        return items.filter(function (item) { return !!item; }).map(this.trim).join('/');
    },
    upload: function (formio, type, xhrCb, file, fileName, dir, progressCallback, groupPermissions, groupId) {
        return new Formio.Promise((function (resolve, reject) {
            var response_1 = { data: file };
            // Send the file with data.
            var xhr_1 = new XMLHttpRequest();
            if (typeof progressCallback === 'function') {
                xhr_1.upload.onprogress = progressCallback;
            }

          xhr_1.openAndSetHeaders = function () {
            xhr_1.open.apply(xhr, arguments);
            setXhrHeaders(formio, xhr_1);
          }; 
            // Fire on network error.
            xhr_1.onerror = function (err) {
                err.networkError = true;
                reject(err);
            };
            // Fire on network abort.
            xhr_1.onabort = function (err) {
                err.networkError = true;
                reject(err);
            };
            // Fired when the response has made it back from the server.
            xhr_1.onload = function () {
                if (xhr_1.status >= 200 && xhr_1.status < 300) {
                    resolve(response_1);
                }
                else {
                    reject(xhr_1.response || 'Unable to upload file');
                }
            };
            // Set the onabort error callback.
            xhr_1.onabort = reject;
            // Get the request and send it to the server.
            xhr_1.send(xhrCb(xhr_1, response_1));
        }));
    },
};
export default XHR;
