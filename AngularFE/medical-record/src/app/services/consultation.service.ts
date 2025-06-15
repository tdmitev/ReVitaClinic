import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface ConsultationCreate {
  date: string;
  doctorId: string;
  patientId: string;
  diagnosisId: number;
  notes?: string;
}

@Injectable({ providedIn: 'root' })
export class ConsultationService {
  constructor(private http: HttpClient) {}

  create(dto: ConsultationCreate): Observable<any> {
    return this.http.post(`${environment.apiUrl}/consultations`, dto);
  }
}
