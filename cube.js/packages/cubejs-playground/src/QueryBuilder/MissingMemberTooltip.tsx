import { Tooltip } from 'antd';
import { t } from '../locales';

export default function MissingMemberTooltip({ children }) {
  return (
    <Tooltip
      overlayClassName="missing-member-tooltip"
      placement="top"
      title={t("This member was removed from the data schema")}
      color="var(--dark-01-color)"
    >
      {children}
    </Tooltip>
  );
}
