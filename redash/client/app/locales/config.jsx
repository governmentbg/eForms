import { findLocale } from "@/locales/config";

const langs = ["en-US", "bg"];

const getLang = () => {
  let lang = navigator.language || navigator.userLanguage;

  if (!langs.includes(lang)) {
    lang = "en-US";
  }

  return lang;
};

getLang();

export function gettext(string, ...args) {
  const lang = getLang();
  const fLocal = findLocale(lang);
  if (!fLocal) {
    return string;
  }
  const locale = fLocal.locale;
  if (!locale) {
    return string;
  } else {
    const tran = locale[string];
    if (!tran) return string;
    else return tran;
  }
}

export const t = gettext;
