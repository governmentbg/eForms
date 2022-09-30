import React from "react";
import { SettingsEditorPropTypes, SettingsEditorDefaultProps } from "../prop-types";
import Form from "antd/lib/form";
import Select from "antd/lib/select";
import Skeleton from "antd/lib/skeleton";
import DynamicComponent from "@/components/DynamicComponent";
import { clientConfig } from "@/services/auth";
import { t } from "@/locales/config.jsx";

export default function FormatSettings(props) {
  const { values, onChange, loading } = props;

  return (
    <DynamicComponent name="OrganizationSettings.FormatSettings" {...props}>
      <Form.Item label={t("Date Format")}>
        {loading ? (
          <Skeleton.Input style={{ width: 300 }} active />
        ) : (
          <Select
            value={values.date_format}
            onChange={value => onChange({ date_format: value })}
            data-test="DateFormatSelect">
            {clientConfig.dateFormatList.map(dateFormat => (
              <Select.Option key={dateFormat} data-test={`DateFormatSelect:${dateFormat}`}>
                {t(dateFormat)}
              </Select.Option>
            ))}
          </Select>
        )}
      </Form.Item>
      <Form.Item label={t("Time Format")}>
        {loading ? (
          <Skeleton.Input style={{ width: 300 }} active />
        ) : (
          <Select
            value={values.time_format}
            onChange={value => onChange({ time_format: value })}
            data-test="TimeFormatSelect">
            {clientConfig.timeFormatList.map(timeFormat => (
              <Select.Option key={timeFormat}>{t(timeFormat)}</Select.Option>
            ))}
          </Select>
        )}
      </Form.Item>
    </DynamicComponent>
  );
}

FormatSettings.propTypes = SettingsEditorPropTypes;

FormatSettings.defaultProps = SettingsEditorDefaultProps;
