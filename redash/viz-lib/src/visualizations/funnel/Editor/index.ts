import createTabbedEditor from "@/components/visualizations/editor/createTabbedEditor";

import GeneralSettings from "./GeneralSettings";
import AppearanceSettings from "./AppearanceSettings";
import { t } from "@/locales";

export default createTabbedEditor([
  { key: "General", title: t("General"), component: GeneralSettings },
  { key: "Appearance", title: t("Appearance"), component: AppearanceSettings },
]);
