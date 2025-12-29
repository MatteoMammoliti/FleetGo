import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {FlottaGlobaleService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/flotta-globale-service';
import {AziendeAffiliateService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {ActivatedRoute} from '@angular/router';
import {GoogleMapsService} from '@core/services/google-maps-service';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import { SceltaTendina } from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {BannerErrore} from "@shared/Componenti/Ui/banner-errore/banner-errore";
declare var google:any;

@Component({
  selector: 'app-dettagli-veicolo',
  standalone: true,
    imports: [
        FormsModule,
        CommonModule,
        TemplateTitoloSottotitolo,
        SceltaTendina,
        BannerErrore
    ],
  templateUrl: './dettagli-veicolo.html',
  styleUrl: './dettagli-veicolo.css',
})

export class DettagliVeicolo implements OnInit {

  constructor(private veicoloService: FlottaGlobaleService,
              private route: ActivatedRoute,
              private aziendeAssociateService:AziendeAffiliateService,
              private googleMapsService:GoogleMapsService) {}

  veicolo: any = null;
  aziende:AziendaDTO[] = [];
  aziendaSelezionata:any=null;

  private map: any;
  private marker: any;
  private infoWindow: any;
  private coordsIniziali: { lat: number, lng: number } | null = null;
  private zoomIniziale: number = 15;

  erroreBanner="";

  ngOnInit(){
    const targa:string | null = this.route.snapshot.paramMap.get('targa');
    this.initVeicolo(targa);
    this.caricaListaAziende();
  }

  caricaListaAziende() {
    this.aziendeAssociateService.richiediAziendeAttive().subscribe({
      next: data => {
        console.log("il backend mi ha mandato questo", data);
        if(data) {
          this.aziende = data;

        }
      }, error: (err) => {
        console.error("Errore aziende:", err)
        this.erroreBanner=("Errore nel caricamento delle aziende")
      }
    });
  }

  initVeicolo(targa: string | null) {
    this.veicoloService.richiediVeicolo(targa).subscribe({
      next: (response) => {
        if (response) {

          console.log("ho ricevuto", response);

          this.veicolo = response;

          const luogo = this.veicolo.luogoRitiroDeposito;
          if (this.veicolo.nomeAziendaAffiliata && luogo && luogo.latitudine && luogo.longitudine) {
            this.initMappa();
          }
        }
      },
      error: (err) => {
        console.error("Errore caricamento veicolo:", err)
        this.erroreBanner=("Errore nel caricamento del veicolo targato: " + targa)
      }
    });
  }

  initMappa() {
    const luogo = this.veicolo.luogoRitiroDeposito;
    if (!luogo || !luogo.latitudine || !luogo.longitudine) return;
    this.coordsIniziali = { lat: luogo.latitudine, lng: luogo.longitudine };

    this.googleMapsService.load().then(() => {
      setTimeout(() => {
        if(this.coordsIniziali) {
            this.disegnaMappa(this.coordsIniziali);
        }
      }, 500);
    });
  }

  disegnaMappa(coords: { lat: number; lng: number}) {
    const mapElement = document.getElementById("google-map");

    if (!mapElement) return;

    const mapOptions = {
      zoom: this.zoomIniziale,
      center: coords,
      disableDefaultUI: true,
      zoomControl: false,
      gestureHandling: 'none',
      clickableIcons: false
    };

    this.map=new google.maps.Map(mapElement, mapOptions);

    this.marker = new google.maps.Marker({
      position: coords,
      map: this.map,
      animation: google.maps.Animation.DROP,
    });

    this.infoWindow = new google.maps.InfoWindow({
      content:`<div style="padding:5px; font-weight:bold; color:#0f172a">${this.veicolo.luogoRitiroDeposito.nomeLuogo}</div>`
    });

    this.marker.addListener("click", () => {
      this.attivaMappa();
    });

    this.infoWindow.addListener('closeclick', () => {
      this.resetMappa();
    });
  }

  attivaMappa() {
    this.map.setZoom(17);
    this.map.setCenter(this.coordsIniziali);
    this.map.setOptions({ gestureHandling: 'cooperative' });
    this.infoWindow.open(this.map, this.marker);
  }

  resetMappa() {
    this.map.setZoom(this.zoomIniziale);
    this.map.setCenter(this.coordsIniziali);
    this.map.setOptions({ gestureHandling: 'none' });
    this.infoWindow.close();
  }

  associaVeicoloAzienda() {

    const veicolo: VeicoloDTO = {
      idVeicolo: this.veicolo.idVeicolo,
      idAziendaAffiliata: this.aziendaSelezionata.idAzienda
    }

    this.veicoloService.associaVeicoloAzienda(veicolo).subscribe({
      next: (response) => {
        console.log(response);
        this.ngOnInit();
      }, error: (err) => {
        console.log(err);
        this.erroreBanner=("Non è stato possibile associare il veicolo all'azienda")
      }
    })
  }

  disassociaVeicoloAzienda() {

    const veicolo: VeicoloDTO = {
      idVeicolo: this.veicolo.idVeicolo,
      idAziendaAffiliata: this.veicolo.idAziendaAffiliata
    }

    this.veicoloService.disassociaVeicoloAzienda(veicolo).subscribe({
      next: (response) => {
        console.log(response);
        this.ngOnInit();
      }, error: (err) => {
        console.log(err);
        this.erroreBanner=("Non è stato possibile disassociare il veicolo all'azienda")
      }
    })
  }

  tornaIndietro(){window.history.back();}
}
