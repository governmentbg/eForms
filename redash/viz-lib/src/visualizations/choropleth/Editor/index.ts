import createTabbedEditor from "@/components/visualizations/editor/createTabbedEditor";

import GeneralSettings from "./GeneralSettings";
import ColorsSettings from "./ColorsSettings";
import FormatSettings from "./FormatSettings";
import BoundsSettings from "./BoundsSettings";
import { t } from "@/locales";

export default createTabbedEditor([
  { key: "General", title: t("General"), component: GeneralSettings },
  { key: "Colors", title: t("Colors"), component: ColorsSettings },
  { key: "Format", title: t("Format"), component: FormatSettings },
  { key: "Bounds", title: t("Bounds"), component: BoundsSettings },
]);
