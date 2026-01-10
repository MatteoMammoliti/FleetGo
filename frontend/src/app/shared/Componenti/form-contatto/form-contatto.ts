import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormsModule, Validators } from '@angular/forms';
import { UtenteDTO } from '@core/models/utenteDTO.model';
import { InputChecked } from '../Ui/input-checked/input-checked';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-form-contatto',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgClass,
    InputChecked
  ],
  templateUrl: './form-contatto.html',
  styleUrl: './form-contatto.css',
})

export class FormContatto implements OnInit {

  constructor(private fb: FormBuilder) {}

  @Input() titolo: string = "";
  @Input() sottotitolo: string = "";
  @Input() modalita: string = "PUBLIC";
  
  @Input() invioInCorso = false;
  @Input() messaggioInviato: boolean = false;
  @Input() erroreInvio: boolean = false;

  @Output() inviaForm = new EventEmitter<FormData>();
  @Output() resetStato = new EventEmitter<void>(); 

  form!: FormGroup;

  ngOnInit() {
    this.form = this.fb.group({
      nomeMittente: ['', [Validators.required]],
      cognomeMittente: ['', [Validators.required]],
      emailMittente: ['', [Validators.required, Validators.email]],
      oggettoMessaggio: ['', [Validators.required]],
      corpoMessaggio: ['', [Validators.required, Validators.minLength(10)]]
    });

    if(this.modalita !== 'PUBLIC') {
       this.form.get('nomeMittente')?.clearValidators();
       this.form.get('nomeMittente')?.updateValueAndValidity();
       this.form.get('cognomeMittente')?.clearValidators();
       this.form.get('cognomeMittente')?.updateValueAndValidity();
       this.form.get('emailMittente')?.clearValidators();
       this.form.get('emailMittente')?.updateValueAndValidity();
    }
  }

  isCampoInvalido(campo: string): boolean {
    const control = this.form.get(campo);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  getMessaggioErrore(campo: string): string {
    const control = this.form.get(campo);
    if (control?.hasError('required')) {
      return 'Campo obbligatorio';
    }
    if (control?.hasError('email')) {
      return 'Email non valida';
    }
    if (control?.hasError('minlength')) {
      return `Inserire almeno ${control.errors?.['minlength'].requiredLength} caratteri`;
    }
    return '';
  }

  protected onSubmit() {
    if (this.form.valid) {
      const valoriForm = this.form.value;
      const formData = new FormData();

      formData.append("messaggio", valoriForm.corpoMessaggio);
      formData.append("oggetto", valoriForm.oggettoMessaggio);
      
      if(valoriForm.nomeMittente) formData.append("NomeCognome", valoriForm.nomeMittente + " " + valoriForm.cognomeMittente);
      if(valoriForm.emailMittente) formData.append("emailMittente", valoriForm.emailMittente);

      this.inviaForm.emit(formData);
    } else {
      this.form.markAllAsTouched();
    }
  }
  
  protected resetForm() {
      this.messaggioInviato = false;
      this.erroreInvio = false;
      this.form.reset();
      this.resetStato.emit();
  }
}
