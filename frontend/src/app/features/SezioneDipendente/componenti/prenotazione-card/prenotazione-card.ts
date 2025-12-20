import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-prenotazione-card',
  imports: [
    NgClass
  ],
  templateUrl: './prenotazione-card.html',
  styleUrl: './prenotazione-card.css',
})
export class PrenotazioneCard {
  @Input() prenotazione:RichiestaNoleggioDTO ={} as RichiestaNoleggioDTO;
  @Output() annullaRichiesta=new EventEmitter<number>()

  onAnnulla() {
    const conferma = confirm("Sei sicuro di voler eliminare questa richiesta?");
    if(conferma){
      this.annullaRichiesta.emit(this.prenotazione.idRichiesta);

    }
  }
}
