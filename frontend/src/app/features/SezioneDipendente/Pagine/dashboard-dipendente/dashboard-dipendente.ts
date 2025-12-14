import { Component } from '@angular/core';
import {BannerHome} from '@features/SezioneDipendente/componenti/banner-home/banner-home';
import {ProssimoViaggio} from '@features/SezioneDipendente/componenti/prossimo-viaggio/prossimo-viaggio';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {HomeService} from '@features/SezioneDipendente/ServiceSezioneDipendente/home-service';
import {StatisticheDipendenteDTO} from '@core/models/StatisticheDipendenteDTO';
import {
  StatisticheDipendente
} from '@features/SezioneDipendente/componenti/statistiche-dipendente/statistiche-dipendente';

@Component({
  selector: 'app-dashboard-dipendente',
  imports: [
    BannerHome,
    ProssimoViaggio,
    StatisticheDipendente
  ],
  templateUrl: './dashboard-dipendente.html',
  styleUrl: './dashboard-dipendente.css',
})
export class DashboardDipendente {
  constructor(private service:HomeService) {}
    prossimoViaggio:RichiestaNoleggioDTO | undefined;
    statisticheDipendente:StatisticheDipendenteDTO | undefined;
    ngOnInit(){
      this.richiediProssimoViaggio();
      this.richiediStatisticheDipendente();
    }

  richiediProssimoViaggio(){
      this.service.richiediProssimoViaggio().subscribe({
        next:(risposta:RichiestaNoleggioDTO)=>{
          console.log(risposta);
          this.prossimoViaggio=risposta
        },
          error:(err)=>{console.error("errore nel caricamento del prossimo viaggio")}
      });
  }
  richiediStatisticheDipendente(){
      this.service.richiediStatisticheDipendente().subscribe({
        next:(risposta:StatisticheDipendenteDTO)=>{
          console.log(risposta)
          this.statisticheDipendente=risposta;
        },
        error:(err)=>{console.error("Errore nel caricamento delle statistiche dipendente")}
      })
    }
}
