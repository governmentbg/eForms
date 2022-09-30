import i18n from "i18next";
import { initReactI18next } from "react-i18next";
 
import EN from "./locales/en.json";
import BG from "./locales/bg.json";
 
i18n
.use(initReactI18next)
.init({
resources: {
    en: {translation: EN},
    bg: {translation: BG}
}
});
 
var userLang = navigator.language || navigator.userLanguage;
userLang = userLang.substr(0,2);

if (['en','bg'].indexOf(userLang) > -1) {
    i18n.changeLanguage(userLang);
} else {
    i18n.changeLanguage('bg');
}
