import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ContenitoreFormNuovaRichiestaNoleggio} from '@core/models/ContenitoreFormNuovaRichiestaNoleggio';
import {FormsModule} from '@angular/forms';
import {CurrencyPipe} from '@angular/common';

@Component({
  selector: 'app-richiesta-noleggio-form',
  imports: [
    FormsModule,
    CurrencyPipe
  ],
  templateUrl: './richiesta-noleggio-form.html',
  styleUrl: './richiesta-noleggio-form.css',
})
export class RichiestaNoleggioForm {
  @Input() dati:ContenitoreFormNuovaRichiestaNoleggio={} as ContenitoreFormNuovaRichiestaNoleggio;
  @Input() costoStimato:number=0;
  @Output() chiudi=new EventEmitter()
  @Output() confermaRegistrazione=new EventEmitter<ContenitoreFormNuovaRichiestaNoleggio>()
  motivazione: string =""

  onClose() {
    this.chiudi.emit();
  }

  onConfirm() {
    this.dati.motivazione=this.motivazione
    this.confermaRegistrazione.emit(this.dati)
  }
}
