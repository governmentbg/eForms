import { Menu } from 'antd';
import {
  LineChartOutlined,
  AreaChartOutlined,
  BarChartOutlined,
  PieChartOutlined,
  TableOutlined,
  InfoCircleOutlined,
} from '@ant-design/icons';
import ButtonDropdown from './ButtonDropdown';
import { t } from '../locales';

const ChartTypes = [
  { name: 'line', title: t('Line'), icon: <LineChartOutlined /> },
  { name: 'area', title: t('Area'), icon: <AreaChartOutlined /> },
  { name: 'bar', title: t('Bar'), icon: <BarChartOutlined /> },
  { name: 'pie', title: t('Pie'), icon: <PieChartOutlined /> },
  { name: 'table', title: t('Table'), icon: <TableOutlined /> },
  { name: 'number', title: t('Number'), icon: <InfoCircleOutlined /> },
];

const SelectChartType = ({ chartType, updateChartType }) => {
  const menu = (
    <Menu data-testid="chart-type-dropdown">
      {ChartTypes.map((m) => (
        <Menu.Item key={m.title} onClick={() => updateChartType(m.name)}>
          {m.icon} {m.title}
        </Menu.Item>
      ))}
    </Menu>
  );

  const foundChartType = ChartTypes.find((t) => t.name === chartType);
  return (
    <ButtonDropdown
      data-testid="chart-type-btn"
      overlay={menu}
      icon={foundChartType?.icon}
      style={{ border: 0 }}
    >
      {foundChartType?.title || ''}
    </ButtonDropdown>
  );
};

export default SelectChartType;
