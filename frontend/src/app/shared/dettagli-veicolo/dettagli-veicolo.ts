
import {Component, inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {VeicoloDTO} from '@models/veicoloDTO.model';
import {FlottaGlobaleService} from '@core/services/ServiceSezioneFleetGo/flotta-globale-service';
import {AziendeAffiliateService} from '@core/services/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AziendaDTO} from '@models/aziendaDTO';
import {ActivatedRoute,Router} from '@angular/router';
import {GoogleMapsService} from '@core/services/google-maps-service';

declare var google:any;
@Component({
  selector: 'app-dettagli-veicolo',
  imports: [
    FormsModule,
    NgClass,
  ],
  templateUrl: './dettagli-veicolo.html',
  styleUrl: './dettagli-veicolo.css',
})
export class DettagliVeicolo {
  private veicoloService:FlottaGlobaleService = inject(FlottaGlobaleService);
  private aziendeAssociateService:AziendeAffiliateService = inject(AziendeAffiliateService);
  private activeRoute:ActivatedRoute = inject(ActivatedRoute);
  private googleMapsService:GoogleMapsService = inject(GoogleMapsService);
  veicolo:any = null;
  aziende:AziendaDTO[] = [];
  aziendaSelezionata:any=null;
  statusCambiato:string = "";
  private luogo: any;

  ngOnInit(){
    const targa:string | null = this.activeRoute.snapshot.paramMap.get('targa');
    this.initVeicolo(targa);
  }

  initVeicolo(targa:string | null){
    this.veicoloService.richiediVeicolo(targa).subscribe({
      next: (response) => {
        if (response) {
          console.log("Dati caricati:", response);
          this.veicolo = response;
          console.log(this.veicolo.nomeAziendaAffiliata)
          if(this.veicolo.nomeAziendaAffiliata === null){
            this.initAziende();
          }else {
            this.intiMappa();
          }

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
  intiMappa(){
    this.luogo = this.veicolo.luogoRitiroDeposito;
    this.googleMapsService.load().then(() => {
      setTimeout(() => {
        this.disegnaMappa(this.luogo.latitudine, this.luogo.longitudine);
      }, 500);
    });

  }

  salvaModifiche():void{
    const veicoloDaInviare:VeicoloDTO = {
      idVeicolo:this.veicolo.idVeicolo,
      targaVeicolo:this.veicolo.targaVeicolo,
      idAziendaAffiliata:this.aziendaSelezionata != null ? this.aziendaSelezionata.idAzienda : undefined,
      nomeAziendaAffiliata:this.aziendaSelezionata != null ? this.aziendaSelezionata.nomeAzienda : undefined,
      statusCondizioneVeicolo:this.statusCambiato != "" ? this.statusCambiato : undefined
    }
    console.log(veicoloDaInviare);
    this.veicoloService.inviaModifiche(veicoloDaInviare).subscribe({
      next: (response) => {
        if (response) {
          console.log("Modifiche avvenute con successo");
          this.reset()
          this.ngOnInit();
        }
        }, error:
        (err) => {
        this.reset()
          console.error("Errore nel caricamento:", err);
    }});
  }

  tornaIndietro(){
    window.history.back();
  }

  disegnaMappa(lat: number, lng: number) {
    const coords = { lat: lat, lng: lng };
    const mapElement = document.getElementById("google-map");

    if (mapElement) {
      const map = new google.maps.Map(mapElement, {
        zoom: 15,
        center: coords,
      });

      new google.maps.Marker({
        position: coords,
        map: map,
        title: "Posizione Veicolo"
      });
    }
  }
  reset(){
    this.aziendaSelezionata = null;
    this.statusCambiato = "";
  }
}
