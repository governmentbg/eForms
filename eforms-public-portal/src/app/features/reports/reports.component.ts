import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatTab } from '@angular/material/tabs';
import { Router } from '@angular/router';
import { RedashService } from 'src/app/core/services/redash.service';
import { TableColumn } from 'src/app/core/types/table-column';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {
  tableColumns: TableColumn[];
  displayedColumns: string[];
  dashboardsDataSource: MatTableDataSource<any[]>;
  apiCallUrl = 'redash/dashboards';
  pageSize: number;
  totalItems: number;
  pageIndex = 0;
  dashboardTitle: string = '';

  constructor(
    private redashService: RedashService,
    private router: Router
  ) {
    this.displayedColumns = [ 'name', 'created_by', 'created_at' ];
    this.dashboardsDataSource = new MatTableDataSource([]);
  }

  ngOnInit(): void {
    this.getDashboards();
  }

  getDashboards(event?: PageEvent) {
    if (event) {
      this.pageIndex = event.pageIndex;
    }

    this.redashService.getDashboards(this.pageIndex + 1).subscribe((response) => {
      response['results'] = response['results'].sort((a,b) => { return a.name.toLowerCase() >= b.name.toLowerCase() ? 1 : -1 })
      this.dashboardsDataSource = new MatTableDataSource(response['results']);
      this.totalItems = response['count'];
      this.pageSize = response['page_size'];
    });
  }

  back() {
    this.router.navigate(['admin-panel/report-panel']);
  }
}
