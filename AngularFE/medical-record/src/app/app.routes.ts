import { Routes } from '@angular/router';
import { ConsultationFormComponent } from './consultation-form.component';

export const routes: Routes = [
  { path: 'consultations/new', component: ConsultationFormComponent },
  { path: '', redirectTo: 'consultations/new', pathMatch: 'full' }
];
