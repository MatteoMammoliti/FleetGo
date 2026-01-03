import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import { CommonModule } from '@angular/common';
import { IconaStato } from '@shared/Componenti/Ui/icona-stato/icona-stato';

@Component({
  selector: 'app-prenotazione-card',
  imports: [
    CommonModule,
    IconaStato
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
  getColoreStato(): string {
    if (this.prenotazione.richiestaAnnullata) return 'rosso';
    
    const stato = this.prenotazione.statoRichiesta || '';
    
    switch (stato) {
      case 'In attesa':
      case 'Da ritirare':
        return 'giallo';
      
      case 'In corso':
      case 'Terminata':
      case 'Approvata':
        return 'verde';
      
      case 'Rifiutata':
        return 'rosso';
        
      default:
        return 'giallo'; 
    }
  }

  getTestoStato(): string {
    if (this.prenotazione.richiestaAnnullata) return 'Richiesta Annullata';
    return this.prenotazione.statoRichiesta || 'Stato sconosciuto';
  }
}