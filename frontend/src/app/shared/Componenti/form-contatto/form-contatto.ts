import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormsModule, Validators } from '@angular/forms';
import { UtenteDTO } from '@core/models/utenteDTO.model';

@Component({
  selector: 'app-form-contatto',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './form-contatto.html',
  styleUrl: './form-contatto.css',
})

export class FormContatto implements OnInit {

  constructor(private fb: FormBuilder) {}

  @Input() titolo: string = "";
  @Input() sottotitolo: string = "";
  @Input() modalita: string = "PUBLIC";

  @Output() inviaForm = new EventEmitter<FormData>();
  @Input() invioInCorso = false;

  form!: FormGroup;
  protected messaggioInviato: boolean = false;
  protected erroreInvio: boolean = false;

  ngOnInit() {
    this.form = this.fb.group({
      nomeMittente: ['', [Validators.required]],
      cognomeMittente: ['', [Validators.required]],
      emailMittente: ['', [Validators.required, Validators.email]],
      oggettoMessaggio: ['', [Validators.required]],
      corpoMessaggio: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  protected onSubmit() {
    if (this.form.valid) {

      const valoriForm = this.form.value;

      const formData = new FormData();

      formData.append("messaggio", valoriForm.corpoMessaggio);
      formData.append("oggetto", valoriForm.oggettoMessaggio);
      formData.append("NomeCognome", valoriForm.nomeMittente + " " + valoriForm.cognomeMittente);
      formData.append("emailMittente", valoriForm.emailMittente);

      this.inviaForm.emit(formData);
    } else {
      this.form.markAllAsTouched();
    }
  }
}
