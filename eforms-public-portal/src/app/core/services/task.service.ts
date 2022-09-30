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

  getCurrentTask(processInstanceId){
    return this.http.get(`api/admin/tasks?processInstanceId=${processInstanceId}`);
  }
  
  getPreviousTasks(processInstanceId){
    return this.http.get(`api/admin/history-tasks?processInstanceId=${processInstanceId}`);
  }
}
