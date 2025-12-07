import { Component } from '@angular/core';
import {CardStatisticheDashboardFleet} from '@shared/card-statistiche-dashboard-fleet/card-statistiche-dashboard-fleet';
import {RouterLink} from '@angular/router';
import {DashboardFleetGoService} from '@core/services/ServiceSezioneFleetGo/dashboardFleetGo-service';
import {ContenitoreStatisticheNumericheFleetGo} from '@models/ContenitoreStatisticheNumericheFleetGo';

@Component({
  selector: 'app-dashboard-fleet-go',
  imports: [CardStatisticheDashboardFleet],
  templateUrl: './dashboard-fleet-go.html',
  styleUrl: './dashboard-fleet-go.css',
})
export class DashboardFleetGo {
  constructor(private dashboardService:DashboardFleetGoService) {}


  statistiche: ContenitoreStatisticheNumericheFleetGo = {
    totaleVeicoli: 0,
    veicoliAssegnati: 0,
    veicoliManutenzione: 0,
    totaleAziende: 0,
    veicoliDisponibili: 0
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
