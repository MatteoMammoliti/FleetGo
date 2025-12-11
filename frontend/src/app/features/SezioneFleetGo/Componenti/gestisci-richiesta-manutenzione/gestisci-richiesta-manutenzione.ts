import { Component,Input } from '@angular/core';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe, NgClass} from '@angular/common';

@Component({
  selector: 'app-gestisci-richiesta-manutenzione',
  imports: [
    DatePipe,
    NgClass
  ],
  standalone:true,
  templateUrl: './gestisci-richiesta-manutenzione.html',
  styleUrl: './gestisci-richiesta-manutenzione.css',
})
export class GestisciRichiestaManutenzione {
  @Input({required:true}) datiRichiesta!:RichiestaManutenzioneDTO;

  clickRifiuta(){}
  clickAccetta(){}
  onClose(){}
}
