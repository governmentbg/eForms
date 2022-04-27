import createTabbedEditor from "@/components/visualizations/editor/createTabbedEditor";

import GeneralSettings from "./GeneralSettings";
import GroupsSettings from "./GroupsSettings";
import FormatSettings from "./FormatSettings";
import StyleSettings from "./StyleSettings";
import { t } from "@/locales";

export default createTabbedEditor([
  { key: "General", title: t("General"), component: GeneralSettings },
  { key: "Groups", title: t("Groups"), component: GroupsSettings },
  { key: "Format", title: t("Format"), component: FormatSettings },
  { key: "Style", title: t("Style"), component: StyleSettings },
]);
