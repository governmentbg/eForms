import createTabbedEditor from "@/components/visualizations/editor/createTabbedEditor";

import ColumnsSettings from "./ColumnsSettings";
import GridSettings from "./GridSettings";

import "./editor.less";
import { t } from "@/locales";

export default createTabbedEditor([
  { key: "Columns", title: t("Columns"), component: ColumnsSettings },
  { key: "Grid", title: t("Grid"), component: GridSettings },
]);
