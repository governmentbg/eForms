import _ from "lodash";
import zh_CN from "@/locales/bg_BG";

export function localeList() {
  return [
    {
      code: "bg",
      name: "Български",
      locale: zh_CN,
    },
    {
      code: "en-US",
      name: "English",
    },
  ];
}

export function findLocale(code) {
  return _.find(localeList(), ["code", code]);
}
