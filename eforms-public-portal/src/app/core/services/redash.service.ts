import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RedashService {

  constructor(
    private http: HttpClient
  ) { }

  getDashboards(pageIndex: number) {
    return this.http.get(`/reports/dashboards?page=${pageIndex}`);
  }

  getDashboard(dashboardId: number) {
    return this.http.get(`/reports/dashboards/${dashboardId}`);
  }
}
