import React from "react";
import Popover from "antd/lib/popover";
import QuestionCircleFilledIcon from "@ant-design/icons/QuestionCircleFilled";
import { visualizationsSettings } from "@/visualizations/visualizationsSettings";

import "./context-help.less";
import { t } from "@/locales";

type OwnContextHelpProps = {
  icon?: React.ReactNode;
  children?: React.ReactNode;
};

type ContextHelpProps = OwnContextHelpProps & typeof ContextHelp.defaultProps;

export default function ContextHelp({ icon, children, ...props }: ContextHelpProps) {
  return (
    <Popover {...props} content={children}>
      {icon || ContextHelp.defaultIcon}
    </Popover>
  );
}

ContextHelp.defaultProps = {
  icon: null,
  children: null,
};

ContextHelp.defaultIcon = <QuestionCircleFilledIcon className="context-help-default-icon" />;

function NumberFormatSpecs() {
  const { HelpTriggerComponent } = visualizationsSettings;
  return (
    <HelpTriggerComponent
      // @ts-expect-error ts-migrate(2322) FIXME: Type '{ children: Element; type: string; title: st... Remove this comment to see the full error message
      type="NUMBER_FORMAT_SPECS"
      title="Formatting Numbers"
      href="https://redash.io/help/user-guide/visualizations/formatting-numbers"
      className="visualization-editor-context-help">
      {ContextHelp.defaultIcon}
    </HelpTriggerComponent>
  );
}

function PercentFormatSpecs() {
  const { HelpTriggerComponent } = visualizationsSettings;
  return (
    <HelpTriggerComponent
      // @ts-expect-error ts-migrate(2322) FIXME: Type '{ children: Element; type: string; title: st... Remove this comment to see the full error message
      type="PERCENT_FORMAT_SPECS"
      title="Formatting Percentages"
      href="https://redash.io/help/user-guide/visualizations/formatting-numbers"
      className="visualization-editor-context-help">
      {ContextHelp.defaultIcon}
    </HelpTriggerComponent>
  );
}

function DateTimeFormatSpecs() {
  const { HelpTriggerComponent } = visualizationsSettings;
  return (
    <HelpTriggerComponent
      title={t("Formatting Dates and Times")}
      href="https://momentjs.com/docs/#/displaying/format/"
      className="visualization-editor-context-help">
      {ContextHelp.defaultIcon}
    </HelpTriggerComponent>
  );
}

ContextHelp.NumberFormatSpecs = NumberFormatSpecs;
ContextHelp.PercentFormatSpecs = PercentFormatSpecs;
ContextHelp.DateTimeFormatSpecs = DateTimeFormatSpecs;
