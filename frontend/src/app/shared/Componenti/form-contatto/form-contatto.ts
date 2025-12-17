import {Component, Input} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-form-contatto',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './form-contatto.html',
  styleUrl: './form-contatto.css',
})
export class FormContatto {

  @Input() titolo: string = "";
  @Input() sottotitolo: string = "";
  @Input() modalita: string = "";
  protected form: any;

  protected messaggioInviato: any;
  protected erroreInvio: any;
  protected invioInCorso = false;

  protected onSubmit() {

  }
}
