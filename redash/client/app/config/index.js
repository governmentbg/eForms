import moment from "moment";
import { isFunction } from "lodash";

// Ensure that this image will be available in assets folder
import "@/assets/images/avatar.svg";

// Register visualizations
import "@redash/viz/lib";

// Register routes before registering extensions as they may want to override some
import "@/pages";

import "./antd-spinner";

let lang = (navigator.language || navigator.userLanguage) === 'bg' ? 'bg' : 'en';

let relativeTimeEn = {
  relativeTime: {
    future: "%s",
    past: "%s",
    s: "just now",
    m: "a minute ago",
    mm: "%d minutes ago",
    h: "an hour ago",
    hh: "%d hours ago",
    d: "a day ago",
    dd: "%d days ago",
    M: "a month ago",
    MM: "%d months ago",
    y: "a year ago",
    yy: "%d years ago",
  },
};

let relativeTimeBg = {
  relativeTime: {
    future: "%s",
    past: "%s",
    s: "сега",
    m: "преди минута",
    mm: "преди %d минути",
    h: "преди час",
    hh: "преди %d минути",
    d: "преди ден",
    dd: "преди %d дни",
    M: "преди месец",
    MM: "преди %d месеца",
    y: "преди година",
    yy: "преди %d години",
  }
};

moment.updateLocale("en", lang === 'bg' ? relativeTimeBg : relativeTimeEn);

function requireImages() {
  // client/app/assets/images/<path> => /images/<path>
  const ctx = require.context("@/assets/images/", true, /\.(png|jpe?g|gif|svg)$/);
  ctx.keys().forEach(ctx);
}

function registerExtensions() {
  const context = require.context("extensions", true, /^((?![\\/.]test[\\./]).)*\.jsx?$/);
  const modules = context
    .keys()
    .map(context)
    .map(module => module.default);

  return modules
    .filter(isFunction)
    .filter(f => f.init)
    .map(f => f());
}

requireImages();
registerExtensions();
