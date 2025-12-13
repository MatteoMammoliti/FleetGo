import {Component, Input} from '@angular/core';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {DatePipe} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-tabella-storico-manutenzioni',
  imports: [
    DatePipe,
    FormsModule
  ],
  templateUrl: './tabella-storico-manutenzioni.html',
  styleUrl: './tabella-storico-manutenzioni.css',
})
export class TabellaStoricoManutenzioni {
  @Input() listaStorico:RichiestaManutenzioneDTO[]=[]
  filtroTarga: any;

  get listaStoricoFiltrata():RichiestaManutenzioneDTO[]{
    return this.listaStorico.filter(richiesta=>{
      const targa = !this.filtroTarga && richiesta.veicolo.targaVeicolo.includes(this.filtroTarga)
      return targa;
    })
  }

}
