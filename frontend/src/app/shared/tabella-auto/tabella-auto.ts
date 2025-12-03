import {Component, inject} from '@angular/core';
import {VeicoloDTO} from '@models/veicoloDTO.model';
import {FlottaGlobaleService} from '@core/services/adminFleetGoService/flotta-globale-service';
import {TabellaBackground} from "@shared/tabella-background/tabella-background";

@Component({
  selector: 'app-tabella-auto',
  imports: [
    TabellaBackground
  ],
  templateUrl: './tabella-auto.html',
  styleUrl: './tabella-auto.css',
})
export class TabellaAuto {
  private service=inject(FlottaGlobaleService);
  constructor() {}
    listaVeicoli:VeicoloDTO[]=[];
  ngOnInit(): void {
    this.aggiornaDati();
  }
  aggiornaDati(){
    this.service.richiediVeicoli().subscribe({
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
