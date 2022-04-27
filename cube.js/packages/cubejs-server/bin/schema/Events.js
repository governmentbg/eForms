cube(`Events`, {
  sql: `SELECT * FROM test.events`,
  
  preAggregations: {
    // Pre-Aggregations definitions go here
    // Learn more here: https://cube.dev/docs/caching/pre-aggregations/getting-started  
  },
  
  joins: {
    
  },
  
  measures: {
    count: {
      type: `count`,
      drillMembers: [anonymousid, timestamp]
    }
  },
  
  dimensions: {
    anonymousid: {
      sql: `${CUBE}.\`anonymousId\``,
      type: `string`
    },
    
    eventtype: {
      sql: `${CUBE}.\`eventType\``,
      type: `string`
    },
    
    timestamp: {
      sql: `timestamp`,
      type: `time`
    }
  },
  
  dataSource: `default`
});
