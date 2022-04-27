import React, { useState } from "react";
import Card from "antd/lib/card";
import Button from "antd/lib/button";
import Typography from "antd/lib/typography";
import { clientConfig } from "@/services/auth";
import Link from "@/components/Link";
import HelpTrigger from "@/components/HelpTrigger";
import DynamicComponent from "@/components/DynamicComponent";
import OrgSettings from "@/services/organizationSettings";
import { t } from "@/locales/config.jsx";

const Text = Typography.Text;

function BeaconConsent() {
  const [hide, setHide] = useState(false);

  if (!clientConfig.showBeaconConsentMessage || hide) {
    return null;
  }

  const hideConsentCard = () => {
    clientConfig.showBeaconConsentMessage = false;
    setHide(true);
  };

  const confirmConsent = confirm => {
    let message = t("🙏 Thank you.");

    if (!confirm) {
      message = t("Settings Saved.");
    }

    OrgSettings.save({ beacon_consent: confirm }, message)
      // .then(() => {
      //   // const settings = get(response, 'settings');
      //   // this.setState({ settings, formValues: { ...settings } });
      // })
      .finally(hideConsentCard);
  };

  return (
    <DynamicComponent name="BeaconConsent">
      <div className="m-t-10 tiled">
        <Card
          title={
            <>
              {t("Would you be ok with sharing anonymous usage data with the Redash team?")}{" "}
              <HelpTrigger type="USAGE_DATA_SHARING" />
            </>
          }
          bordered={false}>
          <Text>{t("Help Redash improve by automatically sending anonymous usage data:")}</Text>
          <div className="m-t-5">
            <ul>
              <li> {t("Number of users, queries, dashboards, alerts, widgets and visualizations.")}</li>
              <li> {t("Types of data sources, alert destinations and visualizations.")}</li>
            </ul>
          </div>
          <Text>{t("All data is aggregated and will never include any sensitive or private data.")}</Text>
          <div className="m-t-5">
            <Button type="primary" className="m-r-5" onClick={() => confirmConsent(true)}>
              {t("Yes")}
            </Button>
            <Button type="default" onClick={() => confirmConsent(false)}>
              {t("No")}
            </Button>
          </div>
          <div className="m-t-15">
            <Text type="secondary">
              {t("You can change this setting anytime from the")}{" "}
              <Link href="settings/organization">{t("Organization Settings")}</Link> {t("page.")}
            </Text>
          </div>
        </Card>
      </div>
    </DynamicComponent>
  );
}

export default BeaconConsent;
