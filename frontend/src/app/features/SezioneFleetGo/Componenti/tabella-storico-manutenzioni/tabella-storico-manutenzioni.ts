import {Component, Input} from '@angular/core';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe} from '@angular/common';
import {FormControl,  ReactiveFormsModule} from '@angular/forms';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';

@Component({
  selector: 'app-tabella-storico-manutenzioni',
  imports: [
    DatePipe,
    MessaggioCardVuota,
    ReactiveFormsModule,
  ],
  templateUrl: './tabella-storico-manutenzioni.html',
  styleUrl: './tabella-storico-manutenzioni.css',
})
export class TabellaStoricoManutenzioni {
  @Input() listaStorico:RichiestaManutenzioneDTO[]=[]
  filtroTarga=new FormControl('', { nonNullable: true });

  get listaStoricoFiltrata():RichiestaManutenzioneDTO[]{
    const testoRicerca = this.filtroTarga.value.trim().toUpperCase();
    if (!testoRicerca) {
      return this.listaStorico;
    }
    return this.listaStorico.filter(richiesta=>{
      const targa = richiesta.veicolo?.targaVeicolo?.toUpperCase() || '';
      return targa.includes(testoRicerca);
    })
  }

}
