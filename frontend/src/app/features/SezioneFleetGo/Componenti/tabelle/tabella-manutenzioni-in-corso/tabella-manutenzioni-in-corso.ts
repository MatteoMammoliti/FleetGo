import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe} from '@angular/common';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import {BottoneChiaro} from '@shared/Componenti/Ui/bottone-chiaro/bottone-chiaro';
import {TableSortService} from '@core/services/table-sort-service';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';

@Component({
  selector: 'app-tabella-manutenzioni-in-corso',
  imports: [
    DatePipe,
    TemplateTitoloSottotitolo,
    MessaggioCardVuota,
    BottoneChiaro
  ],
  templateUrl: './tabella-manutenzioni-in-corso.html',
  styleUrl: './tabella-manutenzioni-in-corso.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class TabellaManutenzioniInCorso {

  @Input() listaRichieste:RichiestaManutenzioneDTO[]|null=null
  @Output() chiudiRichiesta=new EventEmitter<number>()

  onConcludi(idRichiesta:number){
    this.chiudiRichiesta.emit(idRichiesta)
  }

}
