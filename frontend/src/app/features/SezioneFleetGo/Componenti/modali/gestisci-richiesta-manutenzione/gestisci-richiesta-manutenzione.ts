import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe, NgClass} from '@angular/common';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';

@Component({
  selector: 'app-gestisci-richiesta-manutenzione',
  imports: [
    DatePipe,
    TemplateFinestraModale
  ],
  standalone:true,
  templateUrl: './gestisci-richiesta-manutenzione.html',
  styleUrl: './gestisci-richiesta-manutenzione.css',
})
export class GestisciRichiestaManutenzione {
  @Input({required:true}) datiRichiesta!:RichiestaManutenzioneDTO;
  @Output() close = new EventEmitter<void>();
  @Output() accettaRichiesta= new EventEmitter<number>();
  @Output() rifiutaRichiesta=new EventEmitter<number>();

  clickRifiuta(){
    this.rifiutaRichiesta.emit(this.datiRichiesta.idManutenzione);
  }
  clickAccetta(){
    this.accettaRichiesta.emit(this.datiRichiesta.idManutenzione);
  }
  onClose() {
    this.close.emit();
  }
}
