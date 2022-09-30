import React from "react";
import ReactDOM from "react-dom";

import "@/config";

import ApplicationArea from "@/components/ApplicationArea";
import offlineListener from "@/services/offline-listener";
import ConfigProvider from "antd/lib/config-provider";
import bgBG from "antd/lib/locale/bg_BG";
import enUS from "antd/lib/locale/en_US";

ReactDOM.render((<ConfigProvider locale={(navigator.language || navigator.userLanguage) === 'bg' ? bgBG : enUS}>
  <ApplicationArea />
</ConfigProvider>), document.getElementById("application-root"), () => {
  offlineListener.init();
});
