import {Component, Input} from '@angular/core';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe} from '@angular/common';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import {MessaggioCardVuota} from '@shared/Componenti/Banner/messaggio-card-vuota/messaggio-card-vuota';
import {TableSortService} from '@core/services/table-sort-service';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';

@Component({
  selector: 'app-tabella-storico-manutenzioni',
  imports: [
    DatePipe,
    MessaggioCardVuota,
    ReactiveFormsModule,
  ],
  templateUrl: './tabella-storico-manutenzioni.html',
  styleUrl: './tabella-storico-manutenzioni.css',
  animations: [ANIMAZIONE_TABELLA]
})
export class TabellaStoricoManutenzioni {
  constructor(private sortTable: TableSortService) {
  }

  @Input() listaStorico: RichiestaManutenzioneDTO[] | null = null;
  filtroTarga = new FormControl('', {nonNullable: true});

  get listaStoricoFiltrata(): RichiestaManutenzioneDTO[] {
    if (!this.listaStorico) {
      return [];
    }
    const testoRicerca = this.filtroTarga.value.trim().toUpperCase();
    if (!testoRicerca) {
      return this.listaStorico;
    }
    return this.listaStorico.filter(richiesta => {
      const targa = richiesta.veicolo?.targaVeicolo?.toUpperCase() || '';
      return targa.includes(testoRicerca);
    })
  }

  sortColonna(chiave: string) {
    if (this.listaStorico) {
      this.listaStorico = this.sortTable.sortArray([...this.listaStorico], chiave);
    }
  }

}
