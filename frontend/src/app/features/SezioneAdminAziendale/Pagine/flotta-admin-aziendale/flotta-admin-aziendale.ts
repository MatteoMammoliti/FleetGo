import { Component } from '@angular/core';
import {TabellaAutoAdminAzienda} from '@features/SezioneAdminAziendale/Componenti/tabella-auto-admin-azienda/tabella-auto-admin-azienda';
import {FlottaAdminAziendaleService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/flotta-aziendale-service';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';

@Component({
  selector: 'app-flotta-admin-aziendale',
  imports: [
    TabellaAutoAdminAzienda
  ],
  templateUrl: './flotta-admin-aziendale.html',
  styleUrl: './flotta-admin-aziendale.css',
})
export class FlottaAdminAziendale {


  constructor(private service:FlottaAdminAziendaleService) {}

  listaVeicoli:VeicoloDTO[] = [];

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



}
