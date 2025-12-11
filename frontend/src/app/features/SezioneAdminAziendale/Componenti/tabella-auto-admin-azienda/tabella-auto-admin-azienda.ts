import {Component, Input} from '@angular/core';
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

  @Input() listaVeicoli:VeicoloDTO[]=[];


  get veicoliFiltrati(): VeicoloDTO[] {
    return this.listaVeicoli.filter(veicolo => {

      const targa = !this.filtroTarga ||
        (veicolo.targaVeicolo && veicolo.targaVeicolo.toLowerCase().includes(this.filtroTarga.toLowerCase()));

      const modello = !this.filtroModello ||
        (veicolo.modello && veicolo.modello.toLowerCase().includes(this.filtroModello.toLowerCase()));

      const stato = !this.filtroStato ||
        (veicolo.statusCondizioneVeicolo && veicolo.statusCondizioneVeicolo.toLowerCase().includes(this.filtroStato.toLowerCase()));


      return targa && modello && stato;
    });
  }

  rimuoviVeicolo(targaVeicolo: string) {

  }

}
