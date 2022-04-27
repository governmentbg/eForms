import { filter, includes, map } from "lodash";
import React, { useMemo } from "react";
import { Select } from "@/components/visualizations/editor";
import { visualizationsSettings } from "@/visualizations/visualizationsSettings";
import { t } from "@/locales";

const allChartTypes = [
  { type: "line", name: t("Line"), icon: "line-chart" },
  { type: "column", name: t("Bar"), icon: "bar-chart" },
  { type: "area", name: t("Area"), icon: "area-chart" },
  { type: "pie", name: t("Pie"), icon: "pie-chart" },
  { type: "scatter", name: t("Scatter"), icon: "circle-o" },
  { type: "bubble", name: t("Bubble"), icon: "circle-o" },
  { type: "heatmap", name: t("Heatmap"), icon: "th" },
  { type: "box", name: t("Box"), icon: "square-o" },
];

type OwnProps = {
  hiddenChartTypes?: any[]; // TODO: PropTypes.oneOf(map(allChartTypes, "type"))
};

type Props = OwnProps & typeof ChartTypeSelect.defaultProps;

export default function ChartTypeSelect({ hiddenChartTypes, ...props }: Props) {
  const chartTypes = useMemo(() => {
    const result = [...allChartTypes];

    if (visualizationsSettings.allowCustomJSVisualizations) {
      result.push({ type: "custom", name: "Custom", icon: "code" });
    }

    if (hiddenChartTypes.length > 0) {
      return filter(result, ({ type }) => !includes(hiddenChartTypes, type));
    }

    return result;
  }, []);

  return (
    <Select {...props}>
      {map(chartTypes, ({ type, name, icon }) => (
        // @ts-expect-error ts-migrate(2339) FIXME: Property 'Option' does not exist on type '({ class... Remove this comment to see the full error message
        <Select.Option key={type} value={type} data-test={`Chart.ChartType.${type}`}>
          <i className={`fa fa-${icon}`} style={{ marginRight: 5 }} />
          {name}
          {/* @ts-expect-error ts-migrate(2339) FIXME: Property 'Option' does not exist on type '({ class... Remove this comment to see the full error message */}
        </Select.Option>
      ))}
    </Select>
  );
}

ChartTypeSelect.defaultProps = {
  hiddenChartTypes: [],
};
