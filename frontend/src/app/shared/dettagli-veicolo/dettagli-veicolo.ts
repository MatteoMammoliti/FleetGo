import {Component, inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgClass, NgForOf} from '@angular/common';
import {VeicoloDTO} from '@models/veicoloDTO.model';
import {FlottaGlobaleService} from '@core/services/ServiceSezioneFleetGo/flotta-globale-service';
import {AziendeAffiliateService} from '@core/services/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AziendaDTO} from '@models/aziendaDTO';
import {ActivatedRoute,Router} from '@angular/router';

@Component({
  selector: 'app-dettagli-veicolo',
  imports: [
    FormsModule,
    NgForOf,
    NgClass
  ],
  templateUrl: './dettagli-veicolo.html',
  styleUrl: './dettagli-veicolo.css',
})
export class DettagliVeicolo {
  private veicoloService:FlottaGlobaleService = inject(FlottaGlobaleService);
  private aziendeAssociateService:AziendeAffiliateService = inject(AziendeAffiliateService);
  private route:Router = inject(Router);
  private activeRoute:ActivatedRoute = inject(ActivatedRoute);
  veicolo:any = null;
  aziende:AziendaDTO[] = [];
  aziendaSelezionata:string = "";
  statusCambiato:string = "";

  ngOnInit(){
    const targa:string | null = this.activeRoute.snapshot.paramMap.get('targa');
    this.initVeicolo(targa);
    if(this.veicolo.idAziendaAssegnata==null){
      this.initAziende();
    }
  }

  initVeicolo(targa:string | null){
    this.veicoloService.richiediVeicolo(targa).subscribe({
      next: (response) => {
        if (response) {
          this.veicolo = response;

        }
      }, error:
        (err) => {
          console.error("Errore nel caricamento:", err);
        }
    });
  }
  initAziende(){
    this.aziendeAssociateService.richiediAziende().subscribe({
      next: (response) => {
        if (response) {
          this.aziende = response;
        }
      },error:
        (err) => {
          console.error("Errore nel caricamento:", err);
        }
    })

  }

  salvaModifiche():void{
    const veicoloDaInviare:VeicoloDTO = {
      idVeicolo:this.veicolo.idVeicolo,
      targaVeicolo:this.veicolo.targaVeicolo,
      nomeAziendaAssociato:this.aziendaSelezionata != "" ? this.aziendaSelezionata : undefined,
      statusCondizioneVeicolo:this.statusCambiato != "" ? this.statusCambiato : undefined
    }
    this.veicoloService.inviaModifiche(veicoloDaInviare).subscribe({
      next: (response) => {
        if (response) {
          console.log("Modifiche avvenute con successo");
          this.ngOnInit();
        }
        }, error:
        (err) => {
          console.error("Errore nel caricamento:", err);
    }});
  }
  tornaIndietro(){
    window.history.back();
  }
}
