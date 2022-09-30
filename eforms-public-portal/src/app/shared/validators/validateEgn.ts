import { AbstractControl, ValidatorFn } from "@angular/forms";

export function ValidatePinOrEgn(): ValidatorFn {  
    return (control: AbstractControl): { [key: string]: any } | null => {
  
      if(ValidatorEGN.validate(control.value) || PersonalIdentificationNumber.validate(control.value)){
        return null;
      } else {
        return {pinOrEgnError: control.value};
      }
    }
        
}


function isValidCheckSum(checkSum, pin){
    return checkSum === Number(pin.charAt(9));
}

function isValidLength(pin){
    return pin.length === 10;
}

function containsIntegerOnly(pin){
    return /[0-9]/.test(pin);
}

let ValidatorEGN = (function() {

    /**
    * Check Bulgarian EGN codes for validity
    *
    * @param  {String} egn
    * @return {Boolean}
    */
    function validate(egn) {

        let date = initDate(egn);
        let checkSum = computeCheckSum(egn);

        if(isValidLength(egn) &&
          containsIntegerOnly(egn) &&
          isValidDate(date['year'], date['month'], date['day']) &&
          isValidCheckSum(checkSum, egn)){
              return true;
        }else{
            return false;
        }
    };

    function initDate(egn){
        let date = {};

        let year = Number(egn.slice(0, 2));
        let month = Number(egn.slice(2, 4));
        let day = Number(egn.slice(4, 6));

        if (month >= 40) {
            year += 2000;
            month -= 40;
        } else if (month >= 20) {
            year += 1800;
            month -= 20;
        } else {
            year += 1900;
        }

        date['year'] = year;
        date['month'] = month;
        date['day'] = day;

        return date;
    }

    function isValidDate(y, m, d) {
        let date = new Date(y, m - 1, d);
        return date && 
              (date.getMonth() + 1) == m && date.getDate() == Number(d) &&
              checkGregorianCalendarFirstAdoption(y, m, d);
    }

    // Gregorian calendar adoption in 1916 in Bulgaria
    // 31/03/1916 > +13 days > 14/04/1916
    function checkGregorianCalendarFirstAdoption(year, month, day){
        if (year === 1916 &&
            month === 4 &&
            day <= 13){
                return false;
        }else{
            return true;
        } 
    }

    function computeCheckSum(egn){
        let checkSum = 0;
        let weights = [2,4,8,5,10,9,7,3,6];

        for (let i = 0; i < weights.length; ++i) {
            checkSum += weights[i] * Number(egn.charAt(i));
        }

        checkSum %= 11;
        checkSum %= 10;

        return checkSum;
    }

    return {
        validate: validate,
        computeCheckSum: computeCheckSum,
        isValidDate: isValidDate
    }
})();


let PersonalIdentificationNumber = (function() {

    function validate(pin) {

        let checkSum = computeCheckSum(pin);

        if(isValidLength(pin) &&
          containsIntegerOnly(pin) &&
          isValidCheckSum(checkSum, pin)){
            return true;
        }
        
        return false;
    };

    function computeCheckSum(pin){
        let checkSum = 0;
        let weights = [21,19,17,13,11,9,7,3,1];

        for (let i = 0; i < weights.length; ++i) {
            checkSum += weights[i] * Number(pin.charAt(i));
        }

        checkSum %= 10;

        // Ако полученият остатък от checkSum е число по-малко от 10, то става контролно число.
        // Ако е равно на 10, контролното число е 0
        checkSum %= 10;
        return checkSum;
    }

    return {
        validate: validate,
        computeCheckSum: computeCheckSum
    };
})();

