import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(
    private http: HttpClient,
    ) { }

  claimTask(id, userId) {
    return this.http.post(`api/admin/tasks/${id}/claim`, { userId });
  }

  unclaimTask(id: any) {
    return this.http.post(`api/admin/tasks/${id}/unclaim`, {});
  }
}
