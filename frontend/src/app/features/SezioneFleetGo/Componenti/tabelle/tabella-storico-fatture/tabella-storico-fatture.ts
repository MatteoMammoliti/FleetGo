import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FatturaDTO} from '@core/models/FatturaDTO.models';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import {SceltaTendina} from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';
import {BottoneChiaro} from '@shared/Componenti/Ui/bottone-chiaro/bottone-chiaro';
import {TableSortService} from '@core/services/table-sort-service';

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
})

export class TabellaStoricoFatture {
  constructor(private sortTable: TableSortService) {}
  @Input() fatture: FatturaDTO[] = [];
  @Output() richiestaDownload: EventEmitter<number> = new EventEmitter<number>();
  protected annoSelezionato: number = new Date().getFullYear();
  protected aziendaSelezionata: any = null;
  @Output() aggiornaTabella=new EventEmitter();
  @Input()  anniFatture: number[] = [];
  @Input() listaAziende: AziendaDTO[] = [];



  sortColonna(chiave:string){
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
