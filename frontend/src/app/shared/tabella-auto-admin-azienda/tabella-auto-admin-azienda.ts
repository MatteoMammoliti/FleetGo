import {Component, inject} from '@angular/core';
import {FlottaGlobaleService} from '@core/services/ServiceSezioneFleetGo/flotta-globale-service';
import {AziendeAffiliateService} from '@core/services/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AziendaDTO} from '@models/aziendaDTO';
import {VeicoloDTO} from '@models/veicoloDTO.model';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TabellaBackground} from '@shared/tabella-background/tabella-background';
import {FlottaAdminAziendaleService} from '@core/services/ServiceSezioneAdminAziendale/flotta-aziendale-service';

@Component({
  selector: 'app-tabella-auto-admin-azienda',
  imports: [
    ReactiveFormsModule,
    TabellaBackground,
    FormsModule
  ],
  templateUrl: './tabella-auto-admin-azienda.html',
  styleUrl: './tabella-auto-admin-azienda.css',
})
export class TabellaAutoAdminAzienda {
  private service= inject(FlottaAdminAziendaleService);


  filtroTarga = "";
  filtroModello= "";
  filtroStato = "";

  listaVeicoli:VeicoloDTO[]=[];

  constructor() {}

  ngOnInit(): void {
    this.aggiornaDati();

  }

  aggiornaDati(){
    this.service.richiediVeicoliAziendali().subscribe({
      next: (response) => {
        if (response) {
          this.listaVeicoli = response;
          console.log("Dati caricati:", this.listaVeicoli);
        }
        else{
          console.log("Nessun veicolo trovato", this.listaVeicoli);
        }
      },
      error: (err) => {
        console.error("Errore nel caricamento:", err);
      }
    });
  }



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
