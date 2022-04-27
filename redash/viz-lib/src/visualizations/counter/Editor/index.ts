import createTabbedEditor from "@/components/visualizations/editor/createTabbedEditor";

import GeneralSettings from "./GeneralSettings";
import FormatSettings from "./FormatSettings";
import { t } from "@/locales";

export default createTabbedEditor([
  { key: "General", title: t("General"), component: GeneralSettings },
  { key: "Format", title: t("Format"), component: FormatSettings },
]);
