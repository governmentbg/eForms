import Plotly from "plotly.js/lib/core";
// @ts-expect-error ts-migrate(7016) FIXME: Could not find a declaration file for module 'plot... Remove this comment to see the full error message
import bar from "plotly.js/lib/bar";
// @ts-expect-error ts-migrate(7016) FIXME: Could not find a declaration file for module 'plot... Remove this comment to see the full error message
import pie from "plotly.js/lib/pie";
// @ts-expect-error ts-migrate(7016) FIXME: Could not find a declaration file for module 'plot... Remove this comment to see the full error message
import histogram from "plotly.js/lib/histogram";
// @ts-expect-error ts-migrate(7016) FIXME: Could not find a declaration file for module 'plot... Remove this comment to see the full error message
import box from "plotly.js/lib/box";
// @ts-expect-error ts-migrate(7016) FIXME: Could not find a declaration file for module 'plot... Remove this comment to see the full error message
import heatmap from "plotly.js/lib/heatmap";
// @ts-expect-error ts-migrate(7016) FIXME: Implicitly has any type... Remove this comment to see the full error message
import * as bg from "plotly.js/lib/locales/bg"

import prepareData from "./prepareData";
import prepareLayout from "./prepareLayout";
import updateData from "./updateData";
import updateAxes from "./updateAxes";
import updateChartSize from "./updateChartSize";
import { prepareCustomChartData, createCustomChartRenderer } from "./customChartUtils";

if (navigator.language === 'bg') {
  bg.dictionary = {
    Autoscale: "Автоматично мащабиране",
    "Box Select": "Избиране",
    "Compare data on hover": "Сравняване на датата при задържане на курсора",
    "Double-click to zoom back out":
      "Натиснете два пъти, за да се отдалечите",
    "Download plot as a png": "Теглене на мащаба като png", // components/modebar/buttons.js:52
    "Edit in Chart Studio": "Редакритане", // components/modebar/buttons.js:76
    "IE only supports svg.  Changing format to svg.":
      "IE поддържа само svg. Сменяне на формата към svg.", // components/modebar/buttons.js:60
    "Lasso Select": "Ласо избиране", // components/modebar/buttons.js:112
    "Orbital rotation": "Орбитална ротация", // components/modebar/buttons.js:279
    Pan: "Наклоняване", // components/modebar/buttons.js:94
    "Produced with Plotly": "Създадено с Plotly", // components/modebar/modebar.js:256
    Reset: "Нулиране", // components/modebar/buttons.js:431
    "Reset axes": "Нулиране на осите", // components/modebar/buttons.js:148
    "Show closest data on hover": "Показване на най-близката дата при задържане на курсора", // components/modebar/buttons.js:157
    "Snapshot succeeded": "Изображението е създадено успешно", // components/modebar/buttons.js:66
    "Sorry, there was a problem downloading your snapshot!":
      "Възникна проблем при създаването на изображението", // components/modebar/buttons.js:69
    "Taking snapshot - this may take a few seconds":
      "Създаване на изображението - това може да отнеме известно време", // components/modebar/buttons.js:57
    "Toggle Spike Lines": "Превключване на водачи", // components/modebar/buttons.js:547
    "Toggle show closest data on hover":
      "Превключване на показване на най-близките данни при задържане на курсора", // components/modebar/buttons.js:352
    Zoom: "Режим на увеличение", // components/modebar/buttons.js:85
    "Zoom in": "Увеличаване", // components/modebar/buttons.js:121
    "Zoom out": "Отдалечаване" // components/modebar/buttons.js:130
  };
}

// @ts-expect-error ts-migrate(2339) FIXME: Property 'register' does not exist on type 'typeof... Remove this comment to see the full error message
Plotly.register([bar, pie, histogram, box, heatmap]);
// @ts-expect-error ts-migrate(2339) FIXME: Property 'register' does not exist on type 'typeof... Remove this comment to see the full error message
Plotly.register(bg);
// @ts-expect-error ts-migrate(2339) FIXME: Property 'setPlotConfig' does not exist on type 't... Remove this comment to see the full error message
Plotly.setPlotConfig({
  modeBarButtonsToRemove: ["sendDataToCloud"],

});

export {
  Plotly,
  prepareData,
  prepareLayout,
  updateData,
  updateAxes,
  updateChartSize,
  prepareCustomChartData,
  createCustomChartRenderer,
};
