import {Component, inject} from '@angular/core';
import {FormBackground} from '@shared/form-background/form-background';
import {GraficoStatoFlotta} from '@shared/grafico-stato-flotta/grafico-stato-flotta';
import {GraficoAndamentoUtilizzo} from '@shared/grafico-andamento-utilizzo/grafico-andamento-utilizzo';
import {DashboardService} from '@core/services/ServiceSezioneAdminAziendale/dashboard-service';
import {ContenitoreStatisticheNumeriche} from '@models/ContenitoreStatisticheNumeriche';

@Component({
  selector: 'app-dashboard-azienda',
  imports: [
    FormBackground,
    GraficoStatoFlotta,
    GraficoAndamentoUtilizzo
  ],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})
export class DashboardAzienda {

  constructor(private service:DashboardService) {}

  ngOnInit(){
    this.settaDatiGraficoStatoFlotta();
  }

  contenitoreInputGraficoStatoFlotta: ContenitoreStatisticheNumeriche = {
      veicoliAssegnati: 0,
      totaleVeicoli: 0,
      totaleAziende: 0,
      veicoliNoleggati: 0,
      veicoliManutenzione:0,
      veicoliDisponibili:0
    }

  settaDatiGraficoStatoFlotta() {
    this.service.getStatoVeicoli().subscribe({
      next: (contenitore) => {
       this.contenitoreInputGraficoStatoFlotta=contenitore;
       console.log("sono uiiiii")

      },
      error: (err) => console.error("Errore grafico:", err)
    });
  }
}
