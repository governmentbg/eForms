import { NotificationBarType } from "../components/notifications-banner/notification-banner.model";

export default class Utils {


    /**
    * Parses a given url with query parameters to an object.
    * 
    * @param {string} url - The url from which we need to get the parameters
    * @returns An object of the found query parameters
    */
    static parseQueryParamsToObject(url: string): {} {
        let result = {};
        url = url.substr(url.indexOf('?') + 1, url.length);
        url.split('&').forEach((value: string) => {
        let splitValue = value.split('=');
        result[splitValue[0]] = splitValue[1];
        });
        return result;
    }

  /**
   * Get the api path of the form formated to snake case
   * @param formPath
  */
   static getFormDataSubmissionKey(formPath : string) : string{
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

  /**
   * Formats and returns the notification type 
   * extracted from a BE error message.
   * 
   * @param errMessage 
   * @returns 
   */
  static getNotificationTypeFromMessage(errMessage: string) {
    
    let notificationType = NotificationBarType.Error;

    if(errMessage) {
      let errPrefix = errMessage.split(".")[0];

      if(errPrefix) {
        let errPrefixCapitalized = errPrefix.charAt(0).toUpperCase() + errPrefix.slice(1);

        switch (errPrefixCapitalized) {
          case 'ERROR':
            notificationType = NotificationBarType.Error;
            break;

          case 'WARN':
            notificationType = NotificationBarType.Warn;
            break;
        
          case 'INFO':
            notificationType = NotificationBarType.Info;
            break;
        }
      }

    }
    return notificationType;
  }
}



