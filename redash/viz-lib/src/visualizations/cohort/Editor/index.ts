import createTabbedEditor from "@/components/visualizations/editor/createTabbedEditor";

import ColumnsSettings from "./ColumnsSettings";
import OptionsSettings from "./OptionsSettings";
import ColorsSettings from "./ColorsSettings";
import AppearanceSettings from "./AppearanceSettings";
import { t } from "@/locales";

export default createTabbedEditor([
  { key: "Columns", title: t("Columns"), component: ColumnsSettings },
  { key: "Options", title: t("Options"), component: OptionsSettings },
  { key: "Colors", title: t("Colors"), component: ColorsSettings },
  { key: "Appearance", title: t("Appearance"), component: AppearanceSettings },
]);
