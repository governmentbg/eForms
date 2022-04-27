import _ from "lodash";
import { bg_BG } from "./bg_BG";

function localeList() {
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

function findLocale(code) {
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

export function t(string, defaultTrans?) {
  const lang = getLang();
  const fLocal = findLocale(lang);
  if (!fLocal) {
    return defaultTrans || string;
  }
  const locale = fLocal.locale;
  if (!locale) {
    return defaultTrans || string;
  } else {
    const tran = locale[string];
    if (!tran) return defaultTrans || string;
    else return tran;
  }
}
