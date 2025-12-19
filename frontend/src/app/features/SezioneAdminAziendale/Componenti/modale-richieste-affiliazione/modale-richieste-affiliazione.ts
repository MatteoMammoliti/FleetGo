import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaAffiliazioneAziendaDTO} from '@core/models/RichiestaAffiliazioneAziendaDTO.models';

@Component({
  selector: 'app-modale-richieste-affiliazione',
  imports: [],
  templateUrl: './modale-richieste-affiliazione.html',
  styleUrl: './modale-richieste-affiliazione.css',
})
export class ModaleRichiesteAffiliazione {

  @Input() modaleVisibile = false;
  @Input() richiesteAffiliazione: RichiestaAffiliazioneAziendaDTO[] = [];
  @Output() chiudiModale = new EventEmitter<void>();
  @Output() accettaAffiliazione = new EventEmitter<number>();
  @Output() rifiutaAffiliazione = new EventEmitter<number>();
}
