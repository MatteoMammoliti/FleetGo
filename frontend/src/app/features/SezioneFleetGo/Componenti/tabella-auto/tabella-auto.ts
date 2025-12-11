import {Component, inject, Input,Output, EventEmitter} from '@angular/core';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-tabella-auto',
  imports: [
    FormsModule
  ],
  templateUrl: './tabella-auto.html',
  styleUrl: './tabella-auto.css',
})
export class TabellaAuto {
  private route = inject(Router);
  @Input() listaVeicoli: VeicoloDTO[] = [];
  @Input() listaAziende: AziendaDTO[] = [];
  @Output() richiestaEliminazione = new EventEmitter<string>();

  filtroTarga = '';
  filtroModello = '';
  filtroStato = '';
  filtroAzienda = '';

  get veicoliFiltrati(): VeicoloDTO[] {
    return this.listaVeicoli.filter(veicolo => {
      const targa = !this.filtroTarga || (veicolo.targaVeicolo && veicolo.targaVeicolo.toLowerCase().includes(this.filtroTarga.toLowerCase()));
      const modello = !this.filtroModello || (veicolo.modello && veicolo.modello.toLowerCase().includes(this.filtroModello.toLowerCase()));
      const stato = !this.filtroStato || (veicolo.statusCondizioneVeicolo && veicolo.statusCondizioneVeicolo.toLowerCase().includes(this.filtroStato.toLowerCase()));
      const azienda = !this.filtroAzienda || (veicolo.nomeAziendaAffiliata && veicolo.nomeAziendaAffiliata.toLowerCase().includes(this.filtroAzienda.toLowerCase()));
      return targa && modello && stato && azienda;
    });
  }

  eliminaVeicolo(targaVeicolo: string) {
    this.richiestaEliminazione.emit(targaVeicolo);
  }

  dettagliVeicolo(targaVeicolo: string) {
  this.route.navigate(["/dashboardFleetGo", "dettagli-veicolo", targaVeicolo]);
  }
}
