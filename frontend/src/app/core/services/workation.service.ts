import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { SortColumn, SortDirection, Workation } from '../models/workation.model';

@Injectable({
  providedIn: 'root',
})
export class WorkationService {
  private readonly apiUrl = `${environment.apiBaseUrl}/workflex/workation`;

  constructor(private readonly http: HttpClient) {}

  getWorkations(sort?: SortColumn, direction: SortDirection = 'asc'): Observable<Workation[]> {
    let params = new HttpParams().set('direction', direction);

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Workation[]>(this.apiUrl, { params });
  }
}
