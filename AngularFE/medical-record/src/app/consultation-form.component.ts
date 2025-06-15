import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConsultationService } from './services/consultation.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-consultation-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
  <div class="max-w-xl mx-auto mt-10 p-4 bg-white shadow rounded">
    <h2 class="text-xl font-bold mb-4">Create Consultation</h2>
    <form [formGroup]="form" (ngSubmit)="submit()" class="space-y-4">
      <div>
        <label class="block mb-1">Date</label>
        <input type="datetime-local" formControlName="date" class="border rounded px-2 py-1 w-full" />
      </div>
      <div>
        <label class="block mb-1">Doctor ID</label>
        <input type="text" formControlName="doctorId" class="border rounded px-2 py-1 w-full" />
      </div>
      <div>
        <label class="block mb-1">Patient ID</label>
        <input type="text" formControlName="patientId" class="border rounded px-2 py-1 w-full" />
      </div>
      <div>
        <label class="block mb-1">Diagnosis ID</label>
        <input type="number" formControlName="diagnosisId" class="border rounded px-2 py-1 w-full" />
      </div>
      <div>
        <label class="block mb-1">Notes</label>
        <textarea formControlName="notes" class="border rounded px-2 py-1 w-full"></textarea>
      </div>
      <button type="submit" [disabled]="form.invalid" class="bg-blue-500 text-white px-4 py-2 rounded">Save</button>
    </form>
  </div>
  `,
  styles: []
})
export class ConsultationFormComponent {
  form = this.fb.group({
    date: ['', Validators.required],
    doctorId: ['', Validators.required],
    patientId: ['', Validators.required],
    diagnosisId: ['', Validators.required],
    notes: ['']
  });

  constructor(private fb: FormBuilder, private service: ConsultationService) {}

  submit() {
    if (this.form.valid) {
      const value = this.form.value as any;
      this.service.create(value).subscribe();
    }
  }
}
