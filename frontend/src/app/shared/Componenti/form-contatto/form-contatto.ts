import {Component, inject, Input, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, FormsModule, Validators} from '@angular/forms';

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

  private fb = inject(FormBuilder);

  @Input() titolo: string = "";
  @Input() sottotitolo: string = "";
  @Input() modalita: string = "PUBLIC";

  form! : FormGroup;
  protected messaggioInviato: boolean = false;
  protected erroreInvio: boolean = false;
  protected invioInCorso: boolean = false;

  ngOnInit() {
    this.form = this.fb.group({
      nomeMittente : ['',[Validators.required]],
      emailMittente : ['',[Validators.required, Validators.email]],
      oggettoMessaggio : ['',[Validators.required]],
      corpoMessaggio : ['',[Validators.required, Validators.minLength(10)]]
    });
  }

  protected onSubmit() {
    if (this.form.valid) {
      this.invioInCorso = true;
    } else {
      this.form.markAllAsTouched();
    }
  }




}
