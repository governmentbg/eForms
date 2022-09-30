import { map } from "lodash";
import React from "react";
import PropTypes from "prop-types";

import Badge from "antd/lib/badge";
import Card from "antd/lib/card";
import Spin from "antd/lib/spin";
import Table from "antd/lib/table";
import { Columns } from "@/components/items-list/components/ItemsTable";
import { t } from "@/locales/config.jsx";

// CounterCard

export function CounterCard({ title, value, loading }) {
  return (
    <Spin spinning={loading}>
      <Card>
        {title}
        <div className="f-20">{value}</div>
      </Card>
    </Spin>
  );
}

CounterCard.propTypes = {
  title: PropTypes.string.isRequired,
  value: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  loading: PropTypes.bool.isRequired,
};

CounterCard.defaultProps = {
  value: "",
};

// Tables

const queryJobsColumns = [
  { title: t("Queue"), dataIndex: "origin" },
  { title: t("Query ID"), dataIndex: ["meta", "query_id"] },
  { title: t("Org ID"), dataIndex: ["meta", "org_id"] },
  { title: t("Data Source ID"), dataIndex: ["meta", "data_source_id"] },
  { title: t("User ID"), dataIndex: ["meta", "user_id"] },
  Columns.custom(scheduled => scheduled.toString(), { title: t("Scheduled"), dataIndex: ["meta", "scheduled"] }),
  Columns.timeAgo({ title: t("Start Time"), dataIndex: "started_at" }),
  Columns.timeAgo({ title: t("Enqueue Time"), dataIndex: "enqueued_at" }),
];

const otherJobsColumns = [
  { title: t("Queue"), dataIndex: "origin" },
  { title: t("Job Name"), dataIndex: "name" },
  Columns.timeAgo({ title: t("Start Time"), dataIndex: "started_at" }),
  Columns.timeAgo({ title: t("Enqueue Time"), dataIndex: "enqueued_at" }),
];

const workersColumns = [
  Columns.custom(
    value => (
      <span>
        <Badge status={{ busy: "processing", idle: "default", started: "success", suspended: "warning" }[value]} />{" "}
        {value}
      </span>
    ),
    { title: t("State"), dataIndex: "state" }
  ),
]
  .concat(
    map(["Hostname", "PID", "Name", "Queues", "Current Job", "Successful Jobs", "Failed Jobs"], c => ({
      title: t(c),
      dataIndex: c.toLowerCase().replace(/\s/g, "_"),
    }))
  )
  .concat([
    Columns.dateTime({ title: t("Birth Date"), dataIndex: "birth_date" }),
    Columns.duration({ title: t("Total Working Time"), dataIndex: "total_working_time" }),
  ]);

const queuesColumns = map(["Name", "Started", "Queued"], c => ({ title: t(c), dataIndex: c.toLowerCase() }));

const TablePropTypes = {
  loading: PropTypes.bool.isRequired,
  items: PropTypes.arrayOf(PropTypes.object).isRequired,
};

export function WorkersTable({ loading, items }) {
  for (let index = 0; index < items.length; index++) {
    const element = items[index];
    element.state = t(element.state);

    let splitQueues = element.queues.split(', ');
    let translatedQueues = [];

    for (let index = 0; index < splitQueues.length; index++) {
      const translatedQueue = t(splitQueues[index]);
      translatedQueues.push(translatedQueue);
    }

    element.queues = translatedQueues.join(', ');
  }

  return (
    <Table
      locale={{emptyText: t('No Data')}}
      loading={loading}
      columns={workersColumns}
      rowKey="name"
      dataSource={items}
      pagination={{
        defaultPageSize: 25,
        pageSizeOptions: ["10", "25", "50"],
        showSizeChanger: true,
      }}
    />
  );
}

WorkersTable.propTypes = TablePropTypes;

export function QueuesTable({ loading, items }) {
  for (let index = 0; index < items.length; index++) {
    const element = items[index];
    element.name = t(element.name);
  }

  return (
    <Table
      locale={{ emptyText: t('No Data') }}
      loading={loading}
      columns={queuesColumns}
      rowKey="name"
      dataSource={items}
      pagination={{
        defaultPageSize: 25,
        pageSizeOptions: ["10", "25", "50"],
        showSizeChanger: true,
      }}
    />
  );
}

QueuesTable.propTypes = TablePropTypes;

export function QueryJobsTable({ loading, items }) {
  for (let index = 0; index < items.length; index++) {
    const element = items[index];
    element.id = t(element.id);
  }

  return (
    <Table
      locale={{ emptyText: t('No Data') }}
      loading={loading}
      columns={queryJobsColumns}
      rowKey="id"
      dataSource={items}
      pagination={{
        defaultPageSize: 25,
        pageSizeOptions: ["10", "25", "50"],
        showSizeChanger: true,
      }}
    />
  );
}

QueryJobsTable.propTypes = TablePropTypes;

export function OtherJobsTable({ loading, items }) {
  for (let index = 0; index < items.length; index++) {
    const element = items[index];
    element.id = t(element.id);
  }

  return (
    <Table
      locale={{ emptyText: t('No Data') }}
      loading={loading}
      columns={otherJobsColumns}
      rowKey="id"
      dataSource={items}
      pagination={{
        defaultPageSize: 25,
        pageSizeOptions: ["10", "25", "50"],
        showSizeChanger: true,
      }}
    />
  );
}

OtherJobsTable.propTypes = TablePropTypes;
