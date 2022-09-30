import { Component, Input, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { TableColumn } from 'src/app/core/types/table-column';

@Component({
  selector: 'app-static-table',
  templateUrl: './static-table.component.html',
  styleUrls: ['./static-table.component.scss']
})
export class StaticTableComponent implements OnInit {
  @Input() isSortable = false;
  @Input() tableColumns: TableColumn[];
  @Input() data = [];
  @Input() initialSort = ""
  @Input() refreshData
  refreshDataSubscription
  displayedColumns
  public tableDataSource = new MatTableDataSource([]);

  columnsCount = 0;
  constructor() { }
  ngOnInit(): void {
    this.displayedColumns = this.tableColumns.map(c => { return c.name })
    this.columnsCount = 100/(this.tableColumns.length)
    this.tableDataSource = new MatTableDataSource<any>(JSON.parse(JSON.stringify(this.data)))
    if(this.refreshData) {
      this.refreshDataSubscription = this.refreshData.subscribe(() => {
        this.tableDataSource.data = JSON.parse(JSON.stringify(this.data))
    });
    }
  }

}
