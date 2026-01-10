import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {ContenitoreFormNuovaRichiestaNoleggio} from '@core/models/ContenitoreFormNuovaRichiestaNoleggio';
import {FormsModule} from '@angular/forms';
import {CurrencyPipe} from '@angular/common';
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';
import {InputChecked} from '@shared/Componenti/Input/input-checked/input-checked';

@Component({
  selector: 'app-richiesta-noleggio-form',
  imports: [
    FormsModule,
    CurrencyPipe,
    TemplateFinestraModale,
    InputChecked
  ],
  templateUrl: './richiesta-noleggio-form.html',
  styleUrl: './richiesta-noleggio-form.css',
})
export class RichiestaNoleggioForm {

  @ViewChild('modale') finestraModale!: TemplateFinestraModale;

  @Input() dati: ContenitoreFormNuovaRichiestaNoleggio = {} as ContenitoreFormNuovaRichiestaNoleggio;
  @Input() costoStimato: number = 0;
  @Output() chiudi = new EventEmitter()
  @Output() confermaRegistrazione = new EventEmitter<ContenitoreFormNuovaRichiestaNoleggio>()
  motivazione: string = ""
  erroreMotivazione: boolean = false;

  onClose() {
    this.chiudi.emit();
  }

  resetErrore() {
    if (this.motivazione.trim().length >= 5) {
      this.erroreMotivazione = false;
    }
  }

  onConfirm() {
    if (!this.motivazione || this.motivazione.trim().length <= 5) {
      this.erroreMotivazione = true;
      return;
    }

    this.dati.motivazione = this.motivazione;
    this.confermaRegistrazione.emit(this.dati);
    this.finestraModale.chiudiModale()

  }
}
