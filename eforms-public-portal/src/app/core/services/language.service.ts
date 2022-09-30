import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Language } from '../types/language';
import { LanguageResponse } from '../types/language-response';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  private cache$: Observable<LanguageResponse[]>

  constructor(
    private http: HttpClient
  ) { }

  get languages() {
    if (!this.cache$) {
      this.cache$ = this.getLanguages().pipe(
        shareReplay(1)
      );
    }

    return this.cache$;
  }

  private getLanguages(): Observable<LanguageResponse[]> {
    return this.http.get<LanguageResponse[]>(`api/public/projects/${environment.formioBaseProject}/languages`);
  }
}
