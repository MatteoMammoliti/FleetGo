import {Component, EventEmitter, Input, Output} from '@angular/core';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-tabella-auto-admin-azienda',
  imports: [
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './tabella-auto-admin-azienda.html',
  styleUrl: './tabella-auto-admin-azienda.css',
})
export class TabellaAutoAdminAzienda {


  filtroTarga = "";
  filtroModello= "";
  filtroStato = "";
  filtroManutenzione="";

  @Input() listaVeicoli:VeicoloDTO[]=[];
  @Output() apriGestioneVeicolo:EventEmitter<string>=new EventEmitter()


  get veicoliFiltrati(): VeicoloDTO[] {
    return this.listaVeicoli.filter(veicolo => {

      const targa = !this.filtroTarga ||
        (veicolo.targaVeicolo && veicolo.targaVeicolo.toLowerCase().includes(this.filtroTarga.toLowerCase()));

      const modello = !this.filtroModello ||
        (veicolo.modello && veicolo.modello.toLowerCase().includes(this.filtroModello.toLowerCase()));
      const stato = !this.filtroStato ||
        (veicolo.statusContrattualeVeicolo && veicolo.statusContrattualeVeicolo.toLowerCase().includes(this.filtroStato.toLowerCase()));

      const manutenzone=!this.filtroManutenzione || veicolo.inManutenzione;

      return targa && modello && stato && manutenzone;
    });
  }

  rimuoviVeicolo(targaVeicolo: string) {

  }

}
