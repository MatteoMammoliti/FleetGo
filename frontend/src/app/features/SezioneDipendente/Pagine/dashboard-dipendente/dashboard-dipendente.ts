import {Component, Inject} from '@angular/core';
import {BannerHome} from '@features/SezioneDipendente/componenti/banner-home/banner-home';
import {ProssimoViaggio} from '@features/SezioneDipendente/componenti/prossimo-viaggio/prossimo-viaggio';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {HomeService} from '@features/SezioneDipendente/ServiceSezioneDipendente/home-service';
import {StatisticheDipendenteDTO} from '@core/models/StatisticheDipendenteDTO';
import {
  StatisticheDipendente
} from '@features/SezioneDipendente/componenti/statistiche-dipendente/statistiche-dipendente';
import {Router} from '@angular/router';
import {MappaHub} from '@features/SezioneDipendente/componenti/mappa-hub/mappa-hub';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {SupportoFleetgo} from '@features/SezioneDipendente/componenti/supporto-fleetgo/supporto-fleetgo';


@Component({
  selector: 'app-dashboard-dipendente',
  imports: [
    BannerHome,
    ProssimoViaggio,
    StatisticheDipendente,
    MappaHub,
    SupportoFleetgo
  ],
  templateUrl: './dashboard-dipendente.html',
  styleUrl: './dashboard-dipendente.css',
})
export class DashboardDipendente {
  constructor(private service:HomeService,private router:Router) {}
    prossimoViaggio:RichiestaNoleggioDTO | undefined;
    statisticheDipendente:StatisticheDipendenteDTO | undefined;
    luoghiAzienda:LuogoDTO[]=[]
    nomeDipendente=""

    ngOnInit(){
      this.richiediNomeDipendente();
      this.richiediProssimoViaggio();
      this.richiediStatisticheDipendente();
      this.richiediLuoghiAzienda();
    }

    richiediNomeDipendente(){
      this.service.richiediNomeDipendente().subscribe({
        next:(risposta:string)=>{
          this.nomeDipendente=risposta;
        },
        error:(err)=>console.error("Errore nel caricmento del nome dipendente")
      })
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

    richiediLuoghiAzienda(){
      this.service.richiediLuoghiAzienda().subscribe({
        next:(risposta:LuogoDTO[])=>{
          this.luoghiAzienda=risposta;
        },
        error:(err)=>console.log("Errore nel caricamento dei luoghi")
      })
    }

    apriPrenotazioni(){
      this.router.navigate(['/dashboardDipendente/nuovaPrenotazione']);
    }
    apriMieAttivita(){
      this.router.navigate(['/dashboardDipendente/prenotazioni'])
    }
}
