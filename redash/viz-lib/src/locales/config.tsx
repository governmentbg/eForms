import _ from "lodash";
const bg_BG = require('./bg_BG.js');

export function localeList() {
  return [
    {
      code: "bg",
      name: "Български",
      locale: bg_BG,
    },
    {
      code: "en-US",
      name: "English",
    },
  ];
}

export function findLocale(code: string) {
  return _.find(localeList(), ["code", code]);
}

const langs = ["en-US", "bg"];

const getLang = () => {
  let lang = navigator.language

  if (!langs.includes(lang)) {
    lang = "en-US";
  }

  return lang;
};

getLang();

export function gettext(string: string, ...args: any) {
  const lang = getLang();
  const fLocal = findLocale(lang);

  if (!fLocal) {
    return string;
  }
  const locale = fLocal.locale;
  if (!locale) {
    return string;
  } else {
    const tran = locale.default[string];
    if (!tran) return string;
    else return tran;
  }

}

export const t = gettext;
