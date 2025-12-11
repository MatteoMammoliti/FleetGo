import { Component } from '@angular/core';
import {CardStatisticheDashboardFleet} from '@shared/Componenti/Ui/card-statistiche-dashboard-fleet/card-statistiche-dashboard-fleet';
import {RouterLink} from '@angular/router';
import {DashboardFleetGoService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/dashboardFleetGo-service';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';

@Component({
  selector: 'app-dashboard-fleet-go',
  imports: [CardStatisticheDashboardFleet],
  templateUrl: './dashboard-fleet-go.html',
  styleUrl: './dashboard-fleet-go.css',
})
export class DashboardFleetGo {
  constructor(private dashboardService:DashboardFleetGoService) {}

  statistiche: ContenitoreStatisticheNumeriche = {
    totaleVeicoli: 0,
    veicoliAssegnati: 0,
    veicoliManutenzione: 0,
    totaleAziende: 0,
    veicoliDisponibili: 0,
    veicoliNoleggiati: 0
  };

  ngOnInit(): void {
    this.richiediStatistiche();
  }

  richiediStatistiche() {
    this.dashboardService.richiediStatistiche().subscribe({
      next: (contenitore) => {
        console.log(contenitore);
        this.statistiche = contenitore;
      },
      error: (err) => console.error("Errore richiesta statistiche:", err)
    });
  }
}
