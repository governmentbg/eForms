import React from "react";
import PropTypes from "prop-types";
import Tooltip from "@/components/Tooltip";
import PlainButton from "@/components/PlainButton";
import { localizeTime, durationHumanize } from "@/lib/utils";
import { RefreshScheduleType, RefreshScheduleDefault } from "../proptypes";

import "./ScheduleDialog.css";
import { t } from "@/locales/config.jsx";

export default class SchedulePhrase extends React.Component {
  static propTypes = {
    schedule: RefreshScheduleType,
    isNew: PropTypes.bool.isRequired,
    isLink: PropTypes.bool,
    onClick: PropTypes.func,
  };

  static defaultProps = {
    schedule: RefreshScheduleDefault,
    isLink: false,
    onClick: () => {},
  };

  get content() {
    const { interval: seconds } = this.props.schedule || SchedulePhrase.defaultProps.schedule;
    if (!seconds) {
      return ["Never"];
    }
    const humanized = durationHumanize(seconds, {
      omitSingleValueNumber: true,
    });
    const short = `${t("Every")} ${t(humanized)}`;
    let full = `${t("Refreshes every")} ${t(humanized)}`;

    const { time, day_of_week: dayOfWeek } = this.props.schedule;
    if (time) {
      full += ` ${t("at")} ${localizeTime(time)}`;
    }
    if (dayOfWeek) {
      full += ` ${t("on")} ${t(dayOfWeek)}`;
    }

    return [short, full];
  }

  render() {
    if (this.props.isNew) {
      return "Never";
    }

    const [short, full] = this.content;
    const content = full ? <Tooltip title={full}>{short}</Tooltip> : short;

    return this.props.isLink ? (
      <PlainButton type="link" className="schedule-phrase" onClick={this.props.onClick} data-test="EditSchedule">
        {t(content)}
      </PlainButton>
    ) : (
      t(content)
    );
  }
}
