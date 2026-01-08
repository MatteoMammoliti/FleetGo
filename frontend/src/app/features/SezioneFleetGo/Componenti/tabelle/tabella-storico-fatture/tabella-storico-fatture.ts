import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FatturaDTO} from '@core/models/FatturaDTO.models';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import {SceltaTendina} from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';
import {BottoneChiaro} from '@shared/Componenti/Ui/bottone-chiaro/bottone-chiaro';
import {TableSortService} from '@core/services/table-sort-service';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';
@Component({
  selector: 'app-tabella-storico-fatture',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    MessaggioCardVuota,
    SceltaTendina,
    BottoneChiaro
  ],
  templateUrl: './tabella-storico-fatture.html',
  styleUrl: './tabella-storico-fatture.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class TabellaStoricoFatture {
  constructor(private sortTable: TableSortService) {}
  @Input() fatture: FatturaDTO[] |null = null;
  @Output() richiestaDownload: EventEmitter<number> = new EventEmitter<number>();
  protected annoSelezionato: number = new Date().getFullYear();
  protected aziendaSelezionata: any = null;
  @Output() aggiornaTabella=new EventEmitter();
  @Input()  anniFatture: number[] = [];
  @Input() listaAziende: AziendaDTO[] = [];

  nomiMesi = [
    'Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno',
    'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre'
  ];


  sortColonna(chiave:string){
    if(!this.fatture){
      return;
    }
    this.fatture=this.sortTable.sortArray(this.fatture, chiave);
  }

  protected onDownload(numeroFattura: any) {
    this.richiestaDownload.emit(numeroFattura);
  }



  protected aggiornaFatture() {
    const dati={
      annoSelezionato:this.annoSelezionato,
      aziendaSelezionata:this.aziendaSelezionata
    }
    this.aggiornaTabella.emit(dati);
  }
}
