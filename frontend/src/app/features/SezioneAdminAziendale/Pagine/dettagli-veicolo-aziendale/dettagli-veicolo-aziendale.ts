import { Component } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {DettagliVeicoloAziendaleService } from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dettagli-veicolo-aziendale-service';
import {GoogleMapsService} from '@core/services/google-maps-service';
import {timeout} from 'rxjs';
declare var google:any;

@Component({
  selector: 'app-dettagli-veicolo-aziendale',
  imports: [],
  standalone: true,
  templateUrl: './dettagli-veicolo-aziendale.html',
  styleUrl: './dettagli-veicolo-aziendale.css',
})
export class DettagliVeicoloAziendale {
  constructor(private activeRoute:ActivatedRoute, private service: DettagliVeicoloAziendaleService, private googleMapsService: GoogleMapsService) {}



  ngOnInit(){
    const targa:string | null = this.activeRoute.snapshot.paramMap.get('targa');
    this.richiediVeicolo(targa);
  }
  veicolo:any = null;
  nuovaLat: number = 0;
  nuovaLng: number = 0;

  richiediVeicolo(targa:string|null) {
    if (targa != null) {
      this.service.richiediVeicolo(targa).subscribe({
        next: (response) => {
          if (response) {
            console.log("Dati caricati:", response);
            this.veicolo = response;

            if (this.veicolo.luogoRitiroDeposito) {
              this.nuovaLat = this.veicolo.luogoRitiroDeposito.latitudine;
              this.nuovaLng = this.veicolo.luogoRitiroDeposito.longitudine;
            } else {
              this.nuovaLat = 41.9028;
              this.nuovaLng = 12.4964;
            }
            setTimeout(() => {
              this.inizializzaMappa();
            }, 0);
          }
        }, error:
          (err) => {
            console.error("Errore nel caricamento:", err);
          }
      });

    }
  }
    async inizializzaMappa() {
      await this.googleMapsService.load();
      const mapElement = document.getElementById('map');
      const coords={
        lat: this.veicolo.luogoRitiroDeposito.latitudine,
          lng: this.veicolo.luogoRitiroDeposito.longitudine
      }
      if (mapElement) {
        const map = new google.maps.Map(mapElement, {
          zoom: 15,
          center: coords
        });
        const marker = new google.maps.Marker({
          position: coords,
          map: map,
          title: "Posizione Veicolo",
          draggable: true
        });

        marker.addListener("dragend", (event: any) => {
          const lat = event.latLng.lat();
          const lng = event.latLng.lng();
          this.nuovaLat = lat;
          this.nuovaLng = lng;


          console.log("Nuova posizione rilevata:", lat, lng);
        })
      }
    }




    salvaPosizione() {
    this.veicolo.luogoRitiroDeposito.longitudine=this.nuovaLng;
    this.veicolo.luogoRitiroDeposito.latitudine=this.nuovaLat;
    this.service.aggiornaPosizioneVeicolo(this.veicolo).subscribe({
      next: (response) => {
        if (response) {
          console.log("Modifiche avvenute con successo");
          }
        }, error:
          (err) => {
            console.error("Errore nell'aggiornamento della posizione:", err);
          }
      });

    }






}
