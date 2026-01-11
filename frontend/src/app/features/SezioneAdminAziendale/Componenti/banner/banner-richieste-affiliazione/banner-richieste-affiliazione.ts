import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaAffiliazioneAziendaDTO} from '@core/models/RichiestaAffiliazioneAziendaDTO';

@Component({
  selector: 'app-banner-richieste-affiliazione',
  imports: [],
  templateUrl: './banner-richieste-affiliazione.html',
  styleUrl: './banner-richieste-affiliazione.css',
})
export class BannerRichiesteAffiliazione {

  @Input() richiesteAffiliazione: RichiestaAffiliazioneAziendaDTO = {} as RichiestaAffiliazioneAziendaDTO;
  @Input() numRichiesteInAttesa = 0;
  @Output() accettaAffiliazione = new EventEmitter<number>();
  @Output() rifiutaAffiliazione = new EventEmitter<number>();
  @Output() apriModaleRichieste = new EventEmitter<void>();

}
